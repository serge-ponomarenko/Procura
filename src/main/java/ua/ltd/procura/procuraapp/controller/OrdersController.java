package ua.ltd.procura.procuraapp.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.ltd.procura.procuraapp.constants.UriStorage;
import ua.ltd.procura.procuraapp.dto.OrderDto;
import ua.ltd.procura.procuraapp.service.AppLocaleService;
import ua.ltd.procura.procuraapp.service.OrderService;
import ua.ltd.procura.procuraapp.service.PaginationService;

@Controller
@RequiredArgsConstructor
public class OrdersController {

    private final OrderService orderService;
    private final AppLocaleService appLocaleService;
    private final PaginationService<OrderDto> paginationService;

    @GetMapping(UriStorage.ORDERS)
    public String orders(Model model,
                         @RequestParam(defaultValue = "1") int page,
                         @RequestParam(defaultValue = "5") int size,
                         @RequestParam(defaultValue = "id") String sortBy,
                         @RequestParam(defaultValue = "ASC") String sortDirection) {

        Sort sorting = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable paging = PageRequest.of(page - 1, size, sorting);
        Page<OrderDto> orders = orderService.findAllOrders(paging);

        paginationService.paginate(orders, model, paging);

        model.addAttribute("orders", orders.getContent());
        return "orders";
    }

    @PostMapping(UriStorage.ORDERS)
    public String addOrder(@Valid @ModelAttribute("order") OrderDto order,
                         BindingResult result,
                         Model model) {
        orderService.findByInternalId(order.getInternalId())
                .ifPresent(o -> result.rejectValue("internalId", "400", "There is already an order presented with that internal Id"));
        if (result.hasErrors()) {
            model.addAttribute("order", order);
            return "new_order";
        }

        orderService.saveOrder(order);
        return "redirect:" + UriStorage.ORDERS + "?success";
    }

    @GetMapping(UriStorage.ORDERS_NEW)
    public String newOrder(Model model) {
        OrderDto order = new OrderDto();
        model.addAttribute("order", order);
        return "new_order";
    }

}
