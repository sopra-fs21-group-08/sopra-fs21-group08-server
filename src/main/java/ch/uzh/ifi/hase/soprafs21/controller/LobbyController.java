package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.rest.LobbyDTO.LobbyGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.LobbyDTO.LobbyPostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.UserDTO.UserPutDTO;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.LobbyDTOMapper;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.UserDTOMapper;
import ch.uzh.ifi.hase.soprafs21.service.LobbyService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class LobbyController {

    private final LobbyService lobbyService;
    private final UserController userController;

    public LobbyController(LobbyService lobbyService, UserController userController) {
        this.lobbyService = lobbyService;
        this.userController = userController;
    }

    @PostMapping("/rooms")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public LobbyGetDTO createLobby(@RequestBody LobbyPostDTO lobbyPostDTO) {
        //convert Lobby representation to internal repr.
        Lobby lobbyInput = LobbyDTOMapper.INSTANCE.covertLobbyPostDToLobby(lobbyPostDTO);

        //createLobby
        Lobby createdLobby = lobbyService.createLobby(lobbyInput);

        //convert Lobby to DTO object and respond
        return LobbyDTOMapper.INSTANCE.convertLobbyToGetDTO(createdLobby);
    }

    @PostMapping("/rooms/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public LobbyGetDTO joinLobby(@RequestBody UserPutDTO userPutDTO, @PathVariable("id") long id) {
        //convert Lobby representation to internal repr.
        User joinedUser = UserDTOMapper.INSTANCE.convertUserPutDTOtoEntity(userPutDTO);




        //convert Lobby to DTO object and respond
        return null;
    }




}
