package ch.uzh.ifi.hase.soprafs21.rest.TicketDTO;


import ch.uzh.ifi.hase.soprafs21.GameEntities.TicketWallet.Ticket;

public class TicketDTO {
    private Ticket ticket;

    public Ticket getTicket() {
        return ticket;
    }
    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }
}
