package dev.lesechko.proselyte.controller;

import dev.lesechko.proselyte.service.EventService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(
        name = "EventRestController",
        urlPatterns = {"/api/v1/events/*"}
)
public class EventRestController extends HttpServlet {
    private final String ENCODING = "UTF-8";
    private final String CONTENT_TYPE = "application/json; charset=" + ENCODING;

    private EventService eventService = new EventService();

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
        //TODO: implement GET /events + /events/{id}
    }
}
