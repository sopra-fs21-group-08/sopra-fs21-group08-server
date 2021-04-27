package ch.uzh.ifi.hase.soprafs21.rest.PlayerDTO;

public class PlayerGetPositionDTO {

    private Long playerId;
    private Long locationId;

    public Long getPlayerId() {
        return playerId;
    }
    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }


    public Long getLocationId() {
        return locationId;
    }
    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }
}
