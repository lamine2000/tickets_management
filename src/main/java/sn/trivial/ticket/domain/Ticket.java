package sn.trivial.ticket.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import sn.trivial.ticket.domain.enumeration.TicketStatus;

/**
 * A Ticket.
 */
@Entity
@Table(name = "ticket")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Ticket implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TicketStatus status;

    @NotNull
    @Column(name = "issue_description", nullable = false)
    private String issueDescription;

    @NotNull
    @Column(name = "issued_at", nullable = false)
    private Instant issuedAt;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user" }, allowSetters = true)
    private Client issuedBy;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user" }, allowSetters = true)
    private Client assignedTo;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Ticket id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public Ticket code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public TicketStatus getStatus() {
        return this.status;
    }

    public Ticket status(TicketStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public String getIssueDescription() {
        return this.issueDescription;
    }

    public Ticket issueDescription(String issueDescription) {
        this.setIssueDescription(issueDescription);
        return this;
    }

    public void setIssueDescription(String issueDescription) {
        this.issueDescription = issueDescription;
    }

    public Instant getIssuedAt() {
        return this.issuedAt;
    }

    public Ticket issuedAt(Instant issuedAt) {
        this.setIssuedAt(issuedAt);
        return this;
    }

    public void setIssuedAt(Instant issuedAt) {
        this.issuedAt = issuedAt;
    }

    public Client getIssuedBy() {
        return this.issuedBy;
    }

    public void setIssuedBy(Client client) {
        this.issuedBy = client;
    }

    public Ticket issuedBy(Client client) {
        this.setIssuedBy(client);
        return this;
    }

    public Client getAssignedTo() {
        return this.assignedTo;
    }

    public void setAssignedTo(Client client) {
        this.assignedTo = client;
    }

    public Ticket assignedTo(Client client) {
        this.setAssignedTo(client);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Ticket)) {
            return false;
        }
        return id != null && id.equals(((Ticket) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Ticket{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", status='" + getStatus() + "'" +
            ", issueDescription='" + getIssueDescription() + "'" +
            ", issuedAt='" + getIssuedAt() + "'" +
            "}";
    }
}
