package sn.trivial.ticket.web.rest.vm;

import javax.validation.constraints.NotBlank;
import sn.trivial.ticket.service.dto.MessageDTO;
import sn.trivial.ticket.service.dto.TicketDTO;

/**
 * View Model object for storing a ticket and a message.
 */
public class TicketAndMessageVM {

    private TicketDTO ticketDTO;

    @NotBlank
    private String messageContent;

    public TicketDTO getTicket() {
        return this.ticketDTO;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setTicketDTO(TicketDTO ticketDTO) {
        this.ticketDTO = ticketDTO;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    @Override
    public String toString() {
        return "TicketAndMessageVM{" + "ticketDTO=" + ticketDTO + ", messageContent=" + messageContent + '}';
    }
}
