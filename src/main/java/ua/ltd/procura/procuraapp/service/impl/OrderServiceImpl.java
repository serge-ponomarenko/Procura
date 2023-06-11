package ua.ltd.procura.procuraapp.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.ltd.procura.procuraapp.dto.OrderDto;
import ua.ltd.procura.procuraapp.entity.Order;
import ua.ltd.procura.procuraapp.repository.OrderRepository;
import ua.ltd.procura.procuraapp.service.OrderService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Override
    public void saveOrder(OrderDto orderDto) {
        Order order = Order.builder()
                .internalId(orderDto.getInternalId())
                .name(orderDto.getName())
                .clientName(orderDto.getClientName())
                .price(orderDto.getPrice())
                .finishDate(orderDto.getFinishDate())
                .build();
        orderRepository.save(order);
    }

    @Override
    public Optional<Order> findById(long id) {
        return orderRepository.findById(id);
    }

    @Override
    public Optional<Order> findByInternalId(String internalId) {
        return orderRepository.findByInternalId(internalId);
    }

    @Override
    public Page<OrderDto> findAllOrders(Pageable pageable) {
        Page<Order> orders = orderRepository.findAll(pageable);

        return new PageImpl<>(orders.getContent().stream()
                .map(this::convertEntityToDto)
                .toList(), pageable, orders.getTotalElements());
    }

    private OrderDto convertEntityToDto(Order order) {
        return OrderDto.builder()
                .id(order.getId())
                .internalId(order.getInternalId())
                .name(order.getName())
                .clientName(order.getClientName())
                .price(order.getPrice())
                .finishDate(order.getFinishDate())
                .build();
    }

}
