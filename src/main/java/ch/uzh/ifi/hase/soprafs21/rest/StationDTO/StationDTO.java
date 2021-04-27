package ch.uzh.ifi.hase.soprafs21.rest.StationDTO;


import java.util.List;

public class StationDTO {
    private Long id;
    private String name;
    private List<Long> reachable_by_bus;
    private List<Long> reachable_by_tram;
    private float stop_lat;
    private float stop_lon;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public List<Long> getReachable_by_bus() {
        return reachable_by_bus;
    }
    public void setReachable_by_bus(List<Long> reachable_by_bus) {
        this.reachable_by_bus = reachable_by_bus;
    }

    public List<Long> getReachable_by_tram() {
        return reachable_by_tram;
    }
    public void setReachable_by_tram(List<Long> reachable_by_tram) {
        this.reachable_by_tram = reachable_by_tram;
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
}
