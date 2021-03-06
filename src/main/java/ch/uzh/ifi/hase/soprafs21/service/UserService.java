package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * User Service
 * This class is the "worker" and responsible for all functionality related to the user
 * (e.g., it creates, modifies, deletes, finds). The result will be passed back to the caller.
 */
@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    @Autowired
    public UserService(@Qualifier("userRepository") UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getUsers() {
        return this.userRepository.findAll();
    }

    public User createUser(User newUser) {
        newUser.setToken(UUID.randomUUID().toString());
        newUser.setStatus(UserStatus.ONLINE);
        newUser.setCreationDate(java.time.LocalDate.now().toString());

        isUsernameAvailable(newUser);

        // saves the given entity but data is only persisted in the database once flush() is called
        newUser = userRepository.save(newUser);
        userRepository.flush();

        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    public User loginUser(User inputUser) {

        //find user
        User foundUser = findUserByUsername(inputUser);
        if(foundUser == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Wrong credentials.");
        }

        authenticatePassword(inputUser, foundUser);
        foundUser.setStatus(UserStatus.ONLINE);
        log.debug("User Logged in: {}", foundUser);
        return foundUser;
    }

    private User findUserByUsername(User inputUser) {
        String username = inputUser.getUsername();
        return userRepository.findByUsername(username);
    }


    public User getUserById(long id){
        User foundUser = this.userRepository.findByUserId(id);
        String baseErrorMessage = "The user doesn't exits.";
        if (foundUser == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, baseErrorMessage);
        }
        return foundUser;
    }

    public User findUserByEntity(User inputUser) {
        Long userId = inputUser.getUserId();
        // find user in repository
        return this.userRepository.findByUserId(userId);
    }

    public User findUserByToken(String token) {
        User foundUser = userRepository.findByToken(token);
        if (Objects.isNull(foundUser)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "You don't have permission to get this information!");
        }
        return foundUser;
    }

    private User checkIfUserExists(User userToBeCreated) {
        User userByUsername = userRepository.findByUsername(userToBeCreated.getUsername());

        String baseErrorMessage = "The user doesn't exits. Therefore, we couldn't log you in!";
        if (userByUsername == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, baseErrorMessage);
        }
        return userByUsername;
    }

    /**
     * This is a helper method that will check the uniqueness criteria of the username and the name
     * defined in the User entity. The method will do nothing if the input is unique and throw an error otherwise.
     *
     * @param userToBeCreated
     * @throws org.springframework.web.server.ResponseStatusException
     * @see User
     */
    private void isUsernameAvailable(User userToBeCreated) {

        //find user with the same username
        User userByUsername = this.userRepository.findByUsername(userToBeCreated.getUsername());

        String baseErrorMessage = "The username provided is not unique. Therefore, the user could not be created!";

        //if this user already exists then username is taken --> throw error message
        if (userByUsername != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, baseErrorMessage);
        }
    }


    public void updateUser(User changeUser, long id) {

        User foundUser = this.userRepository.findByUserId(id);

        String baseErrorMessage = "User couldn't be found";
        if (foundUser == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, baseErrorMessage);
        }
        String inputUserToken = changeUser.getToken();
        String foundUserToken = foundUser.getToken();
        baseErrorMessage = "Not Authorized to change this User";
        if (!inputUserToken.equals(foundUserToken)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, baseErrorMessage);
        }

        String newName = changeUser.getUsername();
        User usernameConflictUser = this.userRepository.findByUsername(newName);
        baseErrorMessage = "Username already exists";
        if (usernameConflictUser != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, baseErrorMessage);
        }
        if (changeUser.getDob()!=null&& !changeUser.getDob().equals("")){
            foundUser.setDob(changeUser.getDob());}

        if (changeUser.getUsername()!=null && !changeUser.getUsername().equals("")){
            foundUser.setUsername(changeUser.getUsername());}
        log.debug("User profile updated: {}", changeUser);
    }

    public void setUserLobby(User user, Lobby lobby){
        user.setCurrentLobby(lobby);
    }

    public void leaveCurrentLobby(User user){
        user.removeCurrentLobby();
    }

    public User logoutUser(User inputUser) {

        User foundUser = findUserByEntity(inputUser);

        authenticateToken(inputUser, foundUser);
        /*
        String baseErrorMessage = "No User to log out";
        if (foundUser == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, baseErrorMessage);
        }*/

        foundUser.setStatus(UserStatus.OFFLINE);
        log.debug("User Logged out: {}", inputUser);
        return foundUser;
    }

    /**
     * this method will take two arguments
     * @param subject
     * @return
     */
    private void authenticatePassword(User subject, User target) {

        // checks if passwords match
        String inputUserPassword = subject.getPassword();
        String foundUserPassword = target.getPassword();

        String baseErrorMessage = "Incorrect PASSWORD, try again";

        if (!inputUserPassword.equals(foundUserPassword)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, baseErrorMessage);
        }
    }

    public void authenticateToken(User subject, User target) {

        // checks if Tokens match
        String inputUserToken = subject.getToken();
        String foundUserToken = target.getToken();

        String baseErrorMessage = "Incorrect TOKEN, try again";

        if (!inputUserToken.equals(foundUserToken)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, baseErrorMessage);
        }
    }




}
