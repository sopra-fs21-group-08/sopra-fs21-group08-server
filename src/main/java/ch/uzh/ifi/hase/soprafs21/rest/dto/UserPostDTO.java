package ch.uzh.ifi.hase.soprafs21.rest.dto;

public class UserPostDTO {

    private String password;
    private String username;
    private String dob;

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getDob() {
        return dob;
    }
    public void setDob(String dob) {
        this.dob = dob;
    }


}
