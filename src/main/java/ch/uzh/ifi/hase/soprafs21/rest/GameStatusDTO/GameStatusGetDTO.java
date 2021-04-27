package ch.uzh.ifi.hase.soprafs21.rest.GameStatusDTO;

import ch.uzh.ifi.hase.soprafs21.rest.UserDTO.UserGetDTO;

public class GameStatusGetDTO {
    private UserGetDTO currentUser;
    private boolean isGameOver;
    private boolean isMrXVisible;
    private int currentRound;

    public UserGetDTO getCurrentUser() {
        return currentUser;
    }
    public void setCurrentUser(UserGetDTO currentUser) {
        this.currentUser = currentUser;
    }

    public boolean isGameOver() {
        return isGameOver;
    }
    public void setGameOver(boolean gameOver) {
        isGameOver = gameOver;
    }

    public boolean isMrXVisible() {
        return isMrXVisible;
    }
    public void setMrXVisible(boolean mrXVisible) {
        isMrXVisible = mrXVisible;
    }

    public int getCurrentRound() {
        return currentRound;
    }
    public void setCurrentRound(int currentRound) {
        this.currentRound = currentRound;
    }
}
