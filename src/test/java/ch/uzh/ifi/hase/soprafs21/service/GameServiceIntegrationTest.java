package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.GameEntities.Game;
import ch.uzh.ifi.hase.soprafs21.GameEntities.Movement.Move;
import ch.uzh.ifi.hase.soprafs21.GameEntities.Players.Player;
import ch.uzh.ifi.hase.soprafs21.GameEntities.TicketWallet.Ticket;
import ch.uzh.ifi.hase.soprafs21.constant.PlayerClass;
import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs21.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs21.repository.StationRepository;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@WebAppConfiguration
@SpringBootTest
public class GameServiceIntegrationTest {

    // given
    private Lobby testLobby1;
    private User testUser1;
    private User testUser2;
    private User testUser3;
    private Move tram232to183;
    private Move tram194to232;
    private Move tram16to242;

    @Qualifier("gameRepository")
    @Autowired
    private GameRepository gameRepository;

    @Qualifier("stationRepository")
    @Autowired
    private StationRepository stationRepository;

    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;

    @Qualifier("lobbyRepository")
    @Autowired
    private LobbyRepository lobbyRepository;

    @Autowired
    private GameService gameService;

    @Autowired
    private LobbyService lobbyService;

    @Autowired
    private UserService userService;


    @BeforeEach
    public void setUp(){
        testLobby1 = new Lobby();
        testLobby1.setLobbyId(1L);
        testLobby1.setLobbyName("testLobby1");

        testUser1 = new User();
        testUser1.setUsername("testUser1");
        testUser1.setPassword("password1");
        userService.createUser(testUser1);

        testUser2 = new User();
        testUser2.setUsername("testUser2");
        testUser2.setPassword("password2");
        userService.createUser(testUser2);

        testUser3 = new User();
        testUser3.setUsername("testUser3");
        testUser3.setPassword("password3");
        userService.createUser(testUser3);

        tram232to183 = new Move();
        tram232to183.setTicket(Ticket.TRAM);
        tram232to183.setFrom(stationRepository.findByStationId(232L));
        tram232to183.setTo(stationRepository.findByStationId(183L));

        tram194to232 = new Move();
        tram194to232.setTicket(Ticket.TRAM);
        tram194to232.setFrom(stationRepository.findByStationId(194L));
        tram194to232.setTo(stationRepository.findByStationId(232L));

        tram16to242 = new Move();
        tram16to242.setTicket(Ticket.TRAM);
        tram16to242.setFrom(stationRepository.findByStationId(16L));
        tram16to242.setTo(stationRepository.findByStationId(242L));

        testLobby1 = lobbyService.createLobby(testLobby1, testUser1);
        lobbyService.joinLobby(testUser2, testLobby1.getLobbyId());
        lobbyService.joinLobby(testUser3, testLobby1.getLobbyId());
    }

    @AfterEach
    public void tearDown(){
        userRepository.deleteAll();
        lobbyRepository.deleteAll();
        gameRepository.deleteAll();
        testLobby1 = null;
        testUser1 = null;
        testUser2 = null;
        testUser3 = null;
        tram232to183 = null;
        tram194to232 = null;
        tram16to242 = null;
    }

    @Test
    public void initializeGame_validInput_gameCreated(){

        Game createdGame = gameService.initializeGame(testLobby1);

        assertEquals(gameRepository.findByGameId(createdGame.getGameId()), createdGame);
        assertEquals(userRepository.findByUserId(testUser1.getUserId()),testUser1);
        assertEquals(userRepository.findByUserId(testUser2.getUserId()),testUser2);
        assertEquals(userRepository.findByUserId(testUser3.getUserId()),testUser3);

        assertEquals(gameService.getGameByGameId(createdGame.getGameId()), createdGame);
        assertEquals(gameService.getPlayerGroupByGameId(createdGame.getGameId()), createdGame.getPlayerGroup());
        assertEquals(createdGame.getCurrentRound().getRoundNumber(), 1);
        assertNotNull(createdGame.getBlackboard());
        assertEquals(createdGame.getBlackboard().getTickets(), gameService.getBlackboard(createdGame.getGameId()));
        assertEquals(createdGame.getMrX(), createdGame.getCurrentPlayer());
        assertEquals(createdGame.findCorrespondingPlayer(testUser1).getUser(), testUser1);
        assertEquals(createdGame.findCorrespondingPlayer(testUser2).getUser(), testUser2);
        assertEquals(createdGame.findCorrespondingPlayer(testUser3).getUser(), testUser3);
        assertDoesNotThrow(() -> gameService.isUserInGame(testLobby1.getLobbyId(),testUser1));
        assertDoesNotThrow(() -> gameService.isUserInGame(testLobby1.getLobbyId(),testUser2));
        assertDoesNotThrow(() -> gameService.isUserInGame(testLobby1.getLobbyId(),testUser3));

    }

    @Test
    public void playerIssuesMove_validInput_playerMoved(){

        Game createdGame = gameService.initializeGame(testLobby1);
        Player currPlayer = createdGame.getCurrentPlayer();
        User currUser = currPlayer.getUser();

        final int busTicket = currPlayer.getWallet().getBus();
        final int tramTicket = currPlayer.getWallet().getTram();
        final int trainTicket = currPlayer.getWallet().getTrain();

        Move testMove = gameService.createValidMove(createdGame, currUser);

        gameService.playerIssuesMove(currUser, testMove, createdGame.getGameId());

        assertEquals(currPlayer.getCurrentStation(), testMove.getTo());
        if (testMove.getTicket() == Ticket.TRAM){
            assertEquals(tramTicket, currPlayer.getWallet().getTram() + 1);
        }else if (testMove.getTicket() == Ticket.BUS){
            assertEquals(busTicket, currPlayer.getWallet().getBus() + 1);
        }else if (testMove.getTicket() == Ticket.TRAIN){
            assertEquals(trainTicket, currPlayer.getWallet().getTrain() + 1);
        } else{
            throw new IllegalStateException("Something went terribly wrong while testing.");
        }
        assertEquals(currPlayer.getCurrentStation(), testMove.getTo());
        assertNotEquals(createdGame.getCurrentPlayer(), currPlayer);

    }

    @Test
    public void playerIssuesMove_validInputTram_playerMoved(){

        Game createdGame = gameService.initializeGame(testLobby1);
        gameService.hack(createdGame.getGameId());
        Player currPlayer = createdGame.getCurrentPlayer();
        User currUser = currPlayer.getUser();

        final int tramTicket = currPlayer.getWallet().getTram();

        gameService.playerIssuesMove(currUser, tram232to183, createdGame.getGameId());

        assertEquals(currPlayer.getCurrentStation(), tram232to183.getTo());
        assertEquals(tramTicket, currPlayer.getWallet().getTram() + 1);
        assertNotEquals(createdGame.getCurrentPlayer(), currPlayer);

    }




    @Test
    public void playerIssuesMove_invalidInput_invalidStation(){

        Game createdGame = gameService.initializeGame(testLobby1);
        Player currPlayer = createdGame.getCurrentPlayer();
        User currUser = currPlayer.getUser();
        currPlayer.setCurrentStation(stationRepository.findByStationId(232));

        Move testMove = new Move();
        testMove.setTicket(Ticket.TRAM);
        testMove.setFrom(stationRepository.findByStationId(232));
        testMove.setTo(stationRepository.findByStationId(25));

        assertThrows(ResponseStatusException.class,
                () -> gameService.playerIssuesMove(currUser, testMove, createdGame.getGameId()));

    }

    @Test
    public void playerIssuesMove_invalidInput_invalidTicket(){

        Game createdGame = gameService.initializeGame(testLobby1);
        Player currPlayer = createdGame.getCurrentPlayer();
        User currUser = currPlayer.getUser();
        currPlayer.setCurrentStation(stationRepository.findByStationId(232));

        Move testMove = new Move();
        testMove.setTicket(Ticket.BUS);
        testMove.setFrom(stationRepository.findByStationId(232));
        testMove.setTo(stationRepository.findByStationId(16));

        assertThrows(ResponseStatusException.class,
                () -> gameService.playerIssuesMove(currUser, testMove, createdGame.getGameId()));

    }


    @Test
    public void playerIssuesMove_validInput_gameTerminated_MrXSuicide(){

        Game createdGame = gameService.initializeGame(testLobby1);
        Player currPlayer = createdGame.getCurrentPlayer();
        User currUser = currPlayer.getUser();

        gameService.hack(createdGame.getGameId());

        Move testMove = new Move();
        testMove.setTicket(Ticket.TRAM);
        testMove.setTo(stationRepository.findByStationId(194L));

        gameService.playerIssuesMove(currUser, testMove, createdGame.getGameId());

        assertEquals(createdGame.isGameOver(), true);

    }

    @Test
    public void fullRound_validInput_nextRoundInitiated(){
        Game createdGame = gameService.initializeGame(testLobby1);
        gameService.hack(createdGame.getGameId());


        gameService.playerIssuesMove(createdGame.getCurrentPlayer().getUser(),
                tram232to183, createdGame.getGameId());
        gameService.playerIssuesMove(createdGame.getCurrentPlayer().getUser(),
                tram194to232, createdGame.getGameId());
        gameService.playerIssuesMove(createdGame.getCurrentPlayer().getUser(),
                tram16to242, createdGame.getGameId());

        assertEquals(2, createdGame.getCurrentRound().getRoundNumber());
        assertEquals(PlayerClass.MRX, createdGame.getCurrentPlayer().getPlayerClass());
        assertEquals(tram232to183.getTicket(), createdGame.getBlackboard().getTickets().get(0));

    }
}
