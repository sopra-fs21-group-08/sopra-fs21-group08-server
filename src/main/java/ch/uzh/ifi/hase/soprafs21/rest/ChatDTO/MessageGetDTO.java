package ch.uzh.ifi.hase.soprafs21.rest.ChatDTO;

public class MessageGetDTO {

    public String username;
    public String message;
    public String time;

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }



}

