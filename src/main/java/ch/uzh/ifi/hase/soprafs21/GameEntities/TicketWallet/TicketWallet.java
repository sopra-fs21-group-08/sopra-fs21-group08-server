package ch.uzh.ifi.hase.soprafs21.GameEntities.TicketWallet;

import javax.persistence.*;
import java.util.EnumMap;
import java.util.Map;



@Entity
@Table(name = "TICKETWALLET")
public class TicketWallet{


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ElementCollection
    @CollectionTable(name="TICKETS")
    @MapKeyEnumerated(EnumType.STRING)
    @MapKeyColumn(name="TICKET_TYPE")
    @Column(name="AMOUNT")
    private Map<Ticket, Integer> ticketMap = new EnumMap<Ticket, Integer>(Ticket.class);

    public void createDetectiveWallet(){
        this.ticketMap.put(Ticket.TRAM, 10);
        this.ticketMap.put(Ticket.BUS, 10);
        this.ticketMap.put(Ticket.TRAIN, 10);
        this.ticketMap.put(Ticket.DOUBLE, 0);
        this.ticketMap.put(Ticket.BLACK, 0);
    }
    
    public void createMrXWallet(){
        this.createDetectiveWallet();
        this.ticketMap.put(Ticket.DOUBLE, 2);
        this.ticketMap.put(Ticket.BLACK, 2);
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

    public int getTrain(){
        return ticketMap.get(Ticket.TRAIN);
    }
    public int getTram(){
        return ticketMap.get(Ticket.TRAM);
    }
    public int getBus(){
        return ticketMap.get(Ticket.BUS);
    }
    public int getBlack(){
        return ticketMap.get(Ticket.BLACK);
    }
    public int getDouble(){
        return ticketMap.get(Ticket.DOUBLE);
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Long getId() {
        return id;
    }
}