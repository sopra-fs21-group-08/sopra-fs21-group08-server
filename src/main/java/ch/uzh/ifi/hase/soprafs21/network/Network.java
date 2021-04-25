package ch.uzh.ifi.hase.soprafs21.network;

import ch.uzh.ifi.hase.soprafs21.service.StationService;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Random;

@Entity
@Table(name= "NETWORK")
public class Network implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @OneToMany
    private List<Station> stationList;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Station> getStationList() {
        return stationList;
    }

    public void setStationList(List<Station> stationList) {
        this.stationList = stationList;
    }

    public Station getRandomStation(){
        Random rand = new Random();
        return this.stationList.get(rand.nextInt(this.stationList.size()));
    }
}
