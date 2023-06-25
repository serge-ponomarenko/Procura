package ua.ltd.procura.procuraapp.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.ltd.procura.procuraapp.constants.UriStorage;
import ua.ltd.procura.procuraapp.dto.OrderDto;
import ua.ltd.procura.procuraapp.entity.Order;
import ua.ltd.procura.procuraapp.service.AppLocaleService;
import ua.ltd.procura.procuraapp.service.OrderService;
import ua.ltd.procura.procuraapp.service.PaginationService;

import java.util.Objects;

@Controller
@RequiredArgsConstructor
public class OrdersController {

    private final OrderService orderService;
    private final AppLocaleService appLocaleService;
    private final PaginationService<OrderDto> paginationService;

    @GetMapping(UriStorage.ORDERS)
    public String orders(@RequestParam(defaultValue = "") String find,
                         Model model,
                         HttpServletRequest request,
                         HttpServletResponse response
    ) {
        Pageable paging = paginationService.getPaging(request, response);
        //Page<OrderDto> orders = orderService.findAllOrders(paging);
        Page<OrderDto> orders = orderService.findByInternalIdLikeOrNameLikeOrClientNameLike(find, paging);
        paginationService.paginate(model, orders);

        model.addAttribute("find", find);
        model.addAttribute("orders", orders.getContent());
        return "orders";
    }

    @PostMapping(UriStorage.ORDERS)
    public String addOrder(@Valid @ModelAttribute("order") OrderDto order,
                         BindingResult result,
                         Model model) {
        orderService.findByInternalId(order.getInternalId())
                .ifPresent(
                        o -> {
                            if (!Objects.equals(o.getId(), order.getId())) {
                                result.rejectValue("internalId", "400", "There is already an order presented with that internal Id");
                            }
                        });
        if (result.hasErrors()) {
            model.addAttribute("action", "new");
            model.addAttribute("order", order);
            return "new_order";
        }

        orderService.saveOrder(order);
        return "redirect:" + UriStorage.ORDERS + "?success_new";
    }

    @GetMapping(UriStorage.ORDERS_NEW)
    public String newOrder(Model model) {
        OrderDto order = new OrderDto();
        model.addAttribute("order", order);
        model.addAttribute("action", "new");
        return "new_order";
    }

    @GetMapping(UriStorage.ORDERS + "/delete/{id}")
    public String deleteOrder(@PathVariable("id") long orderId) {
        orderService.deleteById(orderId);
        return "redirect:" + UriStorage.ORDERS + "?success_delete";
    }

    @GetMapping(UriStorage.ORDERS + "/edit/{id}")
    public String editOrder(@PathVariable("id") long orderId, Model model) {
        OrderDto order = orderService.findById(orderId).orElseThrow();
        model.addAttribute("order", order);
        model.addAttribute("action", "edit");
        return "new_order";
    }

}
