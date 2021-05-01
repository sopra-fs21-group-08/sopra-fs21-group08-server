package ch.uzh.ifi.hase.soprafs21.controller;


import ch.uzh.ifi.hase.soprafs21.GameEntities.Game;
import ch.uzh.ifi.hase.soprafs21.GameEntities.Movement.Move;
import ch.uzh.ifi.hase.soprafs21.GameEntities.Movement.Round;
import ch.uzh.ifi.hase.soprafs21.GameEntities.Players.Player;
import ch.uzh.ifi.hase.soprafs21.GameEntities.Players.PlayerGroup;
import ch.uzh.ifi.hase.soprafs21.GameEntities.TicketWallet.Ticket;
import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.network.Station;
import ch.uzh.ifi.hase.soprafs21.rest.MoveDTO.MoveDTO;
import ch.uzh.ifi.hase.soprafs21.rest.TicketDTO.TicketDTO;
import ch.uzh.ifi.hase.soprafs21.service.GameService;
import ch.uzh.ifi.hase.soprafs21.service.LobbyService;
import ch.uzh.ifi.hase.soprafs21.service.StationService;
import ch.uzh.ifi.hase.soprafs21.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static ch.uzh.ifi.hase.soprafs21.TestHelpers.TestHelpers.asJsonString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GameController.class)
public class GameControllerTest {

    // given
    private Lobby testLobby;
    private Game testGame;
    private Round testRound;
    private PlayerGroup testPlayerGroup;
    private Player testPlayer1;
    private User testUser1;
    private Station testStation1;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LobbyService lobbyService;
    @MockBean
    private UserService userService;
    @MockBean
    private StationService stationService;
    @MockBean
    private GameService gameService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testLobby = new Lobby();
        testLobby.setLobbyId(1L);
        testLobby.setLobbyName("ROOM1");

        testGame = new Game();
        testGame.setGameId(1L);

        testRound = new Round();

        testPlayerGroup = new PlayerGroup();

        testPlayer1 = new Player();
        testPlayer1.setPlayerId(1L);

        testUser1 = new User();
        testUser1.setUserId(1L);
        testUser1.setToken(UUID.randomUUID().toString());

        testStation1 = new Station();
        testStation1.setStationId(1L);
    }

    @AfterEach
    void tearDown(){

        testLobby = null;
        testGame = null;
        testRound = null;
        testPlayerGroup = null;
        testPlayer1 = null;

    }

    @Test
    public void createGame_validInput_gameCreate() throws Exception{

        when(this.lobbyService.findLobbyById(testLobby.getLobbyId()))
                .thenReturn(testLobby);
        when(gameService.initializeGame(testLobby))
                .thenReturn(testGame);

        MockHttpServletRequestBuilder postRequest = post("/games/{lobbyId}",
                testLobby.getLobbyId())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(postRequest)
                .andExpect(status().isCreated());
    }

    @Test
    public void getPlayers_validInput_playersReceived() throws Exception{

        when(gameService.getPlayerGroupByGameId(testGame.getGameId()))
                .thenReturn(testPlayerGroup);

        MockHttpServletRequestBuilder getRequest = get("/games/{gameId}/players",
                testGame.getGameId())
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", testUser1.getToken());

        mockMvc.perform(getRequest)
                .andExpect(status().isOk());
    }

    @Test
    public void getPlayer_validInput_playerReceived() throws Exception{

        when(gameService.getGameByGameId(testGame.getGameId()))
                .thenReturn(testGame);
        when(userService.getUserById(testUser1.getUserId()))
                .thenReturn(testUser1);
        when(gameService.getPlayerByGameUserEntities(testGame, testUser1))
                .thenReturn(testPlayer1);

        MockHttpServletRequestBuilder getRequest = get("/games/{gameId}/players/{userId}",
                testGame.getGameId(), testUser1.getUserId())
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", testUser1.getToken());

        mockMvc.perform(getRequest)
                .andExpect(status().isOk());
    }

    @Test
    public void getMrX_validInput_MrXReceived() throws Exception{

        testPlayerGroup.add(testPlayer1);
        testGame.setPlayerGroup(testPlayerGroup);

        when(gameService.getGameByGameId(testGame.getGameId()))
                .thenReturn(testGame);


        MockHttpServletRequestBuilder getRequest = get("/games/{gameId}/mrX",
                testGame.getGameId())
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", testUser1.getToken());

        mockMvc.perform(getRequest)
                .andExpect(status().isOk());
    }

    @Test
    public void getPossibleMoves_validInput_movesReceived() throws Exception{

        TicketDTO ticketDTO = new TicketDTO();
        ticketDTO.setTicket(Ticket.TRAM);

        List<Station> testStationList = new ArrayList<>();
        testStationList.add(testStation1);

        when(gameService.getGameByGameId(testGame.getGameId()))
                .thenReturn(testGame);
        when(userService.getUserById(testUser1.getUserId()))
                .thenReturn(testUser1);
        when(gameService.possibleStations(testGame, testUser1, ticketDTO.getTicket()))
                .thenReturn(testStationList);

        MockHttpServletRequestBuilder getRequest = get("/games/{gameId}/moves/validate/{userId}",
                testGame.getGameId(), testUser1.getUserId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(ticketDTO))
                .header("Authorization", testUser1.getToken());

        mockMvc.perform(getRequest)
                .andExpect(status().isOk());
    }

    @Test
    public void createMove_validInput_moveCreated() throws Exception{

        MoveDTO moveDTO = new MoveDTO();
        moveDTO.setTo(testStation1.getStationId());
        moveDTO.setTicket(Ticket.TRAM);

        Move testMove = new Move();
        testMove.setMoveId(1L);


        when(stationService.getStationById(moveDTO.getTo()))
                .thenReturn(testStation1);
        when(userService.getUserById(testUser1.getUserId()))
                .thenReturn(testUser1);
        when(gameService.playerIssuesMove(testUser1, testMove, testGame.getGameId()))
                .thenReturn(testPlayer1);


        MockHttpServletRequestBuilder postRequest = post("/games/{gameId}/moves/{userId}",
                testLobby.getLobbyId(), testUser1.getUserId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(moveDTO))
                .header("Authorization", testUser1.getToken());;

        mockMvc.perform(postRequest)
                .andExpect(status().isCreated());
    }

    /*
    @Test
    public void getBlackboard_validInput_blackboardReceived() throws Exception{

    }*/

    @Test
    public void getStatus_validInput_statusReceived() throws Exception{

        when(gameService.getGameByGameId(testGame.getGameId())).thenReturn(testGame);

        MockHttpServletRequestBuilder getRequest = get("/games/{gameId}/status",
                testGame.getGameId())
                .header("Authorization", testUser1.getToken());

        mockMvc.perform(getRequest)
                .andExpect(status().isOk());
    }
}
