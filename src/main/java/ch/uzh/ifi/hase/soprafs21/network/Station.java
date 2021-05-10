package ch.uzh.ifi.hase.soprafs21.network;

import ch.uzh.ifi.hase.soprafs21.GameEntities.TicketWallet.Ticket;

import javax.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;



@Entity
@Table(name = "STATION")
public class Station implements Serializable{

    @Id
    private Long stationId;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToMany(fetch=FetchType.LAZY)
    private List<Station> stations_reachable_by_bus = new ArrayList<Station>();

    @ManyToMany(fetch=FetchType.LAZY)
    private List<Station> stations_reachable_by_tram = new ArrayList<Station>();

    @ManyToMany(fetch=FetchType.LAZY)
    private List<Station> stations_reachable_by_train = new ArrayList<Station>();

    @Column(nullable = false)
    private float stop_lat;

    @Column(nullable = false)
    private float stop_lon;

    public Long getStationId() {
        return stationId;
    }

    public void setStationId(Long id) {
        this.stationId = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getStop_lat() {
        return stop_lat;
    }

    public void setStop_lat(float stop_lat) {
        this.stop_lat = stop_lat;
    }

    public float getStop_lon() {
        return stop_lon;
    }

    public void setStop_lon(float stop_lon) {
        this.stop_lon = stop_lon;
    }

    public void appendBusStation(Station station){
        this.stations_reachable_by_bus.add(station);
    }

    public void appendTramStation(Station station){
        this.stations_reachable_by_tram.add(station);
    }

    public void appendTrainStation(Station station){
        this.stations_reachable_by_train.add(station);
    }

    public void removeBusStation(Station station){ this.stations_reachable_by_bus.remove(station); }

    public void removeTramStation(Station station){ this.stations_reachable_by_tram.remove(station); }


    public List<Long> get_reachable_by_bus(){
        List<Long> reachable_by_bus = new ArrayList<>();
        for (Station station : stations_reachable_by_bus){
            reachable_by_bus.add(station.getStationId());
        }
        return reachable_by_bus;
    }

    public List<Long> get_reachable_by_tram(){
        List<Long> reachable_by_tram = new ArrayList<>();
        for (Station station : stations_reachable_by_tram){
            reachable_by_tram.add(station.getStationId());
        }
        return reachable_by_tram;
    }

    public List<Long> get_reachable_by_train(){
        List<Long> reachable_by_train = new ArrayList<>();
        for (Station station : stations_reachable_by_train){
            reachable_by_train.add(station.getStationId());
        }
        return reachable_by_train;
    }

    public List<Long> get_reachable_by_ticket(Ticket ticket){
        if (ticket == Ticket.BUS){
            return get_reachable_by_bus();
        }
        else if (ticket == Ticket.TRAM){
            return get_reachable_by_tram();
        }
        else if (ticket == Ticket.TRAIN){
            return get_reachable_by_train();
        }
        else {
            throw new UnsupportedOperationException("No such ticket");
        }
    }
}
