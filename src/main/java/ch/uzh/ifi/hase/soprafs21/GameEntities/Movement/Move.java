package ch.uzh.ifi.hase.soprafs21.GameEntities.Movement;


import ch.uzh.ifi.hase.soprafs21.GameEntities.Players.Player;
import ch.uzh.ifi.hase.soprafs21.GameEntities.TicketWallet.Ticket;
import ch.uzh.ifi.hase.soprafs21.network.Station;

import javax.persistence.*;

@Entity
@Table(name = "MOVE")
public class Move {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long moveId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "roundId")
    private Round round;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "playerId")
    private Player player;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "From_stationId")
    private Station from;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "to_stationId")
    private Station to;

    private Ticket ticket;

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
        return this.from;
    }
    public void setFrom(Station from) {
        this.from = from;
    }

    public Station getTo() {
        return to;
    }
    public void setTo(Station to) {
        this.to = to;
    }

    public Ticket getTicket() {
        return ticket;
    }
    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public void executeMove(){
        this.player.setCurrentStation(this.to);
    }



}
