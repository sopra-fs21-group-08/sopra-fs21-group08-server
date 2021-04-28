package ch.uzh.ifi.hase.soprafs21.rest.mapper;


import ch.uzh.ifi.hase.soprafs21.GameEntities.Game;
import ch.uzh.ifi.hase.soprafs21.rest.GameStatusDTO.GameStatusGetDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface GameDTOMapper {

    GameDTOMapper INSTANCE = Mappers.getMapper(GameDTOMapper.class);

    //TODO: from currentPlayer everything gets mapped correctly except the stationId which is a mystery.
    // since it gets mapped properly when using the playerDTOMapper itself.
    @Mapping(source = "gameId", target = "gameId")
    @Mapping(source = "playerGroup.currentPlayer", target = "currentPlayer")
    @Mapping(source = "gameOver", target = "gameOver")
    @Mapping(source = "currentRound.mrXVisible", target = "mrXVisible")
    @Mapping(source = "currentRound.roundNumber", target = "currentRound")
    GameStatusGetDTO convertEntityToGameStatusGetDTO(Game game);




}
