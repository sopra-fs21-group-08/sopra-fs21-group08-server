package ch.uzh.ifi.hase.soprafs21.GameEntities.Movement;

import ch.uzh.ifi.hase.soprafs21.GameEntities.TicketWallet.Ticket;

import java.util.Iterator;
import java.util.List;

public class BlackboardIterator implements Iterator {

    private int i;
    private final List<Ticket> blackboard;

    public BlackboardIterator(List<Ticket> tickets) {
        this.i = 0;
        this.blackboard = tickets;
    }

    @Override
    public boolean hasNext() {
        return(i < blackboard.size());
    }

    @Override
    public Ticket next() {
        Ticket ticket = blackboard.get(i);
        i++;
        return ticket;
    }

    @Override
    public void remove() {

    }
}
