package sn.trivial.ticket.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.trivial.ticket.service.dto.AgentDTO;

/**
 * Service Interface for managing {@link sn.trivial.ticket.domain.Agent}.
 */
public interface AgentService {
    /**
     * Save a agent.
     *
     * @param agentDTO the entity to save.
     * @return the persisted entity.
     */
    AgentDTO save(AgentDTO agentDTO);

    /**
     * Updates a agent.
     *
     * @param agentDTO the entity to update.
     * @return the persisted entity.
     */
    AgentDTO update(AgentDTO agentDTO);

    /**
     * Partially updates a agent.
     *
     * @param agentDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AgentDTO> partialUpdate(AgentDTO agentDTO);

    /**
     * Get all the agents.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AgentDTO> findAll(Pageable pageable);

    /**
     * Get all the agents with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AgentDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" agent.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AgentDTO> findOne(Long id);

    /**
     * Delete the "id" agent.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Get the "login" agent.
     *
     * @param login the login of the entity's user.
     * @return the entity.
     */
    Optional<AgentDTO> findByUser_Login(String login);

    /**
     * Register a new agent.
     *
     * @param agentDTO the entity to save.
     * @return the persisted entity.
     */
    AgentDTO register(AgentDTO agentDTO);
}
