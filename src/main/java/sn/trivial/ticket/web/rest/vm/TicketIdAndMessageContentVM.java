package sn.trivial.ticket.web.rest.vm;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class TicketIdAndMessageContentVM {

    @NotNull
    private Long ticketId;

    @NotBlank
    private String messageContent;

    public Long getTicketId() {
        return ticketId;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }
}
