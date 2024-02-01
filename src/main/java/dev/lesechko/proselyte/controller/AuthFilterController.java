package dev.lesechko.proselyte.controller;

import java.io.IOException;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dev.lesechko.proselyte.service.UserService;


@WebFilter(
        filterName = "AuthFilterController",
        servletNames = {"FileRestController", "EventRestController"}
)
public class AuthFilterController implements Filter {
    private final UserService userService = new UserService();

    private Integer sanitizeUserId(String userIdFromHeader) {
        try {
            return Integer.valueOf(userIdFromHeader);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        final String AUTH_ERROR_MESSAGE = "Unauthorized access. Check \"User-Id\" header.";

        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        Integer userId = sanitizeUserId(req.getHeader("User-Id"));
        boolean userNotExists = (userService.getById(userId) == null);

        if (userId == null || userNotExists) {
            resp.sendError(401, AUTH_ERROR_MESSAGE);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}

