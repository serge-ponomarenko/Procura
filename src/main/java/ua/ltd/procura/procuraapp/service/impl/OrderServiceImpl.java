package ua.ltd.procura.procuraapp.service.impl;

import jakarta.validation.constraints.NotNull;
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

import static ua.ltd.procura.procuraapp.repository.OrderRepository.hasAnyFieldContains;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Override
    public void saveOrder(OrderDto orderDto) {
        Order order;
        if (orderDto.getId() == null) {
            order = Order.builder()
                    .internalId(orderDto.getInternalId())
                    .name(orderDto.getName())
                    .clientName(orderDto.getClientName())
                    .price(orderDto.getPrice())
                    .finishDate(orderDto.getFinishDate())
                    .build();
        } else {
            order = orderRepository.findById(orderDto.getId()).orElseThrow(); //todo
            updateFields(order, orderDto);
        }
        orderRepository.save(order);
    }

    private void updateFields(Order order, OrderDto orderDto) {
        order.setInternalId(orderDto.getInternalId());
        order.setName(orderDto.getName());
        order.setClientName(orderDto.getClientName());
        order.setPrice(orderDto.getPrice());
        order.setFinishDate(orderDto.getFinishDate());
    }

    @Override
    public Optional<OrderDto> findById(long id) {
        return orderRepository.findById(id).map(this::convertEntityToDto);
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

    @Override
    public void deleteById(Long id) {
        orderRepository.deleteById(id);
    }

    @Override
    public Page<OrderDto> findByInternalIdLikeOrNameLikeOrClientNameLike(String str, @NotNull Pageable pageable) {
        Page<Order> orders = orderRepository.findAll(hasAnyFieldContains(str), pageable);

        return new PageImpl<>(orders.getContent().stream()
                .map(this::convertEntityToDto)
                .toList(), pageable, orders.getTotalElements());
    }
}
