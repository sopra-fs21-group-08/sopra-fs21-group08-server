package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.GameEntities.Game;
import ch.uzh.ifi.hase.soprafs21.GameEntities.Movement.Blackboard;
import ch.uzh.ifi.hase.soprafs21.GameEntities.Movement.Round;
import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs21.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.concurrent.BlockingDeque;

public class GameServiceTest {
/*
    // given
    private Lobby testLobby1;
    private User testUser1;
    private User testUser2;
    private User testUser3;
    private Game testGame;

    @Mock
    private GameRepository gameRepository;

    @InjectMocks
    private GameService gameService;


    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;

    @Qualifier("lobbyRepository")
    @Autowired
    private LobbyRepository lobbyRepository;


    @Autowired
    private LobbyService lobbyService;

    @Autowired
    private UserService userService;

    @BeforeEach
    public void setUp(){
        testLobby1 = new Lobby();
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

        testLobby1 = lobbyService.createLobby(testLobby1, testUser1);
        lobbyService.joinLobby(testUser2, testLobby1.getLobbyId());
        lobbyService.joinLobby(testUser3, testLobby1.getLobbyId());

        testGame = new Game();
        testGame.setLobby(testLobby1);
        testGame.setCurrentRound(new Round());
        testGame.setBlackboard(new Blackboard());
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
        testGame = null;
    }

    @Test
    public void initializeGame_validInput_gameCreated(){

        //gameService.initializeGame(testLobby1);


    }

 */
}

