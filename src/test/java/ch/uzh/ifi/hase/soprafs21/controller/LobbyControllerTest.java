package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs21.rest.LobbyDTO.LobbyGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.LobbyDTO.LobbyPostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.UserDTO.UserPutDTO;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.UserDTOMapper;
import ch.uzh.ifi.hase.soprafs21.service.LobbyService;
import ch.uzh.ifi.hase.soprafs21.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
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
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LobbyController.class)
class LobbyControllerTest {

    // given
    private Lobby testLobby;
    private User testUser1;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LobbyService lobbyService;
    @MockBean
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testLobby = new Lobby();
        testLobby.setLobbyId(1L);
        testLobby.setLobbyName("ROOM1");


        testUser1 = new User();
        testUser1.setUsername("testUsername1");
        testUser1.setPassword("testPassword1");
        testUser1.setUserId(1L);
        testUser1.setDob("2000-01-01");
        testUser1.setStatus(UserStatus.ONLINE);
        testUser1.setToken(UUID.randomUUID().toString());

    }

    @AfterEach
    void tearDown() {
        testLobby = null;
        testUser1 = null;
    }


    @Test
    public void createLobby_validInput_lobbyCreated() throws Exception{

        LobbyPostDTO lobbyPostDTO = new LobbyPostDTO();
        lobbyPostDTO.setLobbyName(testLobby.getLobbyName());
        lobbyPostDTO.setUserId(testUser1.getUserId());

        Lobby inputLobby = new Lobby();
        inputLobby.setLobbyName(lobbyPostDTO.getLobbyName());


        when(userService.getUserById(lobbyPostDTO.getUserId())).thenReturn(testUser1);
        when(lobbyService.createLobby(Mockito.any(), Mockito.any())).thenReturn(testLobby);

        MockHttpServletRequestBuilder postRequest = post("/lobbies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(lobbyPostDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.lobbyId", is(testLobby.getLobbyId().intValue())));
    }

    @Test
    public void joinLobby_validInput_lobbyJoined() throws Exception{

        UserPutDTO userPutDTO = UserDTOMapper.INSTANCE.convertEntityToUserPutDTO(testUser1);


        when(userService.getUserById(userPutDTO.getUserId())).thenReturn(testUser1);
        when(lobbyService.joinLobby(testUser1, testLobby.getLobbyId())).thenReturn(testLobby);

        MockHttpServletRequestBuilder putRequest = put("/lobbies/{lobbyId}", testLobby.getLobbyId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPutDTO));

        // then
        mockMvc.perform(putRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lobbyId", is(testLobby.getLobbyId().intValue())));

    }

    @Test
    public void getUsers_validInput_userReceived() throws Exception{

        List<User> userList = new ArrayList<>();
        userList.add(testUser1);
        testLobby.addUser(testUser1);
        testUser1.setCurrentLobby(testLobby);

        when(lobbyService.getUsers(testLobby.getLobbyId())).thenReturn(userList);
        when(lobbyService.findLobbyById(testLobby.getLobbyId())).thenReturn(testLobby);

        MockHttpServletRequestBuilder getRequest = get("/lobbies/{lobbyId}", testLobby.getLobbyId())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lobbyId", is(testLobby.getLobbyId().intValue())))
                .andExpect(jsonPath("$.lobbyName", is(testLobby.getLobbyName())))
                .andExpect(jsonPath("$.gameStarted", is(testLobby.didGameStart())))
                .andExpect(jsonPath("$.users[0].userId", is(testUser1.getUserId().intValue())));
    }

    @Test
    public void removeUser_validInput_userRemoved() throws Exception{

        UserPutDTO userPutDTO = UserDTOMapper.INSTANCE.convertEntityToUserPutDTO(testUser1);

        when(userService.getUserById(testUser1.getUserId())).thenReturn(testUser1);

        MockHttpServletRequestBuilder deleteRequest = delete("/lobbies/{lobbyId}", testLobby.getLobbyId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPutDTO));

        mockMvc.perform(deleteRequest)
                .andExpect(status().isNoContent());
    }

}