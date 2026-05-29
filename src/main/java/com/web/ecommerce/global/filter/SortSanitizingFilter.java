package com.web.ecommerce.global.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class SortSanitizingFilter extends OncePerRequestFilter {

    private static final Set<String> INVALID_VALUES = Set.of(
        "asc", "desc", "ASC", "DESC", "null", "NULL"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        String[] sortParams = request.getParameterValues("sort");
        if (sortParams == null) {
            filterChain.doFilter(request, response);
            return;
        }
        String[] sanitized = Arrays.stream(sortParams)
                .map(s -> s.replaceAll("[\\[\\]\"']", "").trim())
                .filter(s -> !s.isBlank() && !INVALID_VALUES.contains(s))
                .toArray(String[]::new);
        filterChain.doFilter(new SanitizedRequest(request, sanitized), response);
    }

    private static class SanitizedRequest extends HttpServletRequestWrapper {
        private final String[] sanitizedSort;

        SanitizedRequest(HttpServletRequest request, String[] sanitizedSort) {
            super(request);
            this.sanitizedSort = sanitizedSort;
        }

        @Override
        public String getParameter(String name) {
            if ("sort".equals(name)) {
                return sanitizedSort.length > 0 ? sanitizedSort[0] : null;
            }
            return super.getParameter(name);
        }

        @Override
        public String[] getParameterValues(String name) {
            if ("sort".equals(name)) {
                return sanitizedSort.length > 0 ? sanitizedSort : null;
            }
            return super.getParameterValues(name);
        }

        @Override
        public Map<String, String[]> getParameterMap() {
            Map<String, String[]> params = new HashMap<>(super.getParameterMap());
            if (sanitizedSort.length > 0) {
                params.put("sort", sanitizedSort);
            } else {
                params.remove("sort");
            }
            return params;
        }
    }
}
