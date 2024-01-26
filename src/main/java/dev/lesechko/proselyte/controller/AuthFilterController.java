package dev.lesechko.proselyte.controller;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(
        filterName = "AuthFilterController",
        servletNames = {"FileRestController"}
//        urlPatterns = {"/api/v1/files/*", "/api/v1/events/*"}
)
public class AuthFilterController implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        final String AUTH_ERROR_MESSAGE = "Unauthorized access. Check \"User-Name\" header.";

        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;

        String userName = req.getHeader("User-Name");
        if (userName == null || userName.isBlank()) {
            resp.sendError(401, AUTH_ERROR_MESSAGE);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}

