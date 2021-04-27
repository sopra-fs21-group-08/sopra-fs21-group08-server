package ch.uzh.ifi.hase.soprafs21.GameEntities.TicketWallet;

import java.util.EnumMap;
import java.util.Map;

public class TicketWallet{
    Map<Ticket, Integer> ticketMap =new EnumMap<Ticket, Integer>(Ticket.class);
    public TicketWallet(boolean isMrX){
        if(isMrX){
            this.createMrXWallet();
        }else{
            this.createDetectiveWallet();
        }


    }
   
    private void createDetectiveWallet(){
        this.ticketMap.put(Ticket.TRAM, 10);
        this.ticketMap.put(Ticket.BUS, 10);
        this.ticketMap.put(Ticket.TRAIN, 10);
        this.ticketMap.put(Ticket.DOUBLETICKET, 0);
        this.ticketMap.put(Ticket.BLACKTICKET, 0);
    }
    
    private void createMrXWallet(){
        this.createDetectiveWallet();
        this.ticketMap.put(Ticket.DOUBLETICKET, 2);
        this.ticketMap.put(Ticket.BLACKTICKET, 2);
    }
    

    public boolean isTicketAvaiable(Ticket ticket){
        return this.ticketMap.get(ticket)>0;
    }

    public Ticket useTicket(Ticket ticket){
            if(this.isTicketAvaiable(ticket)){
                int amount = this.ticketMap.get(ticket);
                amount--;
                this.ticketMap.put(ticket, amount);
                return ticket;
            }
            return null;
    }


}