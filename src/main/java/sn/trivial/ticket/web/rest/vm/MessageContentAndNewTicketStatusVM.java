package sn.trivial.ticket.web.rest.vm;

import javax.validation.constraints.NotNull;
import sn.trivial.ticket.domain.enumeration.TicketStatus;

public class MessageContentAndNewTicketStatusVM extends TicketIdAndMessageContentVM {

    @NotNull
    private TicketStatus newTicketStatus;

    public TicketStatus getNewTicketStatus() {
        return newTicketStatus;
    }

    public void setNewTicketStatus(TicketStatus newTicketStatus) {
        this.newTicketStatus = newTicketStatus;
    }

    @Override
    public String toString() {
        return "MessageContentAndNewTicketStatusVM{" + "newTicketStatus=" + newTicketStatus + "} " + super.toString();
    }
}
