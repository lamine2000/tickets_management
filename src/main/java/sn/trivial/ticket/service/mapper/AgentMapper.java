package sn.trivial.ticket.service.mapper;

import org.mapstruct.*;
import sn.trivial.ticket.domain.Agent;
import sn.trivial.ticket.domain.User;
import sn.trivial.ticket.service.dto.AgentDTO;
import sn.trivial.ticket.service.dto.UserDTO;

/**
 * Mapper for the entity {@link Agent} and its DTO {@link AgentDTO}.
 */
@Mapper(componentModel = "spring")
public interface AgentMapper extends EntityMapper<AgentDTO, Agent> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    AgentDTO toDto(Agent s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);
}
