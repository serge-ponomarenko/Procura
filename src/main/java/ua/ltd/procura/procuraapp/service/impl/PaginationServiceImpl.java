package ua.ltd.procura.procuraapp.service.impl;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import ua.ltd.procura.procuraapp.service.PaginationService;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PaginationServiceImpl<T> implements PaginationService<T> {
    private Pageable paging;
    public static final int NUMBER_VISIBLE_PAGES = 2;
    @Override
    public void paginate(Model model, Page<T> content) {

        model.addAttribute("currentPage", content.getNumber() + 1);
        model.addAttribute("totalItems", (int) content.getTotalElements());
        model.addAttribute("totalPages", content.getTotalPages());
        model.addAttribute("size", paging.getPageSize());

        model.addAttribute("numVisiblePages", NUMBER_VISIBLE_PAGES);
        model.addAttribute("sortBy", paging.getSort().get().map(Sort.Order::getProperty).findFirst().orElse("id"));
        model.addAttribute("sortDirection", paging.getSort().get().map(Sort.Order::getDirection).findFirst().orElse(Sort.Direction.ASC).toString());
    }

    @Override
    public Pageable getPaging(HttpServletRequest request, HttpServletResponse response) {
        PagingParameters pagingParameters = new PagingParameters(request, response);
        Sort sorting = Sort.by(Sort.Direction.fromString(pagingParameters.getSortDirection()), pagingParameters.getSortBy());
        paging = PageRequest.of(pagingParameters.getPage() - 1, pagingParameters.getSize(), sorting);
        return paging;
    }

    @Getter
    private static class PagingParameters {

        private final String pagingName;
        private final int page;
        private final int size;
        private final String sortBy;
        private final String sortDirection;

        public static final int COOKIE_LIFETIME = 7 * 24 * 60 * 60; // 7 days


        public PagingParameters(HttpServletRequest request, HttpServletResponse response) {
            pagingName = "paging" + request.getRequestURI().replace("/", "_");

            Map<String, String> cookies = Arrays.stream(request.getCookies())
                    .collect(Collectors.toMap(Cookie::getName, Cookie::getValue));

            String pageParameter = retrieveParameter(request, cookies, "page", "1");
            String sizeParameter = retrieveParameter(request, cookies, "size", "5");
            String sortByParameter = retrieveParameter(request, cookies, "sortBy", "id");
            String sortDirectionParameter = retrieveParameter(request, cookies, "sortDirection", "ASC");

            page = parseIntWithDefault(pageParameter, 1);
            size = parseIntWithDefault(sizeParameter, 5);
            sortBy = sortByParameter;
            sortDirection = sortDirectionParameter;

            updateCookies(response);
        }

        private String retrieveParameter(HttpServletRequest request, Map<String, String> cookies, String parameterName, String defaultValue) {
            return Optional.ofNullable(request.getParameter(parameterName))
                    .or(() -> Optional.ofNullable(cookies.get(getCookieParameterName(parameterName))))
                    .orElse(defaultValue);
        }

        private int parseIntWithDefault(String parameterToParse, int defaultValue) {
            try {
                return Integer.parseInt(parameterToParse);
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        }

        private String getCookieParameterName(String name) {
            return pagingName + "_" + name;
        }

        private void updateCookies(HttpServletResponse response) {
            Cookie cookie = new Cookie(getCookieParameterName("page"), String.valueOf(page));
            cookie.setMaxAge(COOKIE_LIFETIME);
            response.addCookie(cookie);

            cookie = new Cookie(getCookieParameterName("size"), String.valueOf(size));
            cookie.setMaxAge(COOKIE_LIFETIME);
            response.addCookie(cookie);

            cookie = new Cookie(getCookieParameterName("sortBy"), sortBy);
            cookie.setMaxAge(COOKIE_LIFETIME);
            response.addCookie(cookie);

            cookie = new Cookie(getCookieParameterName("sortDirection"), sortDirection);
            cookie.setMaxAge(COOKIE_LIFETIME);
            response.addCookie(cookie);
        }
    }
}
