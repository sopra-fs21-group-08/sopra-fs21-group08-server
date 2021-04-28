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
import ch.uzh.ifi.hase.soprafs21.rest.mapper.GameDTOMapper;
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
    public GameStatusGetDTO createGame(@PathVariable("lobbyId") long lobbyId) {

        //find coresponding lobby
        Lobby foundLobby = this.lobbyService.findLobbyById(lobbyId);

        //initializing game
        Game game = gameService.initializeGame(foundLobby);
        lobbyService.setLobbyGame(foundLobby, game);

        return GameDTOMapper.INSTANCE.convertEntityToGameStatusGetDTO(game);
    }




    @GetMapping("/games/{gameId}/players")
    @ResponseStatus(HttpStatus.OK)
    public List<PlayerGetDTO> getPlayers(@PathVariable("gameId") long gameId,
                                         @RequestHeader("Authorization") String token){

        //TODO: why is authentication here required? anyone should be able to see which people are playing in a certain game

        // finds the game in the repository

        List<PlayerGetDTO> playerGetDTOS = new ArrayList<>();

        for (Player player : gameService.getPlayerGroupByGameId(gameId)){
            playerGetDTOS.add(PlayerDTOMapper.INSTANCE.convertPlayerToGetDTO(player));
        }

        return playerGetDTOS;
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

        return GameDTOMapper.INSTANCE.convertEntityToGameStatusGetDTO(gameService.getGameByGameId(gameId));
    }


}
