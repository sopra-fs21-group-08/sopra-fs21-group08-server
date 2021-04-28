package ch.uzh.ifi.hase.soprafs21.rest.GameStatusDTO;

import ch.uzh.ifi.hase.soprafs21.rest.PlayerDTO.PlayerGetDTO;

public class GameStatusGetDTO {
    private long gameId;
    private PlayerGetDTO currentPlayer;
    private boolean isGameOver;
    private boolean isMrXVisible;
    private int currentRound;

    public PlayerGetDTO getCurrentPlayer() {
        return currentPlayer;
    }
    public void setCurrentPlayer(PlayerGetDTO currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public boolean isGameOver() {
        return isGameOver;
    }
    public void setGameOver(boolean gameOver) {
        isGameOver = gameOver;
    }

    public int getCurrentRound() {
        return currentRound;
    }
    public void setCurrentRound(int currentRound) {
        this.currentRound = currentRound;
    }

    public long getGameId() {
        return gameId;
    }
    public void setGameId(long gameId) {
        this.gameId = gameId;
    }

    public boolean isMrXVisible() {
        return isMrXVisible;
    }
    public void setMrXVisible(boolean mrXVisible) {
        isMrXVisible = mrXVisible;
    }
}
