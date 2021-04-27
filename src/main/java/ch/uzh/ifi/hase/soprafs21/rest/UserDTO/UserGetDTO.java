package ch.uzh.ifi.hase.soprafs21.rest.UserDTO;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;

public class UserGetDTO {

    private Long userId;
    private String username;
    private UserStatus status;
    private String dob;
    private String creationDate;

    public String getCreationDate() {
        return creationDate;
    }
    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getDob() {
        return dob;
    }
    public void setDob(String dob) {
        this.dob = dob;
    }

    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public UserStatus getStatus() {
        return status;
    }
    public void setStatus(UserStatus status) {
        this.status = status;
    }
}
