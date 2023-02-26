package sn.trivial.ticket.service.impl;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.trivial.ticket.domain.Agent;
import sn.trivial.ticket.domain.User;
import sn.trivial.ticket.repository.AgentRepository;
import sn.trivial.ticket.security.AuthoritiesConstants;
import sn.trivial.ticket.service.AgentService;
import sn.trivial.ticket.service.MailService;
import sn.trivial.ticket.service.UserService;
import sn.trivial.ticket.service.dto.AdminUserDTO;
import sn.trivial.ticket.service.dto.AgentDTO;
import sn.trivial.ticket.service.dto.UserDTO;
import sn.trivial.ticket.service.mapper.AgentMapper;
import sn.trivial.ticket.service.mapper.UserMapper;

/**
 * Service Implementation for managing {@link Agent}.
 */
@Service
@Transactional
public class AgentServiceImpl implements AgentService {

    private final Logger log = LoggerFactory.getLogger(AgentServiceImpl.class);

    private final AgentRepository agentRepository;

    private final AgentMapper agentMapper;

    private final UserService userService;

    private final MailService mailService;

    private final UserMapper userMapper;

    public AgentServiceImpl(
        AgentRepository agentRepository,
        AgentMapper agentMapper,
        UserService userService,
        MailService mailService,
        UserMapper userMapper
    ) {
        this.agentRepository = agentRepository;
        this.agentMapper = agentMapper;
        this.userService = userService;
        this.mailService = mailService;
        this.userMapper = userMapper;
    }

    @Override
    public AgentDTO save(AgentDTO agentDTO) {
        log.debug("Request to save Agent : {}", agentDTO);
        Agent agent = agentMapper.toEntity(agentDTO);
        agent = agentRepository.save(agent);
        return agentMapper.toDto(agent);
    }

    @Override
    public AgentDTO update(AgentDTO agentDTO) {
        log.debug("Request to update Agent : {}", agentDTO);
        Agent agent = agentMapper.toEntity(agentDTO);
        agent = agentRepository.save(agent);
        return agentMapper.toDto(agent);
    }

    @Override
    public Optional<AgentDTO> partialUpdate(AgentDTO agentDTO) {
        log.debug("Request to partially update Agent : {}", agentDTO);

        return agentRepository
            .findById(agentDTO.getId())
            .map(existingAgent -> {
                agentMapper.partialUpdate(existingAgent, agentDTO);

                return existingAgent;
            })
            .map(agentRepository::save)
            .map(agentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AgentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Agents");
        return agentRepository.findAll(pageable).map(agentMapper::toDto);
    }

    public Page<AgentDTO> findAllWithEagerRelationships(Pageable pageable) {
        return agentRepository.findAllWithEagerRelationships(pageable).map(agentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AgentDTO> findOne(Long id) {
        log.debug("Request to get Agent : {}", id);
        return agentRepository.findOneWithEagerRelationships(id).map(agentMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Agent : {}", id);
        agentRepository.deleteById(id);
    }

    @Override
    public Optional<AgentDTO> findByUser_Login(String login) {
        log.debug("Request to get Agent of login: {}", login);
        return agentRepository.findByUser_Login(login).map(agentMapper::toDto);
    }

    @Override
    public AgentDTO register(AgentDTO agentDTO) {
        log.debug("Request to register Client : {}", agentDTO);

        //save the user and then save the client
        AdminUserDTO adminUserDTO = new AdminUserDTO();
        UserDTO userDTO = agentDTO.getUser();
        adminUserDTO.setFirstName(agentDTO.getFirstName());
        adminUserDTO.setLastName(agentDTO.getLastName());
        adminUserDTO.setEmail(agentDTO.getEmail());
        adminUserDTO.setLogin(userDTO.getLogin());
        adminUserDTO.setCreatedDate(Instant.now());
        adminUserDTO.setAuthorities(Set.of(AuthoritiesConstants.USER, AuthoritiesConstants.AGENT));
        adminUserDTO.setLangKey("fr");

        User newUser = userService.createUser(adminUserDTO);
        mailService.sendCreationEmail(newUser);
        agentDTO.setUser(userMapper.toDtoLogin(newUser));
        return save(agentDTO);
    }
}
