package ch.uzh.ifi.hase.soprafs21.rest.mapper;

import ch.uzh.ifi.hase.soprafs21.network.Station;
import ch.uzh.ifi.hase.soprafs21.rest.StationDTO.StationDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface StationDTOMapper {

    StationDTOMapper INSTANCE = Mappers.getMapper(StationDTOMapper.class);

    @Mapping(source = "id", target = "stationId")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "stop_lat", target = "stop_lat")
    @Mapping(source = "stop_lon", target = "stop_lon")
    Station convertStationDTOtoEntity(StationDTO stationDTO);

    @Mapping(source = "stationId", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "_reachable_by_bus", target = "reachable_by_bus",
            qualifiedByName = "stationListToLongList")
    @Mapping(source = "_reachable_by_tram", target = "reachable_by_tram",
            qualifiedByName = "stationListToLongList")
    @Mapping(source = "stop_lat", target = "stop_lat")
    @Mapping(source = "stop_lon", target = "stop_lon")
    StationDTO convertEntitytoStationDTO(Station station);

    @Named("stationListToLongList")
    static List<Long> stationListToLongList(List<Station> stationList){
        List<Long> longList = new ArrayList<Long>();
        for (Station station : stationList){
            longList.add(station.getStationId());
        }
        return longList;
    }
}
