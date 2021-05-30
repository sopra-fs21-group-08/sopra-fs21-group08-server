package ch.uzh.ifi.hase.soprafs21.rest.mapper;


import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.rest.LobbyDTO.GetAllLobbiesDTO;
import ch.uzh.ifi.hase.soprafs21.rest.LobbyDTO.LobbyGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.LobbyDTO.LobbyPostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.UserDTO.UserGetDTO;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LobbyDTOMapper {
    LobbyDTOMapper INSTANCE = Mappers.getMapper(LobbyDTOMapper.class);

    @Mapping(source = "lobbyName", target = "lobbyName")
    Lobby covertLobbyPostDTOToEntity(LobbyPostDTO lobbyPostDTO);


    @Mapping(source = "lobbyId", target = "lobbyId")
    @Mapping(source = "lobbyName", target = "lobbyName")
    @Mapping(expression = "java(lobby.getSize())", target = "amountOfUsers")
    @Mapping(expression = "java(lobby.didGameStart())", target = "gameStarted")
    GetAllLobbiesDTO convertEntityToGetAllDTO(Lobby lobby);


    @Mapping(source = "lobbyId", target = "lobbyId")
    @Mapping(source = "lobbyName", target = "lobbyName")
    @Mapping(expression = "java(lobby.didGameStart())", target = "gameStarted")
    LobbyGetDTO convertEntityToLobbyGetDTO(Lobby lobby);




}
