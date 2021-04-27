package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the UserResource REST resource.
 *
 * @see UserService
 */
@WebAppConfiguration
@SpringBootTest
public class UserServiceIntegrationTest {

    // given
    private User testUser1;
    private User testUser2;

    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        testUser1 = new User();
        testUser1.setPassword("testName");
        testUser1.setUsername("testUsername");

        testUser2 = new User();
        testUser2.setPassword("testName2");
        testUser2.setUsername("testUsername");
    }

    @AfterEach
    public void tearDown(){
        userRepository.deleteAll();
        testUser1 = null;
        testUser2 = null;
    }

    @Test
    public void createUser_validInputs_success() {
        // given
        assertNull(userRepository.findByUsername("testUsername"));

        // when
        User createdUser = userService.createUser(testUser1);

        // then
        assertEquals(testUser1.getPassword(), createdUser.getPassword());
        assertEquals(testUser1.getUsername(), createdUser.getUsername());
        assertNotNull(createdUser.getToken());
        assertEquals(UserStatus.ONLINE, createdUser.getStatus());
    }

    @Test
    public void createUser_duplicateUsername_throwsException() {
        assertNull(userRepository.findByUsername("testUsername"));

        User createdUser = userService.createUser(testUser1);

        // check that an error is thrown
        assertThrows(ResponseStatusException.class, () -> userService.createUser(testUser2));
    }
}
