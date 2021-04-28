package ch.uzh.ifi.hase.soprafs21.rest.MoveDTO;

import ch.uzh.ifi.hase.soprafs21.GameEntities.TicketWallet.Ticket;

public class MoveDTO {

    private Ticket ticket;
    private long to;


    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public long getTo() {
        return to;
    }

    public void setTo(long to) {
        this.to = to;
    }
}
