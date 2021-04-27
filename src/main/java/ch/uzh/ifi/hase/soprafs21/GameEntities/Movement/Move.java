package ch.uzh.ifi.hase.soprafs21.GameEntities.Movement;


import ch.uzh.ifi.hase.soprafs21.GameEntities.Players.Player;
import ch.uzh.ifi.hase.soprafs21.network.Station;

import javax.persistence.*;

@Entity
@Table(name = "MOVE")
public class Move {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long moveId;

    @ManyToOne
    @JoinColumn(name = "roundId")
    private Round round;

    @OneToOne
    @JoinColumn(name = "playerId")
    private Player player;

    @ManyToOne
    @JoinColumn(name = "to_stationId")
    private Station to;

    /*
    private Ticket ticket;
    */

    public Long getMoveId() {
        return moveId;
    }
    public void setMoveId(Long moveId) {
        this.moveId = moveId;
    }

    public Round getRound() {
        return round;
    }
    public void setRound(Round round) {
        this.round = round;
    }

    public Player getPlayer() {
        return player;
    }
    public void setPlayer(Player player) {
        this.player = player;
    }

    public Station getFrom() {
        return player.getCurrentStation();
    }

    public Station getTo() {
        return to;
    }
    public void setTo(Station to) {
        this.to = to;
    }

    public void executeMove(){
        this.player.setCurrentStation(this.to);
    }
}
