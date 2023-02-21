package sn.trivial.ticket.service;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.trivial.ticket.service.dto.TicketDTO;
import sn.trivial.ticket.web.rest.vm.ChangeTicketStatusVM;
import sn.trivial.ticket.web.rest.vm.TicketAndMessageVM;

/**
 * Service Interface for managing {@link sn.trivial.ticket.domain.Ticket}.
 */
public interface TicketService {
    /**
     * Save a ticket.
     *
     * @param ticketDTO the entity to save.
     * @return the persisted entity.
     */
    TicketDTO save(TicketDTO ticketDTO);

    /**
     * Updates a ticket.
     *
     * @param ticketDTO the entity to update.
     * @return the persisted entity.
     */
    TicketDTO update(TicketDTO ticketDTO);

    /**
     * Partially updates a ticket.
     *
     * @param ticketDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TicketDTO> partialUpdate(TicketDTO ticketDTO);

    /**
     * Get all the tickets.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TicketDTO> findAll(Pageable pageable);

    /**
     * Get the "id" ticket.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TicketDTO> findOne(Long id);

    /**
     * Delete the "id" ticket.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Get all the tickets created by the authenticated client.
     *
     * @return the list of entities.
     */
    List<TicketDTO> findTicketsOfConnectedClient();

    /**
     * Save a ticket issued by the currently connected Client.
     *
     * @param ticketDTO the entity to save.
     * @return the persisted entity.
     */
    TicketDTO saveWithConnectedClient(TicketAndMessageVM ticketAndMessageVM);

    /**
     * Get the "ticketId" ticket if it has been created by the connected Client.
     *
     * @param ticketId the id of the entity.
     * @return the entity.
     */
    Optional<TicketDTO> findOneTicketOfConnectedClient(Long ticketId);

    /**
     * Updates the status of a ticket that has been created by the connected client.
     *
     * @param changeTicketStatusVM the view model representing a ticket and the new Status.
     * @return the persisted entity.
     */
    TicketDTO changeTicketStatusByClient(ChangeTicketStatusVM changeTicketStatusVM);
}
