package ch.uzh.ifi.hase.soprafs21.rest.LobbyDTO;

public class LobbyPostDTO {

    private String lobbyName;
    public long userId;


    public long getUserId() {
        return userId;
    }
    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getLobbyName() {
        return lobbyName;
    }
    public void setLobbyName(String lobbyName) {
        this.lobbyName = lobbyName;
    }



}
