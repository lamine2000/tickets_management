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
import sn.trivial.ticket.domain.Agent;
import sn.trivial.ticket.repository.AgentRepository;
import sn.trivial.ticket.service.criteria.AgentCriteria;
import sn.trivial.ticket.service.dto.AgentDTO;
import sn.trivial.ticket.service.mapper.AgentMapper;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Agent} entities in the database.
 * The main input is a {@link AgentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AgentDTO} or a {@link Page} of {@link AgentDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AgentQueryService extends QueryService<Agent> {

    private final Logger log = LoggerFactory.getLogger(AgentQueryService.class);

    private final AgentRepository agentRepository;

    private final AgentMapper agentMapper;

    public AgentQueryService(AgentRepository agentRepository, AgentMapper agentMapper) {
        this.agentRepository = agentRepository;
        this.agentMapper = agentMapper;
    }

    /**
     * Return a {@link List} of {@link AgentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AgentDTO> findByCriteria(AgentCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Agent> specification = createSpecification(criteria);
        return agentMapper.toDto(agentRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link AgentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AgentDTO> findByCriteria(AgentCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Agent> specification = createSpecification(criteria);
        return agentRepository.findAll(specification, page).map(agentMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AgentCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Agent> specification = createSpecification(criteria);
        return agentRepository.count(specification);
    }

    /**
     * Function to convert {@link AgentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Agent> createSpecification(AgentCriteria criteria) {
        Specification<Agent> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Agent_.id));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), Agent_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), Agent_.lastName));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Agent_.email));
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(Agent_.user, JoinType.LEFT).get(User_.id))
                    );
            }
        }
        return specification;
    }
}
