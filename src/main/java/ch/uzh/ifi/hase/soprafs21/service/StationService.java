package ch.uzh.ifi.hase.soprafs21.service;


import ch.uzh.ifi.hase.soprafs21.network.Station;
import ch.uzh.ifi.hase.soprafs21.repository.StationRepository;
import ch.uzh.ifi.hase.soprafs21.rest.StationDTO.StationPostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.StationDTOMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
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
        TypeReference<List<StationPostDTO>> mapType = new TypeReference<List<StationPostDTO>>() {};
        InputStream is = TypeReference.class.getResourceAsStream("/json/station_info_new.json");
        try {
            List<StationPostDTO> stationDTOList = mapper.readValue(is, mapType);
            for (StationPostDTO stationDTO : stationDTOList){
                stationRepository.save(StationDTOMapper.INSTANCE.convertStationPostDTOtoEntity(stationDTO));
                stationRepository.flush();
            }
            for (StationPostDTO stationDTO : stationDTOList){
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
}
