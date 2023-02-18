package sn.trivial.ticket.service.impl;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.trivial.ticket.domain.Ticket;
import sn.trivial.ticket.domain.User;
import sn.trivial.ticket.domain.enumeration.TicketStatus;
import sn.trivial.ticket.repository.TicketRepository;
import sn.trivial.ticket.service.AgentService;
import sn.trivial.ticket.service.ClientService;
import sn.trivial.ticket.service.TicketService;
import sn.trivial.ticket.service.UserService;
import sn.trivial.ticket.service.dto.TicketDTO;
import sn.trivial.ticket.service.mapper.TicketMapper;

/**
 * Service Implementation for managing {@link Ticket}.
 */
@Service
@Transactional
public class TicketServiceImpl implements TicketService {

    private final Logger log = LoggerFactory.getLogger(TicketServiceImpl.class);

    private final TicketRepository ticketRepository;

    private final TicketMapper ticketMapper;

    private final UserService userService;

    private final ClientService clientService;

    private final AgentService agentService;

    public TicketServiceImpl(
        TicketRepository ticketRepository,
        TicketMapper ticketMapper,
        UserService userService,
        ClientService clientService,
        AgentService agentService
    ) {
        this.ticketRepository = ticketRepository;
        this.ticketMapper = ticketMapper;
        this.userService = userService;
        this.clientService = clientService;
        this.agentService = agentService;
    }

    @Override
    public TicketDTO save(TicketDTO ticketDTO) {
        log.debug("Request to save Ticket : {}", ticketDTO);
        Ticket ticket = ticketMapper.toEntity(ticketDTO);
        ticket = ticketRepository.save(ticket);
        return ticketMapper.toDto(ticket);
    }

    @Override
    public TicketDTO update(TicketDTO ticketDTO) {
        log.debug("Request to update Ticket : {}", ticketDTO);
        Ticket ticket = ticketMapper.toEntity(ticketDTO);
        ticket = ticketRepository.save(ticket);
        return ticketMapper.toDto(ticket);
    }

    @Override
    public Optional<TicketDTO> partialUpdate(TicketDTO ticketDTO) {
        log.debug("Request to partially update Ticket : {}", ticketDTO);

        return ticketRepository
            .findById(ticketDTO.getId())
            .map(existingTicket -> {
                ticketMapper.partialUpdate(existingTicket, ticketDTO);

                return existingTicket;
            })
            .map(ticketRepository::save)
            .map(ticketMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TicketDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Tickets");
        return ticketRepository.findAll(pageable).map(ticketMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TicketDTO> findOne(Long id) {
        log.debug("Request to get Ticket : {}", id);
        return ticketRepository.findById(id).map(ticketMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Ticket : {}", id);
        ticketRepository.deleteById(id);
    }

    @Override
    public List<TicketDTO> findTicketsOfConnectedClient() {
        log.debug("Request to get tickets of connected Client");
        User user = userService.getUserWithAuthorities().get();

        Long clientId = clientService.findByUser_Login(user.getLogin()).get().getId();

        return ticketRepository.findByIssuedBy_Id(clientId).stream().map(ticketMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public TicketDTO saveWithConnectedClient(TicketDTO ticketDTO) {
        log.debug("Request to save Ticket created by the currently connected Client: {}", ticketDTO);

        User user = userService.getUserWithAuthorities().get();

        ticketDTO.setIssuedBy(clientService.findByUser_Login(user.getLogin()).get());
        ticketDTO.setIssuedAt(Instant.now());
        ticketDTO.setCode(String.format("T-%s-%s", user.getId(), UUID.randomUUID()));
        ticketDTO.setStatus(TicketStatus.RECEIVED);
        //the "no_agent" agent should always exist
        ticketDTO.setAssignedTo(agentService.findByUser_Login("no_agent").get());

        Ticket ticket = ticketMapper.toEntity(ticketDTO);
        ticket = ticketRepository.save(ticket);
        return ticketMapper.toDto(ticket);
    }
}
