package ua.ltd.procura.procuraapp.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;

public interface PaginationService<T> {

    void paginate(Model model, Page<T> content);

    Pageable getPaging(HttpServletRequest request, HttpServletResponse response);
}
