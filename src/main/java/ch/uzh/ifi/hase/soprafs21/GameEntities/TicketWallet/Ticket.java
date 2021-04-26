package ch.uzh.ifi.hase.soprafs21.GameEntities.TicketWallet;

public abstract class Ticket{

    private int ticketCount;

    protected Ticket(int ticketCount){
        this.ticketCount = ticketCount;
    }
    public boolean useTicket(){
        if(ticketCount>0){
            this.ticketCount--;
            return true;
        }else{
            return false;
        }
    }

    public boolean isTicketAvaiable(){
        return this.ticketCount>0;
    }
    public int getTicketCount(){
        return this.ticketCount;
    }
}