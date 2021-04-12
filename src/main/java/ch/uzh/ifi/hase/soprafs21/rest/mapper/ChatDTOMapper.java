package ch.uzh.ifi.hase.soprafs21.rest.mapper;

import ch.uzh.ifi.hase.soprafs21.controller.ChatController;
import ch.uzh.ifi.hase.soprafs21.entity.Chat;
import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.rest.ChatDTO.ChatGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.LobbyDTO.LobbyPostDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ChatDTOMapper{

    ChatDTOMapper INSTANCE = Mappers.getMapper(ChatDTOMapper.class);

    @Mapping(source = "messages", target = "messages")
    ChatGetDTO covertEntityToChatGetDTO(Chat chat);
}
