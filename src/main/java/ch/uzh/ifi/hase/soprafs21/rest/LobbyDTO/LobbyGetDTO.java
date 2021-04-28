package ch.uzh.ifi.hase.soprafs21.rest.LobbyDTO;

import ch.uzh.ifi.hase.soprafs21.rest.UserDTO.UserGetDTO;

import java.util.List;

public class LobbyGetDTO {

    private long lobbyId;
    private String lobbyName;
    private boolean gameStarted;
    private List<UserGetDTO> users;

    public long getLobbyId() {
        return lobbyId;
    }
    public void setLobbyId(long lobbyId) {
        this.lobbyId = lobbyId;
    }

    public String getLobbyName() {
        return lobbyName;
    }
    public void setLobbyName(String lobbyName) {
        this.lobbyName = lobbyName;
    }

    public boolean isGameStarted() {
        return gameStarted;
    }
    public void setGameStarted(boolean gameStarted) {
        this.gameStarted = gameStarted;
    }

    public List<UserGetDTO> getUsers() {
        return users;
    }
    public void setUsers(List<UserGetDTO> users) {
        this.users = users;
    }
}
