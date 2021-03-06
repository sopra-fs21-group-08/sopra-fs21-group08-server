package ch.uzh.ifi.hase.soprafs21.controller;


import ch.uzh.ifi.hase.soprafs21.network.Station;
import ch.uzh.ifi.hase.soprafs21.rest.StationDTO.StationDTO;

import ch.uzh.ifi.hase.soprafs21.rest.mapper.StationDTOMapper;
import ch.uzh.ifi.hase.soprafs21.service.StationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@RestController
public class StationController {

    private final StationService stationService;

    StationController(StationService stationService) {
        this.stationService = stationService;
    }

    // get all stations
    @GetMapping("/stations")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<StationDTO> getAllStations() {
        // fetch all users in the internal representation
        List<Station> stations = stationService.getStations();
        List<StationDTO> stationDTOs = new ArrayList<>();

        // convert each user to the API representation
        for (Station station : stations) {
            stationDTOs.add(StationDTOMapper.INSTANCE.convertEntityToStationDTO(station));
        }
        return stationDTOs;
    }

    @PostConstruct
    public void postAllStations() {
        stationService.initialiseStationsFromJSON();
        stationService.refineStations();
        //stationService.resetIndices();
    }

}
