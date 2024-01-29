package dev.lesechko.proselyte.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.lesechko.proselyte.model.Event;
import dev.lesechko.proselyte.service.EventService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@WebServlet(
        name = "EventRestController",
        urlPatterns = {"/api/v1/events/*"}
)
public class EventRestController extends HttpServlet {
    private final String ENCODING = "UTF-8";
    private final String CONTENT_TYPE = "application/json; charset=" + ENCODING;

    private EventService eventService = new EventService();

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
        //TODO: implement GET /events + /events/{id}
        // /api/v1/events - display all events
        // /api/v1/events/{id} - get JSON-event by ID
        resp.setCharacterEncoding(ENCODING);
        resp.setContentType(CONTENT_TYPE);
        List<Event> responseData = new ArrayList<>();
        String pathInfo = req.getPathInfo();

        if (pathInfo == null || "/".equals(pathInfo)) {
            // Just /events w/o parameters - display all events
            List<Event> events = eventService.getAll();
            if (events != null || !events.isEmpty()) {
                responseData.addAll(events);
            }
            resp.setStatus(200);
        } else {
            Integer id = extractIdFromPath(pathInfo);
            Event event = eventService.getById(id);
            if (event != null) {
                responseData.add(event);
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
        resp.setStatus(405);
        PrintWriter message = resp.getWriter();
        message.write("Method is not allowed for /api/v1/events");
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // /api/v1/event/{id} - set STATUS DELETED for event by ID
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
            if (eventService.deleteById(id)) {
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
}
