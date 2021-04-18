package ch.uzh.ifi.hase.soprafs21.rest.mapper;

import ch.uzh.ifi.hase.soprafs21.network.Station;
import ch.uzh.ifi.hase.soprafs21.rest.StationDTO.StationDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface StationDTOMapper {

    StationDTOMapper INSTANCE = Mappers.getMapper(StationDTOMapper.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "stop_lat", target = "stop_lat")
    @Mapping(source = "stop_lon", target = "stop_lon")
    Station convertStationDTOtoEntity(StationDTO stationDTO);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "stop_lat", target = "stop_lat")
    @Mapping(source = "stop_lon", target = "stop_lon")
    StationDTO convertEntitytoStationDTO(Station station);
}
