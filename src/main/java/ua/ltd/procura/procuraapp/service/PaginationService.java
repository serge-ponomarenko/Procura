package ua.ltd.procura.procuraapp.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;

public interface PaginationService<T> {

    void paginate(Page<T> content, Model model, Pageable paging);
}
