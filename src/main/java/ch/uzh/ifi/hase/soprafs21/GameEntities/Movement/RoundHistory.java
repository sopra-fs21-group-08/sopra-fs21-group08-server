package ch.uzh.ifi.hase.soprafs21.GameEntities.Movement;

import ch.uzh.ifi.hase.soprafs21.GameEntities.Game;
import ch.uzh.ifi.hase.soprafs21.network.Station;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ROUNDHISTORY")
public class RoundHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roundHistoryId;

    @OneToOne(mappedBy = "roundHistory")
    private Game game;

    @OneToMany()
    private List<Round> pastRounds = new ArrayList<>();

    public void addRound(Round round){
        this.pastRounds.add(round);
    }

    public Station getLastVisibleStation(){
        for(int i = pastRounds.size(); i==0; i--){
            Round round = pastRounds.get(i);
            if(round.isMrXVisible()){
                 return round.getMrXStation();
            }

        }
        return null;
    }

    public int size(){
        return pastRounds.size();
    }

    protected List<Round> getRounds() {
        return this.pastRounds;
    }
}
