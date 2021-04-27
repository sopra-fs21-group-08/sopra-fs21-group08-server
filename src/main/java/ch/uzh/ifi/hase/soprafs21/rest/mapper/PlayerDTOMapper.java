package ch.uzh.ifi.hase.soprafs21.rest.mapper;

import ch.uzh.ifi.hase.soprafs21.GameEntities.Players.Player;
import ch.uzh.ifi.hase.soprafs21.rest.PlayerDTO.PlayerGetDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PlayerDTOMapper {
    PlayerDTOMapper INSTANCE = Mappers.getMapper(PlayerDTOMapper.class);

    @Mapping(source = "playerId", target = "playerId")
    @Mapping(source = "playerClass", target = "playerClass")
    PlayerGetDTO convertPlayerToGetDTO(Player player);

}

