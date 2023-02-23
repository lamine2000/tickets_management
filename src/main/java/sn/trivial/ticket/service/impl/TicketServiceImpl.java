package sn.trivial.ticket.service.impl;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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
import sn.trivial.ticket.security.AuthoritiesConstants;
import sn.trivial.ticket.service.*;
import sn.trivial.ticket.service.dto.AdminUserDTO;
import sn.trivial.ticket.service.dto.AgentDTO;
import sn.trivial.ticket.service.dto.MessageDTO;
import sn.trivial.ticket.service.dto.TicketDTO;
import sn.trivial.ticket.service.mapper.TicketMapper;
import sn.trivial.ticket.service.mapper.UserMapper;
import sn.trivial.ticket.web.rest.errors.BadRequestAlertException;
import sn.trivial.ticket.web.rest.vm.ChangeTicketStatusVM;
import sn.trivial.ticket.web.rest.vm.TicketIdAndMessageContentVM;
import sn.trivial.ticket.web.rest.vm.TicketIssueDescriptionAndMessageVM;

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

    private final MessageService messageService;

    private final UserMapper userMapper;

    public TicketServiceImpl(
        TicketRepository ticketRepository,
        TicketMapper ticketMapper,
        UserService userService,
        ClientService clientService,
        AgentService agentService,
        MessageService messageService,
        UserMapper userMapper
    ) {
        this.ticketRepository = ticketRepository;
        this.ticketMapper = ticketMapper;
        this.userService = userService;
        this.clientService = clientService;
        this.agentService = agentService;
        this.messageService = messageService;
        this.userMapper = userMapper;
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
    public TicketDTO createTicketWithConnectedClient(TicketIssueDescriptionAndMessageVM ticketIssueDescriptionAndMessageVM) {
        log.debug(
            "Request to save Ticket created by the currently connected Client, with the first message: {}",
            ticketIssueDescriptionAndMessageVM
        );

        String issueDescription = ticketIssueDescriptionAndMessageVM.getIssueDescription();
        String messageContent = ticketIssueDescriptionAndMessageVM.getMessageContent();

        User user = userService.getUserWithAuthorities().get();

        TicketDTO ticketDTO = new TicketDTO();
        ticketDTO.setIssuedBy(clientService.findByUser_Login(user.getLogin()).get());
        ticketDTO.setIssuedAt(Instant.now());
        ticketDTO.setCode(String.format("T-%s-%s", user.getId(), UUID.randomUUID()));
        ticketDTO.setStatus(TicketStatus.RECEIVED);
        ticketDTO.setIssueDescription(issueDescription);

        //the "no_agent" agent should always exist
        Optional<AgentDTO> noAgent = agentService.findByUser_Login("no_agent");
        if (noAgent.isEmpty()) {
            AdminUserDTO adminUserDTO = new AdminUserDTO();
            adminUserDTO.setLogin("no_agent");
            adminUserDTO.setAuthorities(Set.of(AuthoritiesConstants.USER, AuthoritiesConstants.AGENT));
            adminUserDTO.setLangKey("en");
            adminUserDTO.setActivated(true);

            AgentDTO na = new AgentDTO();
            na.setFirstName("no_agent");
            na.setLastName("no_agent");
            na.setEmail("no_agent@no_agent.com");

            User newUser = userService.registerUser(adminUserDTO, UUID.randomUUID().toString());
            na.setUser(userMapper.toDtoLogin(newUser));
            noAgent = Optional.of(agentService.save(na));
        }

        ticketDTO.setAssignedTo(noAgent.get());

        Ticket ticket = ticketMapper.toEntity(ticketDTO);
        ticket = ticketRepository.save(ticket);
        ticketDTO = ticketMapper.toDto(ticket);

        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setTicket(ticketDTO);
        messageDTO.setContent(messageContent.trim());
        messageDTO.setSentBy(userMapper.toDtoId(user));
        messageDTO.setSentAt(Instant.now());
        messageService.save(messageDTO);

        return ticketDTO;
    }

    @Override
    public Optional<TicketDTO> findOneTicketOfConnectedClient(Long ticketId) {
        log.debug("Request to get Ticket : {} if created by connected Client", ticketId);

        Optional<TicketDTO> optionalTicketDTO = this.findOne(ticketId);
        if (optionalTicketDTO.isEmpty()) return optionalTicketDTO;

        return isIssuedByConnectedUser(ticketId) ? optionalTicketDTO : Optional.empty();
    }

    @Override
    public TicketDTO changeTicketStatusByClient(ChangeTicketStatusVM changeTicketStatusVM) {
        log.debug(
            "Request to update the TicketStatus to {} of the Ticket {}" + " if created by the conected Client.",
            changeTicketStatusVM.getTicketStatus(),
            changeTicketStatusVM.getTicketId()
        );

        //check if the ticket exists
        Long ticketId = changeTicketStatusVM.getTicketId();
        Optional<TicketDTO> optionalTicketDTO = this.findOne(ticketId);
        if (optionalTicketDTO.isEmpty()) throw new BadRequestAlertException(
            String.format("Cannot change status of non-existant Ticket: %d", ticketId),
            "ticket",
            "ticketnotfound"
        );

        //check if the transition is allowed
        TicketStatus oldStatus = optionalTicketDTO.get().getStatus();
        TicketStatus newStatus = changeTicketStatusVM.getTicketStatus();

        Multimap<TicketStatus, TicketStatus> allowedTransitions = ArrayListMultimap.create();
        allowedTransitions.put(TicketStatus.RECEIVED, TicketStatus.CLOSED);
        allowedTransitions.put(TicketStatus.DO_NOT_TREAT, TicketStatus.CLOSED);
        allowedTransitions.put(TicketStatus.TREATED, TicketStatus.CLOSED);
        allowedTransitions.put(TicketStatus.TREATED, TicketStatus.BEING_TREATED);

        if (
            !allowedTransitions.containsKey(oldStatus) || !allowedTransitions.get(oldStatus).contains(newStatus)
        ) throw new BadRequestAlertException(
            String.format("Transition not allowed to clients, from %s to %s on Ticket: %d", oldStatus, newStatus, ticketId),
            "ticket",
            "transitionnotallowed"
        );

        //check if the ticket has been created by the requester
        if (!isIssuedByConnectedUser(ticketId)) throw new BadRequestAlertException(
            String.format("Not allowed action. The requester is not the owner of the Ticket: %d", ticketId),
            "ticket",
            "ticketnotowned"
        );

        //Now we just define what this function should do in a perfect scenario
        TicketDTO ticketDTO = optionalTicketDTO.get();
        ticketDTO.setStatus(newStatus);
        Ticket ticket = ticketMapper.toEntity(ticketDTO);
        return ticketMapper.toDto(ticketRepository.save(ticket));
    }

    @Override
    public Boolean isIssuedBySpecificClient(Long ticketId, Long clienId) {
        Optional<TicketDTO> optionalTicketDTO = this.findOne(ticketId);
        if (optionalTicketDTO.isEmpty()) return false;

        Long ticketOwnerClientId = optionalTicketDTO.get().getIssuedBy().getId();
        return ticketOwnerClientId.equals(clienId);
    }

    @Override
    public Boolean isIssuedBySpecificUser(Long ticketId, Long userId) {
        Optional<TicketDTO> optionalTicketDTO = this.findOne(ticketId);
        if (optionalTicketDTO.isEmpty()) return false;

        Long ticketOwnerClientId = optionalTicketDTO.get().getIssuedBy().getId();
        Long ticketOwnerUserId = clientService.findOne(ticketOwnerClientId).get().getUser().getId();
        return ticketOwnerUserId.equals(userId);
    }

    @Override
    public Boolean isIssuedByConnectedUser(Long ticketId) {
        Long connectedUserId = userService.getUserWithAuthorities().get().getId();
        return isIssuedBySpecificUser(ticketId, connectedUserId);
    }

    @Override
    public Boolean isClientTurn(Long ticketId) {
        //check if the ticket exists
        Optional<TicketDTO> optionalTicketDTO = findOne(ticketId);

        return optionalTicketDTO
            .filter(ticketDTO ->
                Stream.of(TicketStatus.TREATED, TicketStatus.PENDING).anyMatch(allowedStatus -> ticketDTO.getStatus().equals(allowedStatus))
            )
            .isPresent();
    }

    @Override
    public MessageDTO sendMessageByConnectedClient(TicketIdAndMessageContentVM ticketIdAndMessageContentVM) {
        log.debug("Request to send a message by the connected client " + "to an owned by the ticket: {}", ticketIdAndMessageContentVM);

        Long ticketId = ticketIdAndMessageContentVM.getTicketId();
        String messageContent = ticketIdAndMessageContentVM.getMessageContent();

        //check if the ticket exists and issued/owned by connected client
        Optional<TicketDTO> optionalTicketDTO = findOneTicketOfConnectedClient(ticketId);
        if (optionalTicketDTO.isEmpty()) throw new BadRequestAlertException(
            String.format("Ticket %d not found or not issued by requester", ticketId),
            "message",
            "ticketnotfound"
        );

        //check if it is the client turn to send a message
        if (!isClientTurn(ticketId)) throw new BadRequestAlertException(
            String.format("It is not to the connected client's turn to send a message on the ticket: %s", ticketId),
            "message",
            "notclientturn"
        );

        //effectively persist the message
        User user = userService.getUserWithAuthorities().get();
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setContent(messageContent);
        messageDTO.setSentAt(Instant.now());
        messageDTO.setSentBy(userMapper.toDtoLogin(user));
        messageDTO.setTicket(optionalTicketDTO.get());

        TicketDTO ticketDTO = optionalTicketDTO.get();
        ticketDTO.setStatus(TicketStatus.BEING_TREATED);

        save(ticketDTO);
        return messageService.save(messageDTO);
    }

    @Override
    public List<TicketDTO> findAllUnassigned() {
        log.debug("Request to get all the unassigned tickets");
        return ticketRepository
            .findAll()
            .stream()
            .filter(ticket -> ticket.getStatus().equals(TicketStatus.RECEIVED))
            .map(ticketMapper::toDto)
            .collect(Collectors.toList());
    }
}
