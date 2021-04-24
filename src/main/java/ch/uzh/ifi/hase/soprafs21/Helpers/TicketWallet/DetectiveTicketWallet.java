package ch.uzh.ifi.hase.soprafs21.Helpers.TicketWallet;

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
}