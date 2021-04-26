package ch.uzh.ifi.hase.soprafs21.rest.GameStatusDTO;

public class GameStatusGetDTO {
    private long currentUser;
    private boolean isGameOver;
    private boolean isMrXVisible;
    private int currentRound;

    public long getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(long currentUser) {
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
