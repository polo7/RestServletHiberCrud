package dev.lesechko.proselyte.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import dev.lesechko.proselyte.model.User;
import dev.lesechko.proselyte.service.UserService;


@WebServlet(
        name = "UserRestController",
        urlPatterns = {"/api/v1/users/*"}
)
public class UserRestController extends HttpServlet {
    private final String ENCODING = "UTF-8";
    private final String CONTENT_TYPE = "application/json; charset=" + ENCODING;

    private UserService userService = new UserService();

    private Integer extractIdFromPath(String pathInfo) {
        if (pathInfo == null || pathInfo.isBlank()) {
            return null;
        }
        String[] pathParams = pathInfo.split("/");
        if (pathParams.length != 2) {
            return null;
        }
        try {
            return Integer.valueOf(pathParams[1]);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // /api/v1/users - display all users
        // /api/v1/users/{id} - get JSON-user by ID
        resp.setCharacterEncoding(ENCODING);
        resp.setContentType(CONTENT_TYPE);
        List<User> responseData = new ArrayList<>();
        String pathInfo = req.getPathInfo();

        if (pathInfo == null || "/".equals(pathInfo)) {
            // Just "/users" w/o parameters - display all users
            List<User> users = userService.getAll();
            if (users != null && !users.isEmpty()) {
                responseData.addAll(users);
            }
            resp.setStatus(200);
        } else {
            Integer id = extractIdFromPath(pathInfo);
            User user = userService.getById(id);
            if (user != null) {
                responseData.add(user);
                resp.setStatus(200);
            } else {
                // DB has no entry with this ID
                responseData = null;
                resp.setStatus(400);
            }
        }
        String jsonResponse = new ObjectMapper().writeValueAsString(responseData);
        PrintWriter message = resp.getWriter();
        message.write(jsonResponse);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPut(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doDelete(req, resp);
    }
}
