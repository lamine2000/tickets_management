package sn.trivial.ticket.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import sn.trivial.ticket.repository.TicketRepository;
import sn.trivial.ticket.service.TicketQueryService;
import sn.trivial.ticket.service.TicketService;
import sn.trivial.ticket.service.criteria.TicketCriteria;
import sn.trivial.ticket.service.dto.MessageDTO;
import sn.trivial.ticket.service.dto.TicketDTO;
import sn.trivial.ticket.web.rest.errors.BadRequestAlertException;
import sn.trivial.ticket.web.rest.vm.ChangeTicketStatusVM;
import sn.trivial.ticket.web.rest.vm.TicketIdAndMessageContentVM;
import sn.trivial.ticket.web.rest.vm.TicketIssueDescriptionAndMessageVM;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.trivial.ticket.domain.Ticket}.
 */
@RestController
@RequestMapping("/api")
public class TicketResource {

    private final Logger log = LoggerFactory.getLogger(TicketResource.class);

    private static final String ENTITY_NAME = "ticket";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TicketService ticketService;

    private final TicketRepository ticketRepository;

    private final TicketQueryService ticketQueryService;

    public TicketResource(TicketService ticketService, TicketRepository ticketRepository, TicketQueryService ticketQueryService) {
        this.ticketService = ticketService;
        this.ticketRepository = ticketRepository;
        this.ticketQueryService = ticketQueryService;
    }

    /**
     * {@code POST  /tickets} : Create a new ticket.
     *
     * @param ticketDTO the ticketDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ticketDTO, or with status {@code 400 (Bad Request)} if the ticket has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tickets")
    public ResponseEntity<TicketDTO> createTicket(@Valid @RequestBody TicketDTO ticketDTO) throws URISyntaxException {
        log.debug("REST request to save Ticket : {}", ticketDTO);
        if (ticketDTO.getId() != null) {
            throw new BadRequestAlertException("A new ticket cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TicketDTO result = ticketService.save(ticketDTO);
        return ResponseEntity
            .created(new URI("/api/tickets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tickets/:id} : Updates an existing ticket.
     *
     * @param id the id of the ticketDTO to save.
     * @param ticketDTO the ticketDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ticketDTO,
     * or with status {@code 400 (Bad Request)} if the ticketDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ticketDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tickets/{id}")
    public ResponseEntity<TicketDTO> updateTicket(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TicketDTO ticketDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Ticket : {}, {}", id, ticketDTO);
        if (ticketDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ticketDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ticketRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TicketDTO result = ticketService.update(ticketDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ticketDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tickets/:id} : Partial updates given fields of an existing ticket, field will ignore if it is null
     *
     * @param id the id of the ticketDTO to save.
     * @param ticketDTO the ticketDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ticketDTO,
     * or with status {@code 400 (Bad Request)} if the ticketDTO is not valid,
     * or with status {@code 404 (Not Found)} if the ticketDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the ticketDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tickets/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TicketDTO> partialUpdateTicket(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TicketDTO ticketDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Ticket partially : {}, {}", id, ticketDTO);
        if (ticketDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ticketDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ticketRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TicketDTO> result = ticketService.partialUpdate(ticketDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ticketDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /tickets} : get all the tickets.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tickets in body.
     */
    @GetMapping("/tickets")
    public ResponseEntity<List<TicketDTO>> getAllTickets(
        TicketCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Tickets by criteria: {}", criteria);
        Page<TicketDTO> page = ticketQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tickets/count} : count all the tickets.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/tickets/count")
    public ResponseEntity<Long> countTickets(TicketCriteria criteria) {
        log.debug("REST request to count Tickets by criteria: {}", criteria);
        return ResponseEntity.ok().body(ticketQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /tickets/:id} : get the "id" ticket.
     *
     * @param id the id of the ticketDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ticketDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tickets/{id}")
    public ResponseEntity<TicketDTO> getTicket(@PathVariable Long id) {
        log.debug("REST request to get Ticket : {}", id);
        Optional<TicketDTO> ticketDTO = ticketService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ticketDTO);
    }

    /**
     * {@code DELETE  /tickets/:id} : delete the "id" ticket.
     *
     * @param id the id of the ticketDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tickets/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long id) {
        log.debug("REST request to delete Ticket : {}", id);
        ticketService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /* Custom */
    /**
     * {@code GET  /tickets/clients/} : get all the tickets of the current client.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ticketDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tickets/clients")
    public ResponseEntity<List<TicketDTO>> getTicketsOfConnectedClient() {
        log.debug("REST request to get tickets of connected Client");
        List<TicketDTO> ticketList = ticketService.findTicketsOfConnectedClient();
        return ResponseEntity.ok().body(ticketList);
    }

    /**
     * {@code POST  /tickets/clients} : Create a new ticket by the connected Client.
     *
     * @param ticketIssueDescriptionAndMessageVM the ticket to create and message to assign to link to the ticket.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ticketDTO, or with status {@code 400 (Bad Request)} if the ticket has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tickets/clients")
    public ResponseEntity<TicketDTO> createTicketWithConnectedClient(
        @Valid @RequestBody TicketIssueDescriptionAndMessageVM ticketIssueDescriptionAndMessageVM
    ) throws URISyntaxException {
        log.debug(
            "REST request to create a ticket issued by the connected Client, with a first message : {}",
            ticketIssueDescriptionAndMessageVM
        );

        TicketDTO result = ticketService.createTicketWithConnectedClient(ticketIssueDescriptionAndMessageVM);
        return ResponseEntity
            .created(new URI("/api/tickets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /tickets/clients/} : get the "id" ticket of the current client.
     *
     * @return the {@link ResponseEntity} with status {@code                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    200 (OK)} and with body the ticketDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tickets/{id}/clients")
    public ResponseEntity<TicketDTO> getOneTicketOfConnectedClient(@PathVariable Long id) {
        log.debug("REST request to get the {} ticket of connected Client", id);
        Optional<TicketDTO> ticketDTO = ticketService.findOneTicketOfConnectedClient(id);
        return ResponseUtil.wrapOrNotFound(ticketDTO);
    }

    /**
     * {@code POST  /tickets/change-status} : Change the status of a ticket.
     *
     * @param changeTicketStatusVM the ticketDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ticketDTO, or with status {@code 400 (Bad Request)} if the ticket has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tickets/change-status")
    public ResponseEntity<TicketDTO> changeTicketStatus(@Valid @RequestBody ChangeTicketStatusVM changeTicketStatusVM)
        throws URISyntaxException {
        log.debug(
            "REST request to change the status of a ticket of the connected Client. " + "Ticket id and new ticket status : {}",
            changeTicketStatusVM
        );
        if (changeTicketStatusVM.getTicketId() == null || changeTicketStatusVM.getTicketStatus() == null) {
            throw new BadRequestAlertException("The ticket's id and new ticket status are both required", ENTITY_NAME, "requiredfield");
        }
        TicketDTO result = ticketService.changeTicketStatusByClient(changeTicketStatusVM);
        return ResponseEntity
            .created(new URI("/tickets/change-status" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code POST  /tickets/{id}/send-message/clients : Create a new message linked to a ticket issued by the connected client.
     *
     * @param id the ticket id and messageContent content of the message to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new messageDTO, or with status {@code 400 (Bad Request)}.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tickets/{id}/send-message/clients")
    public ResponseEntity<MessageDTO> sendMessageByConnectedClient(@PathVariable Long id, @NotBlank @RequestBody String messageContent)
        throws URISyntaxException {
        TicketIdAndMessageContentVM ticketIdAndMessageContentVM = new TicketIdAndMessageContentVM();
        ticketIdAndMessageContentVM.setMessageContent(messageContent);
        ticketIdAndMessageContentVM.setTicketId(id);
        log.debug("REST request to save Message linked to a ticket issued by the connected client: {}", ticketIdAndMessageContentVM);

        MessageDTO result = ticketService.sendMessageByConnectedClient(ticketIdAndMessageContentVM);
        return ResponseEntity
            .created(new URI("/tickets" + result.getId() + "/send-message/clients"))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /tickets/unassigned} : get all the unassigned tickets.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tickets in body.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    200 (OK)} and with body the ticketDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tickets/unassigned")
    public ResponseEntity<List<TicketDTO>> getAllUnassignedTickets() {
        log.debug("REST request to get the unassigned tickets");
        List<TicketDTO> tickets = ticketService.findAllUnassigned();
        return ResponseEntity.ok().body(tickets);
    }

    /**
     * {@code GET  /tickets/self-assign} : Self assign a ticket.
     *
     * @param id the ticket id.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the ticket in body.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    200 (OK)} and with body the ticketDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tickets/{id}/self-assign")
    public ResponseEntity<TicketDTO> selfAssignTicket(@PathVariable Long id) throws URISyntaxException {
        log.debug("REST request to get the unassigned tickets");
        TicketDTO ticketDTO = ticketService.selfAssignTicket(id);
        return ResponseEntity
            .created(new URI("/api/tickets/" + ticketDTO.getId() + "/self-assign"))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, ticketDTO.getId().toString()))
            .body(ticketDTO);
    }

    /**
     * {@code GET  /tickets/assigned} : Get all the tickets that are assigned to the connected Agent.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tickets in body.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    200 (OK)} and with body the ticketDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tickets/assigned")
    public ResponseEntity<List<TicketDTO>> getAllAssignedToConnectedAgent() {
        log.debug("REST request to get the tickets assigned to the connected Agent");
        List<TicketDTO> tickets = ticketService.findAllAssignedToConnectedAgent();
        return ResponseEntity.ok().body(tickets);
    }

    /**
     * {@code GET  /tickets/:id/assigned} : Get the "id" ticket if assigned to the connected Agent.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the ticket in body.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    200 (OK)} and with body the ticketDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tickets/{id}/assigned")
    public ResponseEntity<TicketDTO> getSpecificAssignedToConnectedAgent(@PathVariable Long id) {
        log.debug("REST request to get a specific ticket assigned to the connected Agent. ticketId: {}", id);
        Optional<TicketDTO> ticket = ticketService.findSpecificAssignedToConnectedAgent(id);
        return ResponseUtil.wrapOrNotFound(ticket);
    }
}
