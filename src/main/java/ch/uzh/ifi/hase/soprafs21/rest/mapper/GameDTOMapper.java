package ch.uzh.ifi.hase.soprafs21.rest.mapper;


import ch.uzh.ifi.hase.soprafs21.GameEntities.Game;
import ch.uzh.ifi.hase.soprafs21.GameEntities.Players.Player;
import ch.uzh.ifi.hase.soprafs21.rest.GameStatusDTO.GameStatusGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.UserDTO.UserGetDTO;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

public interface GameDTOMapper {


    //TODO: get current User who is in
    @Mapping(source = "currentUser", target = "currentUser",
            qualifiedByName = "currentUserFunctionCall")
    GameStatusGetDTO convertEntityToGameStatusGetDTO(Game game);

    @Named("stationListToLongList")
    private static Player getCurrentUser(Game game){
        return game.getCurrentPlayer();
    }
}
