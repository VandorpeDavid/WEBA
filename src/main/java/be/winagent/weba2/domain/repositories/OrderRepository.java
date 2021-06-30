package be.winagent.weba2.domain.repositories;

import be.winagent.weba2.domain.models.Order;
import be.winagent.weba2.domain.models.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

public interface OrderRepository extends CrudRepository<Order, UUID> {
    Page<Order> findAllByEventIdOrderByCreated(UUID eventId, Pageable pageable);
    List<Order> findAllByEventIdAndStatusIsIn(UUID eventId, Iterable<OrderStatus> statuses);

    int countAllByEventIdAndStatusIsInAndCreatedIsBefore(UUID eventId, Iterable<OrderStatus> statuses, ZonedDateTime created);
}
