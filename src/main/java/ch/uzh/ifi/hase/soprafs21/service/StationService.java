package ch.uzh.ifi.hase.soprafs21.service;


import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.network.Network;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

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
        InputStream is = TypeReference.class.getResourceAsStream("/json/station_info_new.json");
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
                stationRepository.flush();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    public List<Station> getStations() {
        return this.stationRepository.findAll();
    }

    public StationDTO convertEntityToDTO(Station station){
        StationDTO stationDTO = StationDTOMapper.INSTANCE.convertEntitytoStationDTO(station);
        stationDTO.setReachable_by_bus(station.get_reachable_by_bus());
        stationDTO.setReachable_by_tram(station.get_reachable_by_tram());
        return stationDTO;
    }

    public Network getNetwork(){
        Network network = new Network();
        network.setStationList(this.getStations());
        return network;
    }

    public Station getRandomStation(){
        Random rand = new Random();
        List<Station> stationList = this.getStations();
        return stationList.get(rand.nextInt(stationList.size()));
    }
}
