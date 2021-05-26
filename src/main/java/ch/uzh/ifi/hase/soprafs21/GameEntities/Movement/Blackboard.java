package ch.uzh.ifi.hase.soprafs21.GameEntities.Movement;


import ch.uzh.ifi.hase.soprafs21.GameEntities.Game;
import ch.uzh.ifi.hase.soprafs21.GameEntities.TicketWallet.Ticket;

import javax.persistence.*;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "BLACKBOARD")
public class Blackboard implements Iterable<Ticket>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long blackboardId;

    @ElementCollection
    public List<Ticket> blackboard = new ArrayList<>();

    public void addTicket(Ticket ticket){
        this.blackboard.add(ticket);
    }


    @OneToOne(mappedBy = "myBlackboard")
    private Game game;

    public List<Ticket> getTickets() {
        return blackboard;
    }

    @Override
    public BlackboardIterator iterator() {
        return new BlackboardIterator(this.getTickets());
    }

    public Game getGame() {
        return game;
    }
    public void setGame(Game game) {
        this.game = game;
    }
}
