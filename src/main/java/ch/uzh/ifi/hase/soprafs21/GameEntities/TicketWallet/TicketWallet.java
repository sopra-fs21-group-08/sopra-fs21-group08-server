package ch.uzh.ifi.hase.soprafs21.GameEntities.TicketWallet;

import ch.uzh.ifi.hase.soprafs21.rest.WalletDTO.WalletGetDTO;

public interface TicketWallet{
    public boolean useTicket(Ticket ticket);
    public boolean isTicketAvaiable(Ticket ticket);
    public WalletGetDTO convertToWalletDTO();
}