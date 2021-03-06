package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.rest.UserDTO.UserGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.UserDTO.UserPostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.UserDTO.UserPutDTO;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.UserDTOMapper;
import ch.uzh.ifi.hase.soprafs21.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * User Controller
 * This class is responsible for handling all REST request that are related to the user.
 * The controller will receive the request and delegate the execution to the UserService and finally return the result.
 */
@RestController
public class UserController {

    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    // get all users
    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<UserGetDTO> getAllUsers() {
        // fetch all users in the internal representation
        List<User> users = userService.getUsers();
        List<UserGetDTO> userGetDTOs = new ArrayList<>();

        // convert each user to the API representation
        for (User user : users) {
            userGetDTOs.add(UserDTOMapper.INSTANCE.convertEntityToUserGetDTO(user));
        }
        return userGetDTOs;
    }

    //create new user
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public UserPutDTO createUser(@RequestBody UserPostDTO userPostDTO) {
        // convert API user to internal representation
        User userInput = UserDTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

        // create user
        User createdUser = userService.createUser(userInput);

        // convert internal representation of user back to API
        return UserDTOMapper.INSTANCE.convertEntityToUserPutDTO(createdUser);
    }

    @PutMapping("/login")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @ResponseBody
    public UserPutDTO loginUser(@RequestBody UserPostDTO userPostDTO) {
        // convert API user to internal representation
        User userInput = UserDTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

        // find user
        User loginUser = userService.loginUser(userInput);

        // convert internal representation of user back to API
        return UserDTOMapper.INSTANCE.convertEntityToUserPutDTO(loginUser);
    }

    @GetMapping("users/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserGetDTO getUser(@PathVariable("id") long id) {

        // fetch user in the internal representation
        User user = userService.getUserById(id);
        return UserDTOMapper.INSTANCE.convertEntityToUserGetDTO(user);
    }

    @PutMapping("users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void putUser(@RequestBody UserPutDTO userIdPutDTO, @PathVariable("id") long id, @RequestHeader("Authorization") String token) {
        User user = UserDTOMapper.INSTANCE.convertUserPutDTOtoEntity(userIdPutDTO);
        user.setToken(token);
        userService.updateUser(user, id);
    }

    @PutMapping("/users/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public UserGetDTO logoutUser(@RequestBody UserPutDTO userPutDTO) {
        User logoutUser = UserDTOMapper.INSTANCE.convertUserPutDTOtoEntity(userPutDTO);

        // find user and set status to offline
        User offlineUser = userService.logoutUser(logoutUser);

        // convert internal representation of user back to API
        return UserDTOMapper.INSTANCE.convertEntityToUserGetDTO(offlineUser);
    }
}
