package ch.uzh.ifi.hase.soprafs21.GameEntities;


import ch.uzh.ifi.hase.soprafs21.constant.PlayerClass;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


// this class might need to be created upon request and not stored in database


@Entity
@Table(name = "GAMESUMMARY")
public class GameSummary {

    @Id
    private Long summaryId;

    public PlayerClass winner;

    public int roundsPlayed;

    public void setSummaryId(Long summaryId) {
        this.summaryId = summaryId;
    }
    public Long getSummaryId() {
        return summaryId;
    }

    public PlayerClass getWinner() {
        return winner;
    }
    public void setWinner(PlayerClass winners) {
        this.winner = winners;
    }

    public int getRoundsPlayed() {
        return roundsPlayed;
    }
    public void setRoundsPlayed(int roundsPlayed) {
        this.roundsPlayed = roundsPlayed;
    }
}
