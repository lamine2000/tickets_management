package sn.trivial.ticket.service.mapper;

import org.mapstruct.*;
import sn.trivial.ticket.domain.Agent;
import sn.trivial.ticket.domain.Client;
import sn.trivial.ticket.domain.Ticket;
import sn.trivial.ticket.service.dto.AgentDTO;
import sn.trivial.ticket.service.dto.ClientDTO;
import sn.trivial.ticket.service.dto.TicketDTO;

/**
 * Mapper for the entity {@link Ticket} and its DTO {@link TicketDTO}.
 */
@Mapper(componentModel = "spring")
public interface TicketMapper extends EntityMapper<TicketDTO, Ticket> {
    @Mapping(target = "issuedBy", source = "issuedBy", qualifiedByName = "clientId")
    @Mapping(target = "assignedTo", source = "assignedTo", qualifiedByName = "agentId")
    TicketDTO toDto(Ticket s);

    @Named("clientId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ClientDTO toDtoClientId(Client client);

    @Named("agentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AgentDTO toDtoAgentId(Agent agent);
}
