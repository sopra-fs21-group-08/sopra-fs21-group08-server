package ch.uzh.ifi.hase.soprafs21.rest.mapper;


import ch.uzh.ifi.hase.soprafs21.GameEntities.Game;
import ch.uzh.ifi.hase.soprafs21.GameEntities.Players.Player;
import ch.uzh.ifi.hase.soprafs21.GameEntities.TicketWallet.TicketWallet;
import ch.uzh.ifi.hase.soprafs21.rest.GameStatusDTO.GameStatusGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.PlayerDTO.PlayerGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.WalletDTO.WalletGetDTO;
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



    @Mapping(source = "user", target = "user")
    @Mapping(source = "playerClass", target = "playerClass")
    @Mapping(source = "currentStation.stationId", target = "stationId")
    PlayerGetDTO convertPlayerToGetDTO(Player player);

    @Mapping(expression = "java(s.getTrain())", target = "TRAIN")
    @Mapping(expression = "java(s.getTram())", target = "TRAM")
    @Mapping(expression = "java(s.getBus())", target = "BUS")
    @Mapping(expression = "java(s.getBlack())", target = "BLACK")
    @Mapping(expression = "java(s.getDouble())", target = "DOUBLE")
    WalletGetDTO convertEntitiyToDTO(TicketWallet s);
}
