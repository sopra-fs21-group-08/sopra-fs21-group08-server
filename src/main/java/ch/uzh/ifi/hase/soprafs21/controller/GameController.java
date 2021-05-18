package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.GameEntities.Game;
import ch.uzh.ifi.hase.soprafs21.GameEntities.Movement.Move;
import ch.uzh.ifi.hase.soprafs21.GameEntities.Players.Player;
import ch.uzh.ifi.hase.soprafs21.GameEntities.TicketWallet.Ticket;
import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.network.Station;
import ch.uzh.ifi.hase.soprafs21.rest.GameStatusDTO.GameStatusGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.MoveDTO.MoveDTO;
import ch.uzh.ifi.hase.soprafs21.rest.PlayerDTO.PlayerGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.StationDTO.StationDTO;
import ch.uzh.ifi.hase.soprafs21.rest.TicketDTO.TicketDTO;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.GameDTOMapper;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.PlayerDTOMapper;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.StationDTOMapper;
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

        //find corresponding lobby
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

        //TODO: change this method ("findUserbyToken()") to throw exception if token isnt found,
        // this will be our authentication, if token is found user is allowed to manipulate entities

        User foundUser = userService.findUserByToken(token);

        //TODO: remove and add to every method that changes game state
        gameService.isUserInGame(gameId, foundUser);

        List<PlayerGetDTO> playerGetDTOS = new ArrayList<>();

        for (Player player : gameService.getPlayerPositions(gameId, foundUser)){
            playerGetDTOS.add(PlayerDTOMapper.INSTANCE.convertPlayerToGetDTO(player));
        }

        return playerGetDTOS;
    }


    @GetMapping("/games/{gameId}/players/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public PlayerGetDTO getPlayer(@PathVariable("gameId") long gameId,
                                  @PathVariable("userId") long userId,
                                  @RequestHeader("Authorization") String token){

        // TODO : Authentification as always

        Game foundGame = gameService.getGameByGameId(gameId);
        User foundUser = userService.getUserById(userId);
        Player foundPlayer = gameService.getPlayerByGameUserEntities(foundGame, foundUser);

        return PlayerDTOMapper.INSTANCE.convertPlayerToGetDTO(foundPlayer);
    }



    @GetMapping("/games/{gameId}/mrX")
    @ResponseStatus(HttpStatus.OK)
    public PlayerGetDTO getMrX(@PathVariable("gameId") long gameId,
                               @RequestHeader("Authorization") String token){

        // TODO : Authentication as always

        return PlayerDTOMapper.INSTANCE.convertPlayerToGetDTO(gameService.getGameByGameId(gameId).getMrX());
    }

    @PostMapping("/games/{gameId}/moves/validate/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public List<StationDTO> getPossibleMoves(@RequestBody TicketDTO ticketDTO,
                                             @PathVariable("gameId") long gameId,
                                             @PathVariable("userId") long userId,
                                             @RequestHeader("Authorization") String token){

        // TODO : Authentication as always

        Game foundGame = gameService.getGameByGameId(gameId);
        User foundUser = userService.getUserById(userId);

        //TODO: check if user is in the game
        List<Station> possibleStationList = gameService.possibleStations(foundGame, foundUser,
                ticketDTO.getTicket());

        List<StationDTO> possibleStationDTOList = new ArrayList<>();

        for (Station station : possibleStationList){
            possibleStationDTOList.add(StationDTOMapper.INSTANCE.convertEntityToStationDTO(station));
        }
        return possibleStationDTOList;
    }

    @PostMapping("/games/{gameId}/moves/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public PlayerGetDTO createMove(@RequestBody MoveDTO moveDTO,
                           @PathVariable("gameId") long gameId,
                           @PathVariable("userId") long userId,
                           @RequestHeader("Authorization") String token){

        Move issuedMove = new Move();
        issuedMove.setTicket(moveDTO.getTicket());
        issuedMove.setTo(stationService.getStationById(moveDTO.getTo()));

                //MoverDTOMapper.INSTANCE.convertDTOtoMove(moveDTO);
        User issuingUser = userService.getUserById(userId);

        //TODO authenticate user

        Player movedPlayer = gameService.playerIssuesMove(issuingUser, issuedMove, gameId);

        return PlayerDTOMapper.INSTANCE.convertPlayerToGetDTO(movedPlayer);
    }

    @GetMapping("/games/{gameId}/moves/blackboards")
    @ResponseStatus(HttpStatus.OK)
    public List<TicketDTO> getBlackboard(@PathVariable("gameId") long gameId,
                                      @RequestHeader("Authorization") String token){
        List<TicketDTO> blackboard = new ArrayList<>();

        for(Ticket ticket :gameService.getBlackboard(gameId)){
            TicketDTO ticketDTO = new TicketDTO();
            ticketDTO.setTicket(ticket);
            blackboard.add(ticketDTO);
        }

        return blackboard;
    }

    @GetMapping("/games/{gameId}/status")
    @ResponseStatus(HttpStatus.OK)
    public GameStatusGetDTO getStatus(@PathVariable("gameId") long gameId,
                                      @RequestHeader("Authorization") String token){

        return GameDTOMapper.INSTANCE.convertEntityToGameStatusGetDTO(gameService.getGameStatusById(gameId));
    }

    @PostMapping("/games/{gameId}/hack")
    @ResponseStatus(HttpStatus.CREATED)
    public void hack(@PathVariable("gameId") long gameId,
                     @RequestHeader("Authorization") String token){

        gameService.hack(gameId);
    }


}
