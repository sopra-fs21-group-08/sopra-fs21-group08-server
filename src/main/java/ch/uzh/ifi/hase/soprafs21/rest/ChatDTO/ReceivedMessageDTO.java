package ch.uzh.ifi.hase.soprafs21.rest.ChatDTO;

public class ReceivedMessageDTO {



    private Long userId;
    private String message;
    private String token;


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }



}
