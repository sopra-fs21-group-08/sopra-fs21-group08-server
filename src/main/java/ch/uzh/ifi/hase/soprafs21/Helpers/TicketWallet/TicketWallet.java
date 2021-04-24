package ch.uzh.ifi.hase.soprafs21.Helpers.TicketWallet;
public interface TicketWallet{
    public boolean useTicket(Ticket ticket);
    public boolean isTicketAvaiable(Ticket ticket);
}