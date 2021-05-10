package ch.uzh.ifi.hase.soprafs21.service;


import ch.uzh.ifi.hase.soprafs21.network.Station;
import ch.uzh.ifi.hase.soprafs21.repository.StationRepository;
import ch.uzh.ifi.hase.soprafs21.rest.StationDTO.StationDTO;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.StationDTOMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StationService {
    private final Logger log = LoggerFactory.getLogger(StationService.class);

    private final StationRepository stationRepository;

    @Autowired
    public StationService(@Qualifier("stationRepository") StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public void initialiseStationsFromJSON(){
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<List<StationDTO>> mapType = new TypeReference<List<StationDTO>>() {};
        InputStream is = TypeReference.class.getResourceAsStream("/json/station_dict.json");
        try {
            List<StationDTO> stationDTOList = mapper.readValue(is, mapType);
            for (StationDTO stationDTO : stationDTOList){
                stationRepository.save(StationDTOMapper.INSTANCE.convertStationDTOtoEntity(stationDTO));
                stationRepository.flush();
            }
            for (StationDTO stationDTO : stationDTOList){
                Optional<Station> optStation = stationRepository.findById(stationDTO.getId());
                if (!optStation.isPresent()){
                    throw new IllegalArgumentException("Station wasn't found.");
                }
                Station station = optStation.get();
                for (Long stationIdBus : stationDTO.getReachable_by_bus()){
                    Optional<Station> optStationBus = stationRepository.findById(stationIdBus);
                    if (!optStationBus.isPresent()){
                        throw new IllegalArgumentException("Station wasn't found.");
                    }
                    station.appendBusStation(optStationBus.get());
                }
                for (Long stationIdTram : stationDTO.getReachable_by_tram()){
                    Optional<Station> optStationTram = stationRepository.findById(stationIdTram);
                    if (!optStationTram.isPresent()){
                        throw new IllegalArgumentException("Station wasn't found.");
                    }
                    station.appendTramStation(optStationTram.get());
                }
                for (Long stationIdTrain : stationDTO.getReachable_by_train()){
                    Optional<Station> optStationTrain = stationRepository.findById(stationIdTrain);
                    if (!optStationTrain.isPresent()){
                        throw new IllegalArgumentException("Station wasn't found.");
                    }
                    station.appendTrainStation(optStationTrain.get());
                }
                stationRepository.flush();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    public void refineStations(){
        while (trimBranches());
    }

    private boolean trimBranches(){
        List<Station> allStations = this.stationRepository.findAll();
        for (Station station : allStations){
            Long adjacentStationId;
            Station adjacentStation;
            if (getTotalNumberOfAdjacentStations(station) == 1){
                if (station.get_reachable_by_tram().size() == 1){
                    adjacentStationId = station.get_reachable_by_tram().get(0);
                    adjacentStation = stationRepository.findByStationId(adjacentStationId);
                    adjacentStation.removeTramStation(station);
                    stationRepository.delete(station);
                    stationRepository.flush();
                    return true;

                }
                else if (station.get_reachable_by_bus().size() == 1){
                    adjacentStationId = station.get_reachable_by_bus().get(0);
                    adjacentStation = stationRepository.findByStationId(adjacentStationId);
                    adjacentStation.removeBusStation(station);
                    stationRepository.delete(station);
                    stationRepository.flush();
                    return true;
                }
                else{
                    throw new IllegalStateException("Something went terribly wrong");
                }
            }
            if (getTotalNumberOfAdjacentStations(station) == 2 && HasSameBusTramStations(station)){
                adjacentStationId = station.get_reachable_by_tram().get(0);
                adjacentStation = stationRepository.findByStationId(adjacentStationId);
                adjacentStation.removeTramStation(station);
                adjacentStation.removeBusStation(station);
                stationRepository.delete(station);
                stationRepository.flush();
                return true;
            }
        }
        return false;
    }

    private int getTotalNumberOfAdjacentStations(Station station){
        return station.get_reachable_by_bus().size()
                + station.get_reachable_by_tram().size()
                + station.get_reachable_by_train().size();
    }

    private boolean HasSameBusTramStations(Station station){
        return station.get_reachable_by_tram().equals(station.get_reachable_by_bus());
    }

    public List<Station> getStations() {
        return this.stationRepository.findAll();
    }

    public Station getStationById(long to) {
        return stationRepository.findByStationId(to);
    }
}
