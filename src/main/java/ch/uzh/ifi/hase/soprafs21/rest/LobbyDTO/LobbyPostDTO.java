package ch.uzh.ifi.hase.soprafs21.rest.LobbyDTO;

public class LobbyPostDTO {

    private String lobbyName;
    public long userID;


    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public String getLobbyName() {
        return lobbyName;
    }
    public void setLobbyName(String lobbyName) {
        this.lobbyName = lobbyName;
    }



}
