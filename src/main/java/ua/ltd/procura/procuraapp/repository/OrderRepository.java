package ua.ltd.procura.procuraapp.repository;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.ltd.procura.procuraapp.entity.Order;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findById(long id);
    Optional<Order> findByInternalId(String internalId);
    @Override
    Page<Order> findAll(@NotNull Pageable pageable);
}
