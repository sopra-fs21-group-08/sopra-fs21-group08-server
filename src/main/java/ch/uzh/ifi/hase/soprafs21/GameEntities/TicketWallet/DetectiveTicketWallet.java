package ch.uzh.ifi.hase.soprafs21.GameEntities.TicketWallet;

import ch.uzh.ifi.hase.soprafs21.rest.WalletDTO.WalletGetDTO;

public class DetectiveTicketWallet implements TicketWallet{
    private BusTicket busTicket;
    private TramTicket tramTicket;
    private TrainTicket trainTicket;
    public DetectiveTicketWallet(){
        this.busTicket = new BusTicket();
        this.tramTicket = new TramTicket();
        this.trainTicket = new TrainTicket();

    }
    public boolean useTicket(Ticket ticket){
        Ticket foundTicket = getTicketStack(ticket);
        return foundTicket.useTicket();
    }
    public boolean isTicketAvaiable(Ticket ticket){
        Ticket foundTicket = getTicketStack(ticket);
        return foundTicket.isTicketAvaiable();
    }
    private Ticket getTicketStack(Ticket ticket){
        if(ticket instanceof BusTicket){
            return this.busTicket;
        }
        else if(ticket instanceof TrainTicket){
            return this.trainTicket;

        }else if(ticket instanceof TramTicket){
            return this.tramTicket;

        }else{
            return null;
        }
    }

    public WalletGetDTO convertToWalletDTO(){
        WalletGetDTO walletGetDTO = new WalletGetDTO();
        walletGetDTO.setTrain(this.trainTicket.getTicketCount());
        walletGetDTO.setTram(this.tramTicket.getTicketCount());
        walletGetDTO.setBus(this.busTicket.getTicketCount());
        return walletGetDTO;
    }
}