package dev.lesechko.proselyte.controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.lesechko.proselyte.model.File;
import dev.lesechko.proselyte.service.FileService;

public class FileController extends HttpServlet {
    private final FileService fileService = new FileService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        Если айди не указан, то гетАлл. Если указан - гетБайАйди.
//        fileService.getById(Integer id);
//        fileService.getAll();
//        И так далее по разным видам запросов обращаемся к разным методам.

        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json; charset=UTF-8");
        resp.setStatus(200);
        PrintWriter messageWriter = resp.getWriter();
//        messageWriter.println("<h1>TEST</h1>");
//        messageWriter.println(req.getRequestURI());
//        messageWriter.println(req.getRequestURI().split("/"));
//        messageWriter.println(Arrays.toString(req.getRequestURI().split("/")));
        File file = new File("Readme.txt", "/path/to/file");
        String jsonResponse = new ObjectMapper().writeValueAsString(file);
        messageWriter.write(jsonResponse);
    }
}
