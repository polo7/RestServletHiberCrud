package dev.lesechko.proselyte.controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.lesechko.proselyte.model.File;
import dev.lesechko.proselyte.service.FileService;

public class FileController extends HttpServlet {
    private final FileService fileService = new FileService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<File> responseData = new ArrayList<>();
        String requestQuery = req.getQueryString();
        Integer id = 777;
        if (requestQuery == null || requestQuery.isEmpty()) {
            System.out.println("No query. Get list of all files");
            List<File> files = fileService.getAll();
            if (files != null && !files.isEmpty()) {
                responseData.addAll(files);
            }
        } else {
            System.out.println("we have a query and it needs to be processed");
            File file = fileService.getById(id);
//            Если такого айди нет, то надо вернуть нулл. Поставить это в респонсДата.
            if (file != null) {
                responseData.add(file);
            } else {
                responseData = null;
            }
        }
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json; charset=UTF-8");
        resp.setStatus(200);
        System.out.println(req.getParameter("name"));
        System.out.println(req.getParameter("path"));
        System.out.println(req.getParameter("user"));
        System.out.println(req.getRequestURI());
        System.out.println(req.getRequestURL());
        System.out.println(req.getQueryString());
//        messageWriter.println("<h1>TEST</h1>");
//        messageWriter.println(req.getRequestURI());
//        messageWriter.println(req.getRequestURI().split("/"));
//        messageWriter.println(Arrays.toString(req.getRequestURI().split("/")));

//        File file = new File(777, "Readme.txt", "/path/to/file");
        File file = new File("Readme.txt", "/path/to/file");
//        String jsonResponse = new ObjectMapper().writeValueAsString(file);

        List<File> tmp = null;
        String jsonResponse = new ObjectMapper().writeValueAsString(tmp);
        PrintWriter messageWriter = resp.getWriter();
        messageWriter.write(jsonResponse);
    }
}
