package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.UserPutDTO;
import ch.uzh.ifi.hase.soprafs21.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.istack.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * UserControllerTest
 * This is a WebMvcTest which allows to test the UserController i.e. GET/POST request without actually sending them over the network.
 * This tests if the UserController works.
 */
@WebMvcTest(UserController.class)
public class UserControllerTest {

    private String baseTestErrorMessage = "Test error Message";
    private User testUser1;
    private User testUser2;
    private User testUser3;
    private List<User> testList = new ArrayList<>();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);

        // all givens for all tests
        testUser1 = new User();
        testUser2 = new User();
        testUser3 = new User();

        testUser1.setUsername("testUsername1");
        testUser1.setPassword("testPassword1");
        testUser1.setId(1L);
        testUser1.setDob("2000-01-01");
        testUser1.setToken(UUID.randomUUID().toString());

        testUser2.setUsername("testUsername2");
        testUser2.setPassword("testPassword2");
        testUser2.setId(2L);
        testUser2.setDob("2000-01-01");
        testUser2.setToken(UUID.randomUUID().toString());

        testUser3.setUsername("testUsername3");
        testUser3.setPassword("testPassword3");
        testUser3.setId(3L);
        testUser3.setDob("2000-01-01");
        testUser3.setToken(UUID.randomUUID().toString());

        testList.add(testUser1);
        testList.add(testUser2);
        testList.add(testUser3);
    }



    @Test // works: tests if return get(/users) returns a jason file with users
    public void givenUsers_whenGetUsers_thenReturnJsonArray() throws Exception {

        // this mocks the UserService -> we define above what the userService should return when getUsers() is called
        given(userService.getUsers()).willReturn(testList);

        // when
        MockHttpServletRequestBuilder getRequest = get("/users").contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username", is(testUser1.getUsername())))
                .andExpect(jsonPath("$[0].dob", is(testUser1.getDob())))
                .andExpect(jsonPath("$[1].username", is(testUser2.getUsername())))
                .andExpect(jsonPath("$[1].dob", is(testUser2.getDob())))
                .andExpect(jsonPath("$[2].username", is(testUser3.getUsername())))
                .andExpect(jsonPath("$[2].dob", is(testUser3.getDob())));
    }

    @Test
    public void createUser_validInput_userCreated() throws Exception {

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setUsername(testUser1.getUsername());
        userPostDTO.setPassword(testUser1.getPassword());
        userPostDTO.setDob(testUser1.getDob());



        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isCreated());
    }

    @Test
    public void createUser_invalidInput_userNotCreated() throws Exception {

        Mockito.when(userService.createUser(Mockito.any())).thenThrow(new ResponseStatusException(HttpStatus.CONFLICT, baseTestErrorMessage));

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setUsername(testUser2.getUsername());
        userPostDTO.setPassword(testUser2.getPassword());
        userPostDTO.setDob(testUser2.getDob());


        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isConflict());

    }


    @Test
    public void loginUser_valid() throws Exception {
        //given
        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setUsername(testUser2.getUsername());
        userPostDTO.setPassword(testUser2.getPassword());
        userPostDTO.setDob(testUser2.getDob());


        Mockito.when(userService.loginUser(testUser2)).thenReturn(testUser2);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest2 = put("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));

        mockMvc.perform(postRequest2)
                .andExpect(status().isAccepted());
    }

    @Test
    public void loginUser_invalid() throws Exception {

        Mockito.when(userService.loginUser(Mockito.any())).thenThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, baseTestErrorMessage));

        //given
        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setUsername(testUser3.getUsername());
        userPostDTO.setPassword("testPasswordWrong");
        userPostDTO.setDob(testUser3.getDob());


        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest2 = put("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));

        mockMvc.perform(postRequest2)
                .andExpect(status().isUnauthorized());
    }


    @Test
    public void getUserWithId_valid() throws Exception {

        UserGetDTO userGetDTO = new UserGetDTO();
        userGetDTO.setUsername(testUser3.getUsername());

        Mockito.when(userService.getUserDataById(testUser3.getId()))
                .thenReturn(testUser3);

        MockHttpServletRequestBuilder getRequest = get("/users/3")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userGetDTO));


        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is(testUser3.getUsername())));
    }

    @Test
    public void getUserWithId_invalid() throws Exception {
        UserGetDTO userGetDTO = new UserGetDTO();
        userGetDTO.setUsername(testUser3.getUsername());

        given(userService.getUserDataById((long) 200))
                .willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, baseTestErrorMessage));

        MockHttpServletRequestBuilder getRequest = get("/users/200")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userGetDTO));


        mockMvc.perform(getRequest)
                .andExpect(status().isNotFound());
    }


    @Test
    public void updateUser_valid() throws Exception {

        UserPutDTO userPutDTO = new UserPutDTO();
        userPutDTO.setUsername("newUsername");
        userPutDTO.setDob("1995-07-14");

        doNothing().when(userService).updateUser(Mockito.any(), anyLong());

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder putRequest = put("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPutDTO));;

        mockMvc.perform(putRequest)
                .andExpect(status().isNoContent());
    }

    @Test
    public void updateUser_invalid_IdNotFound() throws Exception {

        UserPutDTO userPutDTO = new UserPutDTO();
        userPutDTO.setUsername("newUsername");
        userPutDTO.setDob("1995-07-14");
        long id = 5;

        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, baseTestErrorMessage))
                .when(userService)
                .updateUser(Mockito.any(), anyLong());

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder putRequest = put("/users/5")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPutDTO));;

        mockMvc.perform(putRequest)
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateUser_invalid_TokenNotCorrect() throws Exception {

        UserPutDTO userPutDTO = new UserPutDTO();
        userPutDTO.setUsername("newUsername");
        userPutDTO.setDob("1995-07-14");
        userPutDTO.setToken(testUser2.getToken());

        long id = 1;

        doThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, baseTestErrorMessage))
                .when(userService)
                .updateUser(Mockito.any(), anyLong());

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder putRequest = put("/users/5")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPutDTO));;

        mockMvc.perform(putRequest)
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void updateUser_invalid_UsernameTaken() throws Exception {

        UserPutDTO userPutDTO = new UserPutDTO();
        userPutDTO.setUsername(testUser3.getUsername());
        userPutDTO.setDob("1995-07-14");
        userPutDTO.setToken(testUser2.getToken());

        long id = 2;

        doThrow(new ResponseStatusException(HttpStatus.CONFLICT, baseTestErrorMessage))
                .when(userService)
                .updateUser(Mockito.any(), anyLong());

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder putRequest = put("/users/5")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPutDTO));;

        mockMvc.perform(putRequest)
                .andExpect(status().isConflict());
    }

    /**
     * Helper Method to convert userPostDTO into a JSON string such that the input can be processed
     * Input will look like this: {"name": "Test User", "username": "testUsername"}
     * @param object
     * @return string
     */
    private String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        }
        catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("The request body could not be created.%s", e.toString()));
        }
    }
}