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
//        if (pathInfo == null || pathInfo.isBlank()) {
//            return null;
//        }
//        String[] pathParams = pathInfo.split("/");
//        if (pathParams.length != 2) {
//            return null;
//        }
//        try {
//            return Integer.valueOf(pathParams[1]);
//        } catch (NumberFormatException e) {
//            return null;
//        }
        try {
            String[] pathParams = pathInfo.split("/");
            return Integer.valueOf(pathParams[1]);
        } catch (Exception e) {
            e.printStackTrace();
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
        resp.setCharacterEncoding(ENCODING);
        resp.setContentType(CONTENT_TYPE);
        List<User> responseData = new ArrayList<>();
        String pathInfo = req.getPathInfo();

        if (pathInfo == null || "/".equals(pathInfo)) {
            // POST /users is the only point to create users
            User user = new ObjectMapper().readValue(req.getInputStream(), User.class);
            if (user != null) {
                user.setId(null);
                User savedUser = userService.save(user);
                if (savedUser != null) {
                    responseData.add(savedUser);
                    resp.setStatus(201);
                } else {
                    // Something went wrong while saving to DB. Our side.
                    responseData = null;
                    resp.setStatus(503);
                }
            }
        } else {
            // Excessive path/params
            responseData = null;
            resp.setStatus(400);
        }
        String jsonResponse = new ObjectMapper().writeValueAsString(responseData);
        PrintWriter message = resp.getWriter();
        message.write(jsonResponse);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // /api/v1/user/{id} - set STATUS DELETED for user by ID
        resp.setCharacterEncoding(ENCODING);
        resp.setContentType(CONTENT_TYPE);
        Boolean responseData = false;
        String pathInfo = req.getPathInfo();

        if (pathInfo != null || "/".equals(pathInfo)) {
            // No iD in request
            responseData = null;
            resp.setStatus(400);
        } else {
            Integer id = extractIdFromPath(pathInfo);
            if (userService.deleteById(id)) {
                responseData = true;
                resp.setStatus(200);
            } else {
                responseData = false;
                resp.setStatus(200);
            }
        }
        String jsonResponse = new ObjectMapper().writeValueAsString(responseData);
        PrintWriter message = resp.getWriter();
        message.write(jsonResponse);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // /api/v1/users - updates user by processing incoming JSON
        resp.setCharacterEncoding(ENCODING);
        resp.setContentType(CONTENT_TYPE);
        List<User> responseData = new ArrayList<>();
        String pathInfo = req.getPathInfo();

        if (pathInfo == null || "/".equals(pathInfo)) {
            // PUT /users is the only point to upload JSON with updates
            User user = new ObjectMapper().readValue(req.getInputStream(), User.class);
            User updatedUser = userService.update(user);
            if (updatedUser != null) {
                responseData.add(updatedUser);
                resp.setStatus(200);
            } else {
                // Something went wrong while updating.
                // Our side or wrong ID.
                // 4xx or 5xx or add new if?
                responseData = null;
                resp.setStatus(400);
            }
        } else {
            // Excessive path/params
            responseData = null;
            resp.setStatus(400);
        }
        String jsonResponse = new ObjectMapper().writeValueAsString(responseData);
        PrintWriter message = resp.getWriter();
        message.write(jsonResponse);
    }
}
