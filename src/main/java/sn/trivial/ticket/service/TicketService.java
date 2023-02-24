package sn.trivial.ticket.service;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.trivial.ticket.service.dto.MessageDTO;
import sn.trivial.ticket.service.dto.TicketDTO;
import sn.trivial.ticket.web.rest.vm.ChangeTicketStatusVM;
import sn.trivial.ticket.web.rest.vm.TicketIdAndMessageContentVM;
import sn.trivial.ticket.web.rest.vm.TicketIssueDescriptionAndMessageVM;

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
     * @param ticketIssueDescriptionAndMessageVM the ticket and message to be saved.
     * @return the persisted entity.
     */
    TicketDTO createTicketWithConnectedClient(TicketIssueDescriptionAndMessageVM ticketIssueDescriptionAndMessageVM);

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

    /**
     * Tells if the "clientId" client is the owner of the "ticketId" ticket.
     *
     * @param ticketId the tickets's id.
     * @param clienId the client's id.
     * @return true if the client is the owner or else, false.
     */
    Boolean isIssuedBySpecificClient(Long ticketId, Long clienId);

    /**
     * Tells if the "userId" user is the owner of the "ticketId" ticket.
     *
     * @param ticketId the tickets's id.
     * @param userId the user's id.
     * @return true if the user is the owner or else, false.
     */
    Boolean isIssuedBySpecificUser(Long ticketId, Long userId);

    /**
     * Tells if the connected user is the owner of the "ticketId" ticket.
     *
     * @param ticketId the tickets's id.
     * @return true if the user is the owner or else, false.
     */
    Boolean isIssuedByConnectedUser(Long ticketId);

    /**
     * Tells if it is to a client turn to send a message (in contrario to an Agent/Admin turn).
     *
     * @param ticketId the tickets's id.
     * @return true if it is to a client turn to send a message or else, false.
     */

    Boolean isClientTurn(Long ticketId);

    /**
     * Create a new message linked to a ticket issued by the connected client.
     *
     * @param ticketIdAndMessageContentVM the ticket id and message content of the message to create.
     * @return the persisted entity.
     */
    MessageDTO sendMessageByConnectedClient(TicketIdAndMessageContentVM ticketIdAndMessageContentVM);

    /**
     * Get all the unassigned tickets.
     *
     * @return the list of entities.
     */
    List<TicketDTO> findAllUnassigned();

    /**
     * Get all the tickets assigned to the connected admin.
     *
     * @return the list of entities.
     */
    List<TicketDTO> findAllAssigned();

    /**
     * Self assign a ticket.
     *
     * @param ticketId the id of the entity.
     * @return the entity.
     */
    TicketDTO selfAssignTicket(Long ticketId);

    /**
     * Get all the tickets assigned to the connected agent.
     *
     * @return the list of entities.
     */
    List<TicketDTO> findAllAssignedToConnectedAgent();

    /**
     * Get the "id" ticket if it has been assigned to the connected agent.
     *
     * @param ticketId the id of the entity.
     * @return the entity.
     */
    Optional<TicketDTO> findSpecificAssignedToConnectedAgent(Long ticketId);
}
