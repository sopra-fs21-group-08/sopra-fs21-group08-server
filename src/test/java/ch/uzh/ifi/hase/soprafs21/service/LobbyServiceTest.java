package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.repository.LobbyRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class LobbyServiceTest {

    // given
    private User testUser1;
    private List<User> testUserList;
    private Lobby testLobby1;

    @Mock
    private LobbyRepository lobbyRepository;

    @InjectMocks
    private LobbyService lobbyService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        testUser1 = new User();

        testUserList = new ArrayList<>();
        testUserList.add(testUser1);

        testLobby1 = new Lobby();
        testLobby1.setLobbyId(1L);
        testLobby1.setLobbyName("LOBBY1");

    }

    @AfterEach
    public void tearDown() {

        testUser1 = null;
        testUserList = null;
        testLobby1 = null;
    }

    @Test
    public void createLobbyTest_validInput_lobbyCreated() throws Exception{

        when(lobbyRepository.save(Mockito.any())).thenReturn(testLobby1);

        Lobby createdLobby = lobbyService.createLobby(testLobby1, testUser1);

        verify(lobbyRepository, Mockito.times(1)).save(Mockito.any());

        assertEquals(createdLobby.getLobbyName(), testLobby1.getLobbyName());
        assertEquals(createdLobby.getSize(), 1);
        assertEquals(createdLobby.getUsers(), testUserList);
        assertEquals(createdLobby.didGameStart(), false);
        assertEquals(createdLobby.getChat().getLobby(), createdLobby);
        assertEquals(createdLobby.getGame(), null);
        assertEquals(createdLobby.isEmpty(), false);
        assertNotNull(createdLobby.getChat());

    }

    @Test
    public void joinLobbyTest_validInput_lobbyJoined() throws Exception{

        when(this.lobbyRepository.findByLobbyId(testLobby1.getLobbyId())).thenReturn(testLobby1);

        Lobby joinedLobby = lobbyService.joinLobby(testUser1, testLobby1.getLobbyId());

        assertEquals(joinedLobby.getUsers(), testUserList);

    }
}
