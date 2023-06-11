package ua.ltd.procura.procuraapp.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.ltd.procura.procuraapp.dto.OrderDto;
import ua.ltd.procura.procuraapp.entity.Order;

import java.util.Optional;

public interface OrderService {
    void saveOrder(OrderDto orderDto);
    Optional<Order> findById(long id);
    Optional<Order> findByInternalId(String internalId);
    Page<OrderDto> findAllOrders(Pageable pageable);

}
