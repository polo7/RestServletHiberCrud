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
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json; charset=UTF-8");
        List<File> responseData = new ArrayList<>();
        String requestQuery = req.getQueryString();

        if (requestQuery == null || requestQuery.isBlank()) {
            // No query. Display all items
            List<File> files = fileService.getAll();
            if (files != null && !files.isEmpty()) {
                responseData.addAll(files);
            }
            resp.setStatus(200);
        } else {
            // Some query is present
            String parameterId = req.getParameter("id");
            try {
                // TODO: вопрос про try vs if
                // Это ОК так управлять ходом программы?
                // Или надо было несколько IF вписать на проверку наличия id и проверку на число?
                // Читал мнение в Инете, что нехорошо через try подменять работу if, но так вышло на 1 else короче

                Integer id = Integer.valueOf(parameterId);
                // At this step query has numerical ID
                File file = fileService.getById(id);
                if (file != null) {
                    responseData.add(file);
                    resp.setStatus(200);
//                    TODO: вопрос по условиям и логике работы
//                    Добавить в обработку выше String username = req.getParameter("user");
//                    Ищем в БД по имени пользователя
//                    Если такого нет в списке, то отказываем в предоставлении файла
//                    Если пользователь есть в списке, то предоставляем файл и добавляем скачивание этому пользователю
//                    Такой смысл?
                } else {
                    // DB has no entry with this ID
                    responseData = null;
                    resp.setStatus(400);
                }
            } catch (NumberFormatException e) {
                // Query has incorrect ID or no ID
                responseData = null;
                resp.setStatus(400);
            }

        }

        String jsonResponse = new ObjectMapper().writeValueAsString(responseData);
        PrintWriter messageWriter = resp.getWriter();
        messageWriter.write(jsonResponse);
    }
}
