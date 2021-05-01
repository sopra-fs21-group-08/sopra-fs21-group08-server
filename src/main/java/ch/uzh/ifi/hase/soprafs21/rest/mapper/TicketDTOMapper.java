/*package ch.uzh.ifi.hase.soprafs21.rest.mapper;

import ch.uzh.ifi.hase.soprafs21.GameEntities.TicketWallet.Ticket;

import ch.uzh.ifi.hase.soprafs21.rest.TicketDTO.TicketDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TicketDTOMapper {
    TicketDTOMapper INSTANCE = Mappers.getMapper(TicketDTOMapper.class);

    @Mapping(expression = "ticket.values()", target = "ticket")
    TicketDTO convertTicketToDTO(Ticket ticket);



}*/
