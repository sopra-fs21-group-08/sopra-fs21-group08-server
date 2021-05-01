package ch.uzh.ifi.hase.soprafs21.service;


import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.entity.Message;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.repository.LobbyRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

@WebAppConfiguration
@SpringBootTest
public class LobbyServiceIntegrationTest {

    // given
    private User testUser1;
    private Message testMessage1;
    private Lobby testLobby;

    @Qualifier("lobbyRepository")
    @Autowired
    private LobbyRepository lobbyRepository;

    @Autowired
    private LobbyService lobbyService;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);

        testUser1 = new User();
        testUser1.setUserId(1L);
        testUser1.setUsername("testUsername");

        testMessage1 = new Message();
        testMessage1.setMessageId(1L);
        testMessage1.setMessage("test");

        testLobby = new Lobby();
    }

    @AfterEach
    public void tearDown(){

        testUser1 = null;
        testMessage1 = null;
        testLobby = null;
    }

    @Test
    public void postMessage_validInput_messagePosted() throws Exception{

    }
}
