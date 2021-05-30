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

import java.util.List;
import java.util.Objects;

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
    private Move tram183to232;

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

        tram183to232 = new Move();
        tram183to232.setTicket(Ticket.TRAM);
        tram183to232.setFrom(stationRepository.findByStationId(183L));
        tram183to232.setTo(stationRepository.findByStationId(232L));

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
        tram183to232 = null;
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
        int i = 0;
        for (Player player: createdGame.getPlayerGroup()){
            if (i==0){
                assertEquals(PlayerClass.MRX, player.getPlayerClass());
            }
            else{
                assertEquals(PlayerClass.DETECTIVE, player.getPlayerClass());
            }
            i++;
        }
        assertEquals(createdGame.findCorrespondingPlayer(testUser1).getUser(), testUser1);
        assertEquals(createdGame.findCorrespondingPlayer(testUser2).getUser(), testUser2);
        assertEquals(createdGame.findCorrespondingPlayer(testUser3).getUser(), testUser3);
        assertDoesNotThrow(() -> gameService.isUserInGame(testLobby1.getLobbyId(),testUser1));
        assertDoesNotThrow(() -> gameService.isUserInGame(testLobby1.getLobbyId(),testUser2));
        assertDoesNotThrow(() -> gameService.isUserInGame(testLobby1.getLobbyId(),testUser3));

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
    public void playerIssuesMove_validInputBus_playerMoved(){

        Game createdGame = gameService.initializeGame(testLobby1);
        gameService.hack(createdGame.getGameId());
        Player currPlayer = createdGame.getCurrentPlayer();
        currPlayer.setCurrentStation(stationRepository.findByStationId(310L));
        User currUser = currPlayer.getUser();

        Move bus310to105 = new Move();
        bus310to105.setTicket(Ticket.BUS);
        bus310to105.setFrom(stationRepository.findByStationId(310L));
        bus310to105.setTo(stationRepository.findByStationId(105L));

        final int busTicket = currPlayer.getWallet().getBus();

        gameService.playerIssuesMove(currUser, bus310to105, createdGame.getGameId());

        assertEquals(currPlayer.getCurrentStation(), bus310to105.getTo());
        assertEquals(busTicket, currPlayer.getWallet().getBus() + 1);
        assertNotEquals(createdGame.getCurrentPlayer(), currPlayer);

    }

    @Test
    public void playerIssuesMove_validInputTrain_playerMoved(){

        Game createdGame = gameService.initializeGame(testLobby1);
        gameService.hack(createdGame.getGameId());
        Player currPlayer = createdGame.getCurrentPlayer();
        currPlayer.setCurrentStation(stationRepository.findByStationId(105L));
        User currUser = currPlayer.getUser();

        Move train105to308 = new Move();
        train105to308.setTicket(Ticket.TRAIN);
        train105to308.setFrom(stationRepository.findByStationId(105L));
        train105to308.setTo(stationRepository.findByStationId(308L));

        final int trainTicket = currPlayer.getWallet().getTrain();

        gameService.playerIssuesMove(currUser, train105to308, createdGame.getGameId());

        assertEquals(currPlayer.getCurrentStation(), train105to308.getTo());
        assertEquals(trainTicket, currPlayer.getWallet().getTrain() + 1);
        assertNotEquals(createdGame.getCurrentPlayer(), currPlayer);

    }

    @Test
    public void playerIssuesMove_validInputDouble_playerMoved(){

        Game createdGame = gameService.initializeGame(testLobby1);
        gameService.hack(createdGame.getGameId());
        Player currPlayer = createdGame.getCurrentPlayer();
        currPlayer.setCurrentStation(stationRepository.findByStationId(308L));
        User currUser = currPlayer.getUser();

        Move double308to204 = new Move();
        double308to204.setTicket(Ticket.DOUBLE);
        double308to204.setFrom(stationRepository.findByStationId(308L));
        double308to204.setTo(stationRepository.findByStationId(204L));

        final int busTicket = currPlayer.getWallet().getDouble();

        gameService.playerIssuesMove(currUser, double308to204, createdGame.getGameId());

        assertEquals(currPlayer.getCurrentStation(), double308to204.getTo());
        assertEquals(busTicket, currPlayer.getWallet().getDouble() + 1);
        assertNotEquals(createdGame.getCurrentPlayer(), currPlayer);

    }

    @Test
    public void playerIssuesMove_validInputBlack_playerMoved(){

        Game createdGame = gameService.initializeGame(testLobby1);
        gameService.hack(createdGame.getGameId());
        Player currPlayer = createdGame.getCurrentPlayer();
        User currUser = currPlayer.getUser();

        Move black232to183 = new Move();
        black232to183.setTicket(Ticket.BLACK);
        black232to183.setFrom(stationRepository.findByStationId(232L));
        black232to183.setTo(stationRepository.findByStationId(183L));

        final int busTicket = currPlayer.getWallet().getBlack();

        gameService.playerIssuesMove(currUser, black232to183, createdGame.getGameId());

        assertEquals(currPlayer.getCurrentStation(), black232to183.getTo());
        assertEquals(busTicket, currPlayer.getWallet().getBlack() + 1);
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
        for (Player player : createdGame.getPlayerGroup()){
            assertEquals(1,player.getUser().getGamesPlayed());
            if (player.getPlayerClass()==PlayerClass.MRX){
                assertEquals(0,player.getUser().getGamesWon());
            } else{
                assertEquals(1,player.getUser().getGamesWon());
            }
        }

    }

    @Test
    public void playerIssuesMove_validInput_gameTerminated_MrXCaught(){

        Game createdGame = gameService.initializeGame(testLobby1);
        gameService.hack(createdGame.getGameId());
        createdGame.getCurrentPlayer().setCurrentStation(stationRepository.findByStationId(183L));


        gameService.playerIssuesMove(createdGame.getCurrentPlayer().getUser(), tram183to232, createdGame.getGameId());
        gameService.playerIssuesMove(createdGame.getCurrentPlayer().getUser(), tram194to232, createdGame.getGameId());

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

    @Test
    public void getPlayerPositions_validInput_playersReceived(){
        Game createdGame = gameService.initializeGame(testLobby1);
        gameService.hack(createdGame.getGameId());

        User mrXUser = new User();
        User detectiveUser = new User();

        for (Player player : createdGame.getPlayerGroup()){
            if (player.getPlayerClass() == PlayerClass.MRX){
                mrXUser = player.getUser();
            }
            if (player.getPlayerClass() == PlayerClass.DETECTIVE){
                detectiveUser = player.getUser();
            }
        }

        List<Player> playerPositionMrX = gameService.getPlayerPositions(createdGame.getGameId(), mrXUser);
        List<Player> playerPositionDetective = gameService.getPlayerPositions(createdGame.getGameId(), detectiveUser);

        for (Player player: createdGame.getPlayerGroup()){
            assertTrue(playerPositionMrX.contains(player));
        }
        assertTrue(Objects.isNull(playerPositionDetective.get(0).getCurrentStation()));
    }

    @Test
    public void gameOver_validInput_mrXEvades(){
        Game createdGame = gameService.initializeGame(testLobby1);
        gameService.hack(createdGame.getGameId());
        createdGame.getCurrentRound().setRoundNumber(22);

        gameService.playerIssuesMove(createdGame.getCurrentPlayer().getUser(),
                tram232to183, createdGame.getGameId());
        gameService.playerIssuesMove(createdGame.getCurrentPlayer().getUser(),
                tram194to232, createdGame.getGameId());
        gameService.playerIssuesMove(createdGame.getCurrentPlayer().getUser(),
                tram16to242, createdGame.getGameId());

        assertTrue(createdGame.isGameOver());
        for (Player player : createdGame.getPlayerGroup()){
            assertEquals(1,player.getUser().getGamesPlayed());
            if (player.getPlayerClass()==PlayerClass.MRX){
                assertEquals(1,player.getUser().getGamesWon());
            } else{
                assertEquals(0,player.getUser().getGamesWon());
            }
        }
        assertEquals(PlayerClass.MRX ,createdGame.getGameSummary().getWinner());
        assertEquals(22, createdGame.getGameSummary().getRoundsPlayed());

    }
}
