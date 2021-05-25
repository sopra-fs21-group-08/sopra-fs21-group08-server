package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entity.Chat;
import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.entity.Message;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.rest.ChatDTO.ReceivedMessageDTO;
import ch.uzh.ifi.hase.soprafs21.service.GameService;
import ch.uzh.ifi.hase.soprafs21.service.LobbyService;
import ch.uzh.ifi.hase.soprafs21.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.UUID;

import static ch.uzh.ifi.hase.soprafs21.TestHelpers.TestHelpers.asJsonString;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ChatController.class)
public class ChatControllerTest {

    // given
    private User testUser1;
    private Lobby testLobby;
    private Chat testChat;
    private Message testMessage;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LobbyService lobbyService;
    @MockBean
    private UserService userService;
    @MockBean
    private GameService gameService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testChat = new Chat();
        testChat.setChatId(1L);

        testLobby = new Lobby();
        testLobby.setLobbyId(1L);
        testLobby.setLobbyName("ROOM1");
        testLobby.setChat(testChat);

        testUser1 = new User();
        testUser1.setUsername("testUsername1");
        testUser1.setPassword("testPassword1");
        testUser1.setUserId(1L);
        testUser1.setDob("2000-01-01");
        testUser1.setStatus(UserStatus.ONLINE);
        testUser1.setToken(UUID.randomUUID().toString());

        testMessage = new Message();
        testMessage.setMessageId(1L);
        testMessage.setUsername(testUser1.getUsername());
        testMessage.setMessage("test");

        testChat.addMessage(testMessage);
    }

    @AfterEach
    void tearDown() {
        testLobby = null;
        testUser1 = null;
        testChat = null;
        testMessage = null;
    }

    @Test
    public void getMessage_validInput_messagesReceived() throws Exception{
        Long gameId = 1L;

        when(this.lobbyService.findLobbyById(gameId)).thenReturn(testLobby);

        MockHttpServletRequestBuilder getRequest = get("/games/{gameID}/chats", gameId)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username", is(testUser1.getUsername())))
                .andExpect(jsonPath("$[0].message", is("test")));
    }

    @Test
    public void writeMessage_validInput_messagePosted() throws Exception{
        Long gameId = 1L;

        ReceivedMessageDTO receivedMessageDTO = new ReceivedMessageDTO();
        receivedMessageDTO.setToken(testUser1.getToken());
        receivedMessageDTO.setUserId(testUser1.getUserId());
        receivedMessageDTO.setMessage(testMessage.getMessage());

        when(userService.findUserByEntity(Mockito.any())).thenReturn(testUser1);

        MockHttpServletRequestBuilder postRequest = post("/games/{gameID}/chats", gameId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(receivedMessageDTO));

        mockMvc.perform(postRequest)
                .andExpect(status().isOk());
    }
}
