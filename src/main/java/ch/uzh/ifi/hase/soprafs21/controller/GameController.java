package ch.uzh.ifi.hase.soprafs21.controller;


import ch.uzh.ifi.hase.soprafs21.rest.MoveDTO.MoveDTO;
import ch.uzh.ifi.hase.soprafs21.rest.PlayerDTO.PlayerGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.StationDTO.StationDTO;
import ch.uzh.ifi.hase.soprafs21.rest.TicketDTO.TicketDTO;
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
    public void createGame(@PathVariable("lobbyId") long lobbyId){

    }

    @GetMapping("/games/{gameId}/players")
    @ResponseStatus(HttpStatus.OK)
    public List<PlayerGetDTO> getPlayers(@PathVariable("gameId") long gameId,
                                         @RequestHeader("Authorization") String token){

        return new ArrayList<PlayerGetDTO>();
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
}
