package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.rest.LobbyDTO.GetAllLobbiesDTO;
import ch.uzh.ifi.hase.soprafs21.rest.LobbyDTO.LobbyGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.LobbyDTO.LobbyPostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.UserDTO.UserGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.UserDTO.UserPutDTO;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.LobbyDTOMapper;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.UserDTOMapper;
import ch.uzh.ifi.hase.soprafs21.service.LobbyService;
import ch.uzh.ifi.hase.soprafs21.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class LobbyController {

    private final LobbyService lobbyService;
    private final UserService userService;

    public LobbyController(LobbyService lobbyService, UserService userService) {
        this.lobbyService = lobbyService;
        this.userService = userService;
    }

    @PostMapping("/lobbies")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public LobbyGetDTO createLobby(@RequestBody LobbyPostDTO lobbyPostDTO) {

        //convert Lobby representation to internal repr.
        Lobby lobbyInput = LobbyDTOMapper.INSTANCE.covertLobbyPostDTOToEntity(lobbyPostDTO);

        //find user
        User foundUser = userService.getUserById(lobbyPostDTO.getUserId());

        //createLobby
        Lobby createdLobby = lobbyService.createLobby(lobbyInput, foundUser);

        //set user to being in lobby
        userService.setUserLobby(foundUser, createdLobby);

        //convert Lobby to DTO object and respond
        return LobbyDTOMapper.INSTANCE.convertEntityToLobbyGetDTO(createdLobby);
    }

    @PutMapping("/lobbies/{lobbyId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public LobbyGetDTO joinLobby(@RequestBody UserPutDTO userPutDTO, @PathVariable("lobbyId") long lobbyId) {
        //convert Lobby representation to internal repr.
        User joinedUser = UserDTOMapper.INSTANCE.convertUserPutDTOtoEntity(userPutDTO);

        //find the user by id
        User foundUser = userService.getUserById(joinedUser.getUserId());

        //add user to lobby
        Lobby joinedLobby = lobbyService.joinLobby(foundUser, lobbyId);

        //update user profile
        userService.setUserLobby(foundUser, joinedLobby);

        //convert Lobby to DTO object and respond
        return LobbyDTOMapper.INSTANCE.convertEntityToLobbyGetDTO(joinedLobby);
    }


    @GetMapping("/lobbies/{lobbyId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public LobbyGetDTO getLobbyDetails(@PathVariable("lobbyId") long lobbyId) {

        // find the user by lobbyId
        List<User> users = lobbyService.getUsers(lobbyId);

        // find the lobby
        Lobby foundLobby = lobbyService.findLobbyById(lobbyId);

        // convert lobby to DTO (lobbyId, lobbyName, gameStarted, list<user>
        LobbyGetDTO lobbyGetDTO = LobbyDTOMapper.INSTANCE.convertEntityToLobbyGetDTO(foundLobby);
        List<UserGetDTO> userGetDTOs = new ArrayList<>();

        // convert each user to the API representation
        for (User user : users) {
            userGetDTOs.add(UserDTOMapper.INSTANCE.convertEntityToUserGetDTO(user));
        }

        lobbyGetDTO.setUsers(userGetDTOs);

        return lobbyGetDTO;
    }

    @DeleteMapping("/lobbies/{lobbyId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void removeUserInLobby(@RequestBody UserPutDTO userPutDTO, @PathVariable("lobbyId") long lobbyId){
        User userToRemove = UserDTOMapper.INSTANCE.convertUserPutDTOtoEntity(userPutDTO);
        userToRemove = userService.getUserById(userToRemove.getUserId());

        userService.leaveCurrentLobby(userToRemove);

        lobbyService.leaveLobby(userToRemove, lobbyId);
    }

    @GetMapping("/lobbies")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<GetAllLobbiesDTO> getAllLobbies() {

        // get all lobbies
        List<Lobby> lobbies = lobbyService.getLobbies();
        List<GetAllLobbiesDTO> getAllLobbiesDTO = new ArrayList<>();

        // convert each user to the API representation
        for (Lobby lobby : lobbies) {
            getAllLobbiesDTO.add(LobbyDTOMapper.INSTANCE.convertEntityToGetAllDTO(lobby));
        }
        return getAllLobbiesDTO;
    }


    @PostMapping("/lobbies/{lobbyId}/replay")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void nextLobby(@PathVariable("lobbyId") long lobbyId,
                          @RequestHeader("Authorization") String token){

        User issuingUser = userService.findUserByToken(token);

        userService.leaveCurrentLobby(issuingUser);

        lobbyService.playAgain(lobbyId, issuingUser);
    }
}
