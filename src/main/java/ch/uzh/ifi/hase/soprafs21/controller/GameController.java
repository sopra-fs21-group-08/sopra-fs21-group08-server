package ch.uzh.ifi.hase.soprafs21.controller;


import ch.uzh.ifi.hase.soprafs21.GameEntities.Game;
import ch.uzh.ifi.hase.soprafs21.GameEntities.Players.Player;
import ch.uzh.ifi.hase.soprafs21.GameEntities.Players.PlayerGroup;
import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.rest.GameStatusDTO.GameStatusGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.MoveDTO.MoveDTO;
import ch.uzh.ifi.hase.soprafs21.rest.PlayerDTO.PlayerGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.StationDTO.StationDTO;
import ch.uzh.ifi.hase.soprafs21.rest.TicketDTO.TicketDTO;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.PlayerDTOMapper;
import ch.uzh.ifi.hase.soprafs21.service.GameService;
import ch.uzh.ifi.hase.soprafs21.service.LobbyService;
import ch.uzh.ifi.hase.soprafs21.service.StationService;
import ch.uzh.ifi.hase.soprafs21.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class GameController {
    private final LobbyService lobbyService;
    private final UserService userService;
    private final GameService gameService;
    private final StationService stationService;

    public GameController(LobbyService lobbyService, UserService userService, GameService gameService,
                          StationService stationService) {
        this.lobbyService = lobbyService;
        this.userService = userService;
        this.gameService = gameService;
        this.stationService = stationService;
    }

    @PostMapping("/games/{lobbyId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void createGame(@PathVariable("lobbyId") long lobbyId) {

        Lobby foundLobby = this.lobbyService.findLobbyById(lobbyId);

        //initializing game
        Game game = gameService.initializeGame(foundLobby);

        lobbyService.setLobbyGame(foundLobby, game);
    }




    @GetMapping("/games/{gameId}/players")
    @ResponseStatus(HttpStatus.OK)
    public List<PlayerGetDTO> getPlayers(@PathVariable("gameId") long gameId,
                                         @RequestHeader("Authorization") String token){

        //TODO: why is authentication here required? anyone should be able to see which people are playing in a certan game

        // finds the game in the repository
        Game foundGame = gameService.getGameByEntity(createGameFromId(gameId));


        // converts the game using defined private method below and send it
        return convertPlayerGroupToDTO(foundGame.getPlayerGroup());
    }


    @GetMapping("/games/{gameId}/players/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public PlayerGetDTO getPlayer(@PathVariable("gameId") long gameId,
                                  @PathVariable("userId") long userId,
                                  @RequestHeader("Authorization") String token){

        return new PlayerGetDTO();
    }



    @GetMapping("/games/{gameId}/mrX")
    @ResponseStatus(HttpStatus.OK)
    public PlayerGetDTO getMrX(@PathVariable("gameId") long gameId,
                               @RequestHeader("Authorization") String token){

        return new PlayerGetDTO();
    }

    @GetMapping("/games/{gameId}/moves/validate/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public List<StationDTO> getPossibleMoves(@RequestBody TicketDTO ticketDTO,
                                             @PathVariable("gameId") long gameId,
                                             @PathVariable("userId") long userId,
                                             @RequestHeader("Authorization") String token){

        return new ArrayList<>();
    }

    @PostMapping("/games/{gameId}/moves/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void createMove(@RequestBody MoveDTO moveDTO,
                           @PathVariable("gameId") long gameId,
                           @PathVariable("userId") long userId,
                           @RequestHeader("Authorization") String token){

    }

    @GetMapping("/games/{gameId}/moves/blackboards")
    @ResponseStatus(HttpStatus.OK)
    public List<MoveDTO> getBlackboard(@PathVariable("gameId") long gameId,
                                       @RequestHeader("Authorization") String token){

        return new ArrayList<>();
    }

    @GetMapping("/games/{gameId}/status")
    @ResponseStatus(HttpStatus.OK)
    public GameStatusGetDTO getStatus(@PathVariable("gameId") long gameId,
                                      @RequestHeader("Authorization") String token){

        return new GameStatusGetDTO();
    }

    private Game createGameFromId(long id){
        Game game = new Game();
        game.setGameId(id);
        return game;
    }

    private List<PlayerGetDTO> convertPlayerGroupToDTO(PlayerGroup pg){

        List<PlayerGetDTO> list = new ArrayList<>();

        for(Player p :pg){
            // converting player to DTO         :    playerId and playerclass
            // remaining attributes to convert  :    Wallets, user
            PlayerGetDTO dto = PlayerDTOMapper.INSTANCE.convertPlayerToGetDTO(p);

            // converting user to DTO (UserGetDTO) and assigning to PlayerGetDTO
            // dto.setUser(UserDTOMapper.INSTANCE.convertEntityToUserGetDTO(p.getUser()));

            //converting Wallet to DTO .....


            // adding playerGetDTO to dto list
            list.add(dto);
        }

        return list;

    }
}
