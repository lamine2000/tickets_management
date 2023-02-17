package sn.trivial.ticket.service.mapper;

import org.mapstruct.*;
import sn.trivial.ticket.domain.Client;
import sn.trivial.ticket.domain.User;
import sn.trivial.ticket.service.dto.ClientDTO;
import sn.trivial.ticket.service.dto.UserDTO;

/**
 * Mapper for the entity {@link Client} and its DTO {@link ClientDTO}.
 */
@Mapper(componentModel = "spring")
public interface ClientMapper extends EntityMapper<ClientDTO, Client> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    ClientDTO toDto(Client s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);
}
