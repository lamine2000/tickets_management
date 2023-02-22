package sn.trivial.ticket.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link sn.trivial.ticket.domain.Message} entity. This class is used
 * in {@link sn.trivial.ticket.web.rest.MessageResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /messages?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MessageCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter sentAt;

    private LongFilter ticketId;

    private LongFilter sentById;

    private Boolean distinct;

    public MessageCriteria() {}

    public MessageCriteria(MessageCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.sentAt = other.sentAt == null ? null : other.sentAt.copy();
        this.ticketId = other.ticketId == null ? null : other.ticketId.copy();
        this.sentById = other.sentById == null ? null : other.sentById.copy();
        this.distinct = other.distinct;
    }

    @Override
    public MessageCriteria copy() {
        return new MessageCriteria(this);
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

    public InstantFilter getSentAt() {
        return sentAt;
    }

    public InstantFilter sentAt() {
        if (sentAt == null) {
            sentAt = new InstantFilter();
        }
        return sentAt;
    }

    public void setSentAt(InstantFilter sentAt) {
        this.sentAt = sentAt;
    }

    public LongFilter getTicketId() {
        return ticketId;
    }

    public LongFilter ticketId() {
        if (ticketId == null) {
            ticketId = new LongFilter();
        }
        return ticketId;
    }

    public void setTicketId(LongFilter ticketId) {
        this.ticketId = ticketId;
    }

    public LongFilter getSentById() {
        return sentById;
    }

    public LongFilter sentById() {
        if (sentById == null) {
            sentById = new LongFilter();
        }
        return sentById;
    }

    public void setSentById(LongFilter sentById) {
        this.sentById = sentById;
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
        final MessageCriteria that = (MessageCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(sentAt, that.sentAt) &&
            Objects.equals(ticketId, that.ticketId) &&
            Objects.equals(sentById, that.sentById) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sentAt, ticketId, sentById, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MessageCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (sentAt != null ? "sentAt=" + sentAt + ", " : "") +
            (ticketId != null ? "ticketId=" + ticketId + ", " : "") +
            (sentById != null ? "sentById=" + sentById + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
