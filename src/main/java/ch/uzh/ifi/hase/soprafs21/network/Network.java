package ch.uzh.ifi.hase.soprafs21.network;

import java.util.List;
import java.util.Random;


public class Network {

    private List<Station> stationList;


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
