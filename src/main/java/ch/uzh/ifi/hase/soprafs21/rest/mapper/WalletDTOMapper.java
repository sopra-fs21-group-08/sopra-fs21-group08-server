package ch.uzh.ifi.hase.soprafs21.rest.mapper;

import ch.uzh.ifi.hase.soprafs21.GameEntities.TicketWallet.*;
import ch.uzh.ifi.hase.soprafs21.rest.WalletDTO.WalletGetDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mapper
public interface WalletDTOMapper {

    WalletDTOMapper INSTANCE = Mappers.getMapper(WalletDTOMapper.class);

    @Mapping(expression = "java(s.getTrain())", target = "TRAIN")
    @Mapping(expression = "java(s.getTram())", target = "TRAM")
    @Mapping(expression = "java(s.getBus())", target = "BUS")
    @Mapping(expression = "java(s.getBlack())", target = "BLACK")
    @Mapping(expression = "java(s.getDouble())", target = "DOUBLE")
    WalletGetDTO convertEntitiyToDTO(TicketWallet s);

}
