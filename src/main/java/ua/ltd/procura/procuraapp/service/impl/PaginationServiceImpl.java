package ua.ltd.procura.procuraapp.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import ua.ltd.procura.procuraapp.service.PaginationService;

@Service
public class PaginationServiceImpl<T> implements PaginationService<T> {
    public static final int NUMBER_VISIBLE_PAGES = 2;
    @Override
    public void paginate(Page<T> content, Model model, Pageable paging) {
        model.addAttribute("currentPage", content.getNumber() + 1);
        model.addAttribute("totalItems", (int) content.getTotalElements());
        model.addAttribute("totalPages", content.getTotalPages());
        model.addAttribute("size", paging.getPageSize());

        model.addAttribute("numVisiblePages", NUMBER_VISIBLE_PAGES);
        model.addAttribute("sortBy", paging.getSort().get().map(Sort.Order::getProperty).findFirst().orElse("id"));
        model.addAttribute("sortDirection", paging.getSort().get().map(Sort.Order::getDirection).findFirst().orElse(Sort.Direction.ASC).toString());
    }
}
