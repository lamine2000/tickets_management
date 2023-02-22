package sn.trivial.ticket.repository;

import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.trivial.ticket.domain.Message;

/**
 * Spring Data JPA repository for the Message entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MessageRepository extends JpaRepository<Message, Long>, JpaSpecificationExecutor<Message> {
    @Query("select message from Message message where message.sentBy.login = ?#{principal.username}")
    List<Message> findBySentByIsCurrentUser();
}
