package sn.trivial.ticket.service;

import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.trivial.ticket.domain.*; // for static metamodels
import sn.trivial.ticket.domain.Message;
import sn.trivial.ticket.repository.MessageRepository;
import sn.trivial.ticket.service.criteria.MessageCriteria;
import sn.trivial.ticket.service.dto.MessageDTO;
import sn.trivial.ticket.service.mapper.MessageMapper;
import tech.jhipster.service.QueryService;
import tech.jhipster.service.filter.LongFilter;

/**
 * Service for executing complex queries for {@link Message} entities in the database.
 * The main input is a {@link MessageCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MessageDTO} or a {@link Page} of {@link MessageDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MessageQueryService extends QueryService<Message> {

    private final Logger log = LoggerFactory.getLogger(MessageQueryService.class);

    private final MessageRepository messageRepository;

    private final MessageMapper messageMapper;

    private final TicketService ticketService;

    public MessageQueryService(MessageRepository messageRepository, MessageMapper messageMapper, TicketService ticketService) {
        this.messageRepository = messageRepository;
        this.messageMapper = messageMapper;
        this.ticketService = ticketService;
    }

    /**
     * Return a {@link List} of {@link MessageDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MessageDTO> findByCriteria(MessageCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Message> specification = createSpecification(criteria);
        return messageMapper.toDto(messageRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link MessageDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MessageDTO> findByCriteria(MessageCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Message> specification = createSpecification(criteria);
        return messageRepository.findAll(specification, page).map(messageMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MessageCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Message> specification = createSpecification(criteria);
        return messageRepository.count(specification);
    }

    /**
     * Function to convert {@link MessageCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Message> createSpecification(MessageCriteria criteria) {
        Specification<Message> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Message_.id));
            }
            if (criteria.getSentAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSentAt(), Message_.sentAt));
            }
            if (criteria.getTicketId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getTicketId(), root -> root.join(Message_.ticket, JoinType.LEFT).get(Ticket_.id))
                    );
            }
            if (criteria.getSentById() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getSentById(), root -> root.join(Message_.sentBy, JoinType.LEFT).get(User_.id))
                    );
            }
        }
        return specification;
    }

    /* CUSTOM */

    /**
     * Return a {@link Page} of {@link MessageDTO} which matches the ticket Id from the database.
     * @param ticketId the id of the ticket.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MessageDTO> findByCriteriaAndTicketID(Long ticketId) {
        log.debug("find by ticket Id: {}", ticketId);
        MessageCriteria criteria = new MessageCriteria();
        LongFilter ticketIdFilter = new LongFilter();
        ticketIdFilter.setEquals(ticketId);
        criteria.setTicketId(ticketIdFilter);

        final Specification<Message> specification = createSpecification(criteria);

        return messageRepository
            .findAll(specification)
            .stream()
            .filter(message -> ticketService.isIssuedByConnectedUser(message.getTicket().getId()))
            .map(messageMapper::toDto)
            .collect(Collectors.toList());
    }
}
