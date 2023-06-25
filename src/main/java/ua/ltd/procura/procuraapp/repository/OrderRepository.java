package ua.ltd.procura.procuraapp.repository;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ua.ltd.procura.procuraapp.entity.Order;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {
    Optional<Order> findById(long id);

    Optional<Order> findByInternalId(String internalId);

    static Specification<Order> hasAnyFieldContains(String pattern) {
        String likePattern = "%" + pattern.toLowerCase() + "%";

        return (order, cq, cb) ->
                cb.or(
                        cb.like(cb.lower(order.get("internalId")), likePattern),
                        cb.like(cb.lower(order.get("name")), likePattern),
                        cb.like(cb.lower(order.get("clientName")), likePattern)
                );
    }

}
