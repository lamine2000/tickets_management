package sn.trivial.ticket.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import sn.trivial.ticket.domain.enumeration.TicketStatus;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link sn.trivial.ticket.domain.Ticket} entity. This class is used
 * in {@link sn.trivial.ticket.web.rest.TicketResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /tickets?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TicketCriteria implements Serializable, Criteria {

    /**
     * Class for filtering TicketStatus
     */
    public static class TicketStatusFilter extends Filter<TicketStatus> {

        public TicketStatusFilter() {}

        public TicketStatusFilter(TicketStatusFilter filter) {
            super(filter);
        }

        @Override
        public TicketStatusFilter copy() {
            return new TicketStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter code;

    private TicketStatusFilter status;

    private StringFilter issueDescription;

    private InstantFilter issuedAt;

    private LongFilter issuedById;

    private LongFilter assignedToId;

    private Boolean distinct;

    public TicketCriteria() {}

    public TicketCriteria(TicketCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.code = other.code == null ? null : other.code.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.issueDescription = other.issueDescription == null ? null : other.issueDescription.copy();
        this.issuedAt = other.issuedAt == null ? null : other.issuedAt.copy();
        this.issuedById = other.issuedById == null ? null : other.issuedById.copy();
        this.assignedToId = other.assignedToId == null ? null : other.assignedToId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public TicketCriteria copy() {
        return new TicketCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getCode() {
        return code;
    }

    public StringFilter code() {
        if (code == null) {
            code = new StringFilter();
        }
        return code;
    }

    public void setCode(StringFilter code) {
        this.code = code;
    }

    public TicketStatusFilter getStatus() {
        return status;
    }

    public TicketStatusFilter status() {
        if (status == null) {
            status = new TicketStatusFilter();
        }
        return status;
    }

    public void setStatus(TicketStatusFilter status) {
        this.status = status;
    }

    public StringFilter getIssueDescription() {
        return issueDescription;
    }

    public StringFilter issueDescription() {
        if (issueDescription == null) {
            issueDescription = new StringFilter();
        }
        return issueDescription;
    }

    public void setIssueDescription(StringFilter issueDescription) {
        this.issueDescription = issueDescription;
    }

    public InstantFilter getIssuedAt() {
        return issuedAt;
    }

    public InstantFilter issuedAt() {
        if (issuedAt == null) {
            issuedAt = new InstantFilter();
        }
        return issuedAt;
    }

    public void setIssuedAt(InstantFilter issuedAt) {
        this.issuedAt = issuedAt;
    }

    public LongFilter getIssuedById() {
        return issuedById;
    }

    public LongFilter issuedById() {
        if (issuedById == null) {
            issuedById = new LongFilter();
        }
        return issuedById;
    }

    public void setIssuedById(LongFilter issuedById) {
        this.issuedById = issuedById;
    }

    public LongFilter getAssignedToId() {
        return assignedToId;
    }

    public LongFilter assignedToId() {
        if (assignedToId == null) {
            assignedToId = new LongFilter();
        }
        return assignedToId;
    }

    public void setAssignedToId(LongFilter assignedToId) {
        this.assignedToId = assignedToId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final TicketCriteria that = (TicketCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(code, that.code) &&
            Objects.equals(status, that.status) &&
            Objects.equals(issueDescription, that.issueDescription) &&
            Objects.equals(issuedAt, that.issuedAt) &&
            Objects.equals(issuedById, that.issuedById) &&
            Objects.equals(assignedToId, that.assignedToId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, status, issueDescription, issuedAt, issuedById, assignedToId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TicketCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (code != null ? "code=" + code + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (issueDescription != null ? "issueDescription=" + issueDescription + ", " : "") +
            (issuedAt != null ? "issuedAt=" + issuedAt + ", " : "") +
            (issuedById != null ? "issuedById=" + issuedById + ", " : "") +
            (assignedToId != null ? "assignedToId=" + assignedToId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
