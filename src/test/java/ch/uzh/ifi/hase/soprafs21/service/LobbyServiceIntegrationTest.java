package ch.uzh.ifi.hase.soprafs21.service;


import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.repository.LobbyConnectorRepository;
import ch.uzh.ifi.hase.soprafs21.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.jupiter.api.Assertions.*;

import javax.transaction.Transactional;

@Transactional
@WebAppConfiguration
@SpringBootTest
public class LobbyServiceIntegrationTest {

    // given
    private Lobby testLobby1;
    private User testUser1;
    private User testUser2;

    @Qualifier("lobbyRepository")
    @Autowired
    private LobbyRepository lobbyRepository;

    @Qualifier("lobbyConnectorRepository")
    @Autowired
    private LobbyConnectorRepository lobbyConnectorRepository;

    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LobbyService lobbyService;

    @Autowired
    private UserService userService;

    @BeforeEach
    public void setUp(){
        testLobby1 = new Lobby();
        testLobby1.setLobbyId(1L);

        testUser1 = new User();
        testUser1.setUsername("testUser1");
        testUser1.setPassword("password1");
        testUser1 = userService.createUser(testUser1);

        testUser2 = new User();
        testUser2.setUsername("testUser2");
        testUser2.setPassword("password2");
        testUser2 = userService.createUser(testUser2);
    }

    @AfterEach
    public void tearDown(){
        lobbyRepository.deleteAll();
        lobbyConnectorRepository.deleteAll();
        userRepository.deleteAll();

        testLobby1 = null;
        testUser1 = null;
        testUser2 = null;
    }

    @Test
    public void removeAllUsers_validInput_lobbyDeleted(){

        Long testLobbyId = testLobby1.getLobbyId();

        testLobby1 = lobbyService.createLobby(testLobby1, testUser1);
        testLobby1 = lobbyService.joinLobby(testUser2, testLobby1.getLobbyId());
        testLobby1 = lobbyService.findLobbyById(testLobby1.getLobbyId());

        assertTrue(testLobby1.getUsers().contains(testUser1));
        assertTrue(testLobby1.getUsers().contains(testUser2));

        lobbyService.leaveLobby(testUser2, testLobby1.getLobbyId());

        testLobby1 = lobbyService.findLobbyById(testLobby1.getLobbyId());

        assertFalse(testLobby1.getUsers().contains(testUser2));
        assertTrue(testLobby1.getUsers().contains(testUser1));

        lobbyService.leaveLobby(testUser1, testLobby1.getLobbyId());

        assertNull(lobbyService.findLobbyById(testLobbyId));

    }

    /*
    @Test
    public void usersPlayAgain_validInput_newLobbyCreated(){

        Long testLobbyId = testLobby1.getLobbyId();

        testLobby1 = lobbyService.createLobby(testLobby1, testUser1);
        testLobby1 = lobbyService.joinLobby(testUser2, testLobby1.getLobbyId());
        testLobby1 = lobbyService.findLobbyById(testLobby1.getLobbyId());

        assertTrue(testLobby1.getUsers().contains(testUser1));
        assertTrue(testLobby1.getUsers().contains(testUser2));

        Lobby testNextLobby1 = lobbyService.playAgain(testLobby1.getLobbyId(), testUser2);

        testLobby1 = lobbyService.findLobbyById(testLobby1.getLobbyId());
        testNextLobby1 = lobbyService.findLobbyById(testNextLobby1.getLobbyId());


        assertFalse(testLobby1.getUsers().contains(testUser2));
        assertTrue(testNextLobby1.getUsers().contains(testUser2));
        assertTrue(lobbyConnectorRepository.existsByLastLobbyId(testLobby1.getLobbyId()));
        assertEquals(lobbyConnectorRepository.findByLastLobbyId(testLobby1.getLobbyId()).getNext(),
                testNextLobby1);

        testNextLobby1 = lobbyService.playAgain(testLobby1.getLobbyId(), testUser1);

        assertNull(lobbyService.findLobbyById(testLobbyId));
        assertNull(lobbyConnectorRepository.findByLastLobbyId(testLobbyId));
        assertTrue(testNextLobby1.getUsers().contains(testUser1));
    }
    */
}
