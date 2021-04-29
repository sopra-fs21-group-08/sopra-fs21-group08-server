package ch.uzh.ifi.hase.soprafs21.rest.TicketDTO;


import ch.uzh.ifi.hase.soprafs21.GameEntities.TicketWallet.Ticket;

public class TicketDTO {
    private Ticket ticketType;

    public Ticket getTicketType() {
        return ticketType;
    }

    public void setTicketType(Ticket ticketType) {
        this.ticketType = ticketType;
    }
}
