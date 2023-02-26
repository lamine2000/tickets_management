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
import sn.trivial.ticket.domain.Client;
import sn.trivial.ticket.domain.User;
import sn.trivial.ticket.repository.ClientRepository;
import sn.trivial.ticket.security.AuthoritiesConstants;
import sn.trivial.ticket.service.ClientService;
import sn.trivial.ticket.service.MailService;
import sn.trivial.ticket.service.UserService;
import sn.trivial.ticket.service.dto.AdminUserDTO;
import sn.trivial.ticket.service.dto.ClientDTO;
import sn.trivial.ticket.service.dto.UserDTO;
import sn.trivial.ticket.service.mapper.ClientMapper;
import sn.trivial.ticket.service.mapper.UserMapper;

/**
 * Service Implementation for managing {@link Client}.
 */
@Service
@Transactional
public class ClientServiceImpl implements ClientService {

    private final Logger log = LoggerFactory.getLogger(ClientServiceImpl.class);

    private final ClientRepository clientRepository;

    private final ClientMapper clientMapper;

    private final UserMapper userMapper;

    private final UserService userService;

    private final MailService mailService;

    public ClientServiceImpl(
        ClientRepository clientRepository,
        ClientMapper clientMapper,
        UserMapper userMapper,
        UserService userService,
        MailService mailService
    ) {
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
        this.userMapper = userMapper;
        this.userService = userService;
        this.mailService = mailService;
    }

    @Override
    public ClientDTO save(ClientDTO clientDTO) {
        log.debug("Request to save Client : {}", clientDTO);
        Client client = clientMapper.toEntity(clientDTO);
        client = clientRepository.save(client);
        return clientMapper.toDto(client);
    }

    @Override
    public ClientDTO update(ClientDTO clientDTO) {
        log.debug("Request to update Client : {}", clientDTO);
        Client client = clientMapper.toEntity(clientDTO);
        client = clientRepository.save(client);
        return clientMapper.toDto(client);
    }

    @Override
    public Optional<ClientDTO> partialUpdate(ClientDTO clientDTO) {
        log.debug("Request to partially update Client : {}", clientDTO);

        return clientRepository
            .findById(clientDTO.getId())
            .map(existingClient -> {
                clientMapper.partialUpdate(existingClient, clientDTO);

                return existingClient;
            })
            .map(clientRepository::save)
            .map(clientMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ClientDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Clients");
        return clientRepository.findAll(pageable).map(clientMapper::toDto);
    }

    public Page<ClientDTO> findAllWithEagerRelationships(Pageable pageable) {
        return clientRepository.findAllWithEagerRelationships(pageable).map(clientMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ClientDTO> findOne(Long id) {
        log.debug("Request to get Client : {}", id);
        return clientRepository.findOneWithEagerRelationships(id).map(clientMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Client : {}", id);
        clientRepository.deleteById(id);
    }

    @Override
    public Optional<ClientDTO> findByUser_Login(String login) {
        log.debug("Request the Client associated with user : {}", login);
        Optional<Client> optionalClient = clientRepository.findByUser_Login(login);

        return optionalClient.map(clientMapper::toDto);
    }

    @Override
    public ClientDTO register(ClientDTO clientDTO) {
        log.debug("Request to register Client : {}", clientDTO);

        //save the user and then save the client
        AdminUserDTO adminUserDTO = new AdminUserDTO();
        UserDTO userDTO = clientDTO.getUser();
        adminUserDTO.setFirstName(clientDTO.getFirstName());
        adminUserDTO.setLastName(clientDTO.getLastName());
        adminUserDTO.setEmail(clientDTO.getEmail());
        adminUserDTO.setLogin(userDTO.getLogin());
        adminUserDTO.setCreatedDate(Instant.now());
        adminUserDTO.setAuthorities(Set.of("ROLE_CLIENT", "ROLE_USER"));
        adminUserDTO.setLangKey("fr");

        User newUser = userService.createUser(adminUserDTO);
        mailService.sendCreationEmail(newUser);
        clientDTO.setUser(userMapper.toDtoLogin(newUser));
        return save(clientDTO);
    }
}
