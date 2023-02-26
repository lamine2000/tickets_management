package sn.trivial.ticket.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.trivial.ticket.service.dto.ClientDTO;

/**
 * Service Interface for managing {@link sn.trivial.ticket.domain.Client}.
 */
public interface ClientService {
    /**
     * Save a client.
     *
     * @param clientDTO the entity to save.
     * @return the persisted entity.
     */
    ClientDTO save(ClientDTO clientDTO);

    /**
     * Updates a client.
     *
     * @param clientDTO the entity to update.
     * @return the persisted entity.
     */
    ClientDTO update(ClientDTO clientDTO);

    /**
     * Partially updates a client.
     *
     * @param clientDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ClientDTO> partialUpdate(ClientDTO clientDTO);

    /**
     * Get all the clients.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ClientDTO> findAll(Pageable pageable);

    /**
     * Get all the clients with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ClientDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" client.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ClientDTO> findOne(Long id);

    /**
     * Delete the "id" client.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Get the "login" client.
     *
     * @param login the login of the user.
     * @return the entity.
     */
    Optional<ClientDTO> findByUser_Login(String login);

    //register a new client
    /**
     * Register a new client.
     *
     * @param clientDTO the entity to save.
     * @return the persisted entity.
     */
    ClientDTO register(ClientDTO clientDTO);
}
