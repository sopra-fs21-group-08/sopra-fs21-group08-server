package ch.uzh.ifi.hase.soprafs21.rest.mapper;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.rest.UserDTO.UserGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.UserDTO.UserPostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.UserDTO.UserPutDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * DTOMapperTest
 * Tests if the mapping between the internal and the external/API representation works.
 */
public class UserDTOMapperTest {
    @Test
    public void testCreateUser_fromUserPostDTO_toUser_success() {
        // create UserPostDTO
        UserPostDTO userPostDTO = new UserPostDTO();

        userPostDTO.setUsername("username");
        userPostDTO.setPassword("password");
        userPostDTO.setDob("2000-01-01");


        // MAP -> Create user
        User user = UserDTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

        // check content
        assertEquals(userPostDTO.getPassword(), user.getPassword());
        assertEquals(userPostDTO.getUsername(), user.getUsername());
        assertEquals(userPostDTO.getDob(), user.getDob());
    }

    @Test
    public void testCreateUser_fromUserPostDTO_toUser_fail() {
        // create UserPostDTO
        UserPostDTO userPostDTO = new UserPostDTO();

        userPostDTO.setUsername("username");
        userPostDTO.setPassword("password");
        userPostDTO.setDob("2000-01-01");


        // MAP -> Create user
        User user = UserDTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);
        user.setUsername("un");
        user.setPassword("pw");
        user.setDob("2000-01-02");

        // check content
        assertNotEquals(userPostDTO.getPassword(), user.getPassword());
        assertNotEquals(userPostDTO.getUsername(), user.getUsername());
        assertNotEquals(userPostDTO.getDob(), user.getDob());
    }

    @Test
    public void testGetUser_fromUser_toUserGetDTO_success() {
        // create User
        User user = new User();
        user.setUserId((long) 1);
        user.setUsername("un");
        user.setStatus(UserStatus.OFFLINE);
        user.setDob("2000-01-02");
        user.setCreationDate("2000-01-02");
        user.setPassword("pw");


        // MAP -> Create UserGetDTO
        UserGetDTO userGetDTO = UserDTOMapper.INSTANCE.convertEntityToUserGetDTO(user);

        // check content
        assertEquals(user.getUserId(), userGetDTO.getId());
        assertEquals(user.getUsername(), userGetDTO.getUsername());
        assertEquals(user.getStatus(), userGetDTO.getStatus());
        assertEquals(user.getDob(), userGetDTO.getDob());
        assertEquals(user.getCreationDate(), userGetDTO.getCreationDate());

    }

    @Test
    public void testGetUser_fromUser_toUserGetDTO_fail() {
        // create User
        User user = new User();
        user.setUserId((long) 1);
        user.setUsername("un");
        user.setStatus(UserStatus.OFFLINE);
        user.setDob("2000-01-02");
        user.setCreationDate("2000-01-02");
        user.setPassword("pw");


        // MAP -> Create UserGetDTO
        UserGetDTO userGetDTO = UserDTOMapper.INSTANCE.convertEntityToUserGetDTO(user);

        userGetDTO.setId((long) 2);
        userGetDTO.setUsername("un1");
        userGetDTO.setStatus(UserStatus.ONLINE);
        userGetDTO.setDob("2000-01-03");
        userGetDTO.setCreationDate("2000-01-03");

        // check content
        assertNotEquals(user.getUserId(), userGetDTO.getId());
        assertNotEquals(user.getUsername(), userGetDTO.getUsername());
        assertNotEquals(user.getStatus(), userGetDTO.getStatus());
        assertNotEquals(user.getDob(), userGetDTO.getDob());
        assertNotEquals(user.getCreationDate(), userGetDTO.getCreationDate());

    }

    @Test
    public void testConvert_fromUser_toUserPutDTO(){
        User user = new User();

        user.setUserId((long) 1);
        user.setUsername("un");
        user.setToken("ölkj");
        user.setDob("2000-01-03");

        UserPutDTO userPutDTO = UserDTOMapper.INSTANCE.convertEntityToUserPutDTO(user);

        assertEquals(user.getUserId(), userPutDTO.getId());
        assertEquals(user.getUsername(), userPutDTO.getUsername());
        assertEquals(user.getToken(), userPutDTO.getToken());
        assertEquals(user.getDob(), userPutDTO.getDob());
    }
    @Test
    public void testConvert_fromPutDTO_toUser(){
        UserPutDTO userPutDTO = new UserPutDTO();

        userPutDTO.setId((long) 1);
        userPutDTO.setUsername("un");
        userPutDTO.setToken("ölkj");
        userPutDTO.setDob("2000-01-03");

        User user = UserDTOMapper.INSTANCE.convertUserPutDTOtoEntity(userPutDTO);

        assertEquals(user.getUserId(), userPutDTO.getId());
        assertEquals(user.getUsername(), userPutDTO.getUsername());
        assertEquals(user.getToken(), userPutDTO.getToken());
        assertEquals(user.getDob(), userPutDTO.getDob());
    }
}
