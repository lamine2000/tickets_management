package sn.trivial.ticket.service.mapper;

import org.mapstruct.*;
import sn.trivial.ticket.domain.Message;
import sn.trivial.ticket.domain.Ticket;
import sn.trivial.ticket.service.dto.MessageDTO;
import sn.trivial.ticket.service.dto.TicketDTO;

/**
 * Mapper for the entity {@link Message} and its DTO {@link MessageDTO}.
 */
@Mapper(componentModel = "spring")
public interface MessageMapper extends EntityMapper<MessageDTO, Message> {
    @Mapping(target = "ticket", source = "ticket", qualifiedByName = "ticketId")
    MessageDTO toDto(Message s);

    @Named("ticketId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TicketDTO toDtoTicketId(Ticket ticket);
}
