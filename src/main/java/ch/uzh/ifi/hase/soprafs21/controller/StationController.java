package ch.uzh.ifi.hase.soprafs21.controller;


import ch.uzh.ifi.hase.soprafs21.rest.UserDTO.UserGetDTO;

import ch.uzh.ifi.hase.soprafs21.service.StationService;
import ch.uzh.ifi.hase.soprafs21.service.UserService;
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
    public List<UserGetDTO> getAllStations() {
        // fetch all users in the internal representation
        List<UserGetDTO> userGetDTOs = new ArrayList<>();

        return userGetDTOs;
    }

    @PostConstruct
    //@PostMapping("/stations")
    //@ResponseStatus(HttpStatus.NO_CONTENT)
    //@ResponseBody
    public void postAllStations() {
        stationService.initialiseStationsFromJSON();
    }


}
