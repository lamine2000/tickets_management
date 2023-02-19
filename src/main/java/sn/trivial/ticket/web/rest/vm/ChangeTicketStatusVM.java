package sn.trivial.ticket.web.rest.vm;

import sn.trivial.ticket.domain.enumeration.TicketStatus;

/**
 * View Model object for storing a ticket's id and the new TicketStatus we want to assign to it.
 */

public class ChangeTicketStatusVM {

    private Long ticketId;

    private TicketStatus ticketStatus;

    public Long getTicketId() {
        return ticketId;
    }

    public TicketStatus getTicketStatus() {
        return ticketStatus;
    }

    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
    }

    public void setTicketStatus(TicketStatus ticketStatus) {
        this.ticketStatus = ticketStatus;
    }

    @Override
    public String toString() {
        return "ChangeTicketStatusVM{" + "ticketId= " + ticketId + ", ticketStatus= " + ticketStatus + "}";
    }
}
