package sn.trivial.ticket.service;

import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.trivial.ticket.domain.*; // for static metamodels
import sn.trivial.ticket.domain.Ticket;
import sn.trivial.ticket.repository.TicketRepository;
import sn.trivial.ticket.service.criteria.TicketCriteria;
import sn.trivial.ticket.service.dto.TicketDTO;
import sn.trivial.ticket.service.mapper.TicketMapper;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Ticket} entities in the database.
 * The main input is a {@link TicketCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TicketDTO} or a {@link Page} of {@link TicketDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TicketQueryService extends QueryService<Ticket> {

    private final Logger log = LoggerFactory.getLogger(TicketQueryService.class);

    private final TicketRepository ticketRepository;

    private final TicketMapper ticketMapper;

    public TicketQueryService(TicketRepository ticketRepository, TicketMapper ticketMapper) {
        this.ticketRepository = ticketRepository;
        this.ticketMapper = ticketMapper;
    }

    /**
     * Return a {@link List} of {@link TicketDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TicketDTO> findByCriteria(TicketCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Ticket> specification = createSpecification(criteria);
        return ticketMapper.toDto(ticketRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TicketDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TicketDTO> findByCriteria(TicketCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Ticket> specification = createSpecification(criteria);
        return ticketRepository.findAll(specification, page).map(ticketMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TicketCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Ticket> specification = createSpecification(criteria);
        return ticketRepository.count(specification);
    }

    /**
     * Function to convert {@link TicketCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Ticket> createSpecification(TicketCriteria criteria) {
        Specification<Ticket> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Ticket_.id));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), Ticket_.code));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), Ticket_.status));
            }
            if (criteria.getIssueDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIssueDescription(), Ticket_.issueDescription));
            }
            if (criteria.getIssuedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getIssuedAt(), Ticket_.issuedAt));
            }
            if (criteria.getIssuedById() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getIssuedById(), root -> root.join(Ticket_.issuedBy, JoinType.LEFT).get(Client_.id))
                    );
            }
            if (criteria.getAssignedToId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getAssignedToId(), root -> root.join(Ticket_.assignedTo, JoinType.LEFT).get(Agent_.id))
                    );
            }
        }
        return specification;
    }
}
