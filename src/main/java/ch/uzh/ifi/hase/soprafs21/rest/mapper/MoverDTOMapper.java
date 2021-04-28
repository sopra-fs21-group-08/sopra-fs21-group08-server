/*package ch.uzh.ifi.hase.soprafs21.rest.mapper;


import ch.uzh.ifi.hase.soprafs21.GameEntities.Movement.Move;
import ch.uzh.ifi.hase.soprafs21.rest.MoveDTO.MoveDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MoverDTOMapper {
    MoverDTOMapper INSTANCE = Mappers.getMapper(MoverDTOMapper.class);

    @Mapping(source = "ticket", target = "ticket")
    Move convertDTOtoMove(MoveDTO moveDTO);
}
*/