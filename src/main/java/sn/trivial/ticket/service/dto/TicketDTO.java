package sn.trivial.ticket.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;
import sn.trivial.ticket.domain.enumeration.TicketStatus;

/**
 * A DTO for the {@link sn.trivial.ticket.domain.Ticket} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TicketDTO implements Serializable {

    private Long id;

    @NotNull
    private String code;

    @NotNull
    private TicketStatus status;

    @NotNull
    private String issueDescription;

    @NotNull
    private Instant issuedAt;

    private ClientDTO issuedBy;

    private ClientDTO assignedTo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public String getIssueDescription() {
        return issueDescription;
    }

    public void setIssueDescription(String issueDescription) {
        this.issueDescription = issueDescription;
    }

    public Instant getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(Instant issuedAt) {
        this.issuedAt = issuedAt;
    }

    public ClientDTO getIssuedBy() {
        return issuedBy;
    }

    public void setIssuedBy(ClientDTO issuedBy) {
        this.issuedBy = issuedBy;
    }

    public ClientDTO getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(ClientDTO assignedTo) {
        this.assignedTo = assignedTo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TicketDTO)) {
            return false;
        }

        TicketDTO ticketDTO = (TicketDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, ticketDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TicketDTO{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", status='" + getStatus() + "'" +
            ", issueDescription='" + getIssueDescription() + "'" +
            ", issuedAt='" + getIssuedAt() + "'" +
            ", issuedBy=" + getIssuedBy() +
            ", assignedTo=" + getAssignedTo() +
            "}";
    }
}
