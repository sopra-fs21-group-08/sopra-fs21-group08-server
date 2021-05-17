package ch.uzh.ifi.hase.soprafs21.GameEntities;


import ch.uzh.ifi.hase.soprafs21.constant.PlayerClass;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;


// this class might need to be created upon request and not stored in database


@Entity
@Table(name = "GAMESUMMARY")
public class GameSummary {


    @Id
    private Long summaryId;

    public PlayerClass winners;

    @Transient
    public List<List<Integer>> moves = new ArrayList<>();

    public void setSummaryId(Long summaryId) {
        this.summaryId = summaryId;
    }
    public Long getSummaryId() {
        return summaryId;
    }

    public void addLocation(Integer integer, Integer pos){
        this.moves.get(pos).add(integer);
    }
}
