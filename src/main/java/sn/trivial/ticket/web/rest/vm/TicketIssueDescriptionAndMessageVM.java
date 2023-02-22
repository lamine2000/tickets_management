package sn.trivial.ticket.web.rest.vm;

import javax.validation.constraints.NotBlank;
import sn.trivial.ticket.service.dto.TicketDTO;

/**
 * View Model object for storing a ticket and a message.
 */
public class TicketIssueDescriptionAndMessageVM {

    @NotBlank
    private String issueDescription;

    @NotBlank
    private String messageContent;

    public String getIssueDescription() {
        return issueDescription;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setIssueDescription(String issueDescription) {
        this.issueDescription = issueDescription;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    @Override
    public String toString() {
        return (
            "TicketIssueDescriptionAndMessageVM{" +
            "issueDescription='" +
            issueDescription +
            '\'' +
            ", messageContent='" +
            messageContent +
            '\'' +
            '}'
        );
    }
}
