package ch.uzh.ifi.hase.soprafs21.rest.mapper;

import ch.uzh.ifi.hase.soprafs21.GameEntities.Players.Player;
import ch.uzh.ifi.hase.soprafs21.network.Station;
import ch.uzh.ifi.hase.soprafs21.rest.PlayerDTO.PlayerGetDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PlayerDTOMapper {
    PlayerDTOMapper INSTANCE = Mappers.getMapper(PlayerDTOMapper.class);
    @Mapping(source = "user", target = "user")
    @Mapping(source = "playerClass", target = "playerClass")
    @Mapping(source = "currentStation.stationId", target = "stationId")
    PlayerGetDTO convertPlayerToGetDTO(Player player);



}

