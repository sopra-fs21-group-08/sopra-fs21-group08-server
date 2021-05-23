package ch.uzh.ifi.hase.soprafs21.rest.GameStatusDTO;

import ch.uzh.ifi.hase.soprafs21.constant.PlayerClass;

public class GameSummaryDTO {

    private Long summaryId;
    private PlayerClass winner;
    private int roundsPlayed;

    public Long getSummaryId() {
        return summaryId;
    }
    public void setSummaryId(Long summaryId) {
        this.summaryId = summaryId;
    }

    public PlayerClass getWinner() {
        return winner;
    }
    public void setWinner(PlayerClass winner) {
        this.winner = winner;
    }

    public int getRoundsPlayed() {
        return roundsPlayed;
    }
    public void setRoundsPlayed(int roundsPlayed) {
        this.roundsPlayed = roundsPlayed;
    }
}
