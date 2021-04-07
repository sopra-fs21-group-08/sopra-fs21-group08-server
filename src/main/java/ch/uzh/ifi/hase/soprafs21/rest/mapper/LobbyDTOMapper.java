package ch.uzh.ifi.hase.soprafs21.rest.mapper;


import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.rest.LobbyDTO.LobbyGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.LobbyDTO.LobbyPostDTO;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LobbyDTOMapper {
    LobbyDTOMapper INSTANCE = Mappers.getMapper(LobbyDTOMapper.class);

    @Mapping(source = "id", target = "lobbyId")
    @Mapping(source = "lobbyname", target = "lobbyname")
    Lobby covertLobbyPostDToLobby(LobbyPostDTO lobbyPostDTO);

    @Mapping(source = "lobbyId", target = "id")
    LobbyGetDTO convertLobbyToGetDTO(Lobby lobby);



}
