package sn.trivial.ticket.repository;

import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.trivial.ticket.domain.Ticket;
import sn.trivial.ticket.domain.enumeration.TicketStatus;

/**
 * Spring Data JPA repository for the Ticket entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long>, JpaSpecificationExecutor<Ticket> {
    List<Ticket> findByIssuedBy_Id(Long clientId);

    List<Ticket> findAllByStatus(TicketStatus ticketStatus);
}
