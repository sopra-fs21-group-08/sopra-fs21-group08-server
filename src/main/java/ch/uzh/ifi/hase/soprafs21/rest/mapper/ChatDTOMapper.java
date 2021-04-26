package ch.uzh.ifi.hase.soprafs21.rest.mapper;

import ch.uzh.ifi.hase.soprafs21.entity.Message;
import ch.uzh.ifi.hase.soprafs21.rest.ChatDTO.MessageGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.ChatDTO.ReceivedMessageDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ChatDTOMapper{

    ChatDTOMapper INSTANCE = Mappers.getMapper(ChatDTOMapper.class);

    @Mapping(source = "username", target = "username")
    @Mapping(source = "message", target = "message")
    @Mapping(source = "time", target = "time")
    MessageGetDTO covertEntityToMessageDTO(Message msg);


    @Mapping(source = "message", target = "message")
    Message convertReceivedMessageDTOtoMessage(ReceivedMessageDTO msgDTO);


}
