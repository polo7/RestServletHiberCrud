package dev.lesechko.proselyte.controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.lesechko.proselyte.model.File;
import dev.lesechko.proselyte.service.FileService;

public class FileRestController extends HttpServlet {
    private final String ENCODING = "UTF-8";
    private final String CONTENT_TYPE = "application/json; charset=" + ENCODING;
    private final FileService fileService = new FileService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding(ENCODING);
        resp.setContentType(CONTENT_TYPE);
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
        PrintWriter message = resp.getWriter();
        message.write(jsonResponse);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding(ENCODING);
        resp.setContentType(CONTENT_TYPE);
        List<File> responseData = new ArrayList<>();
        String requestQuery = req.getQueryString();
        if (requestQuery != null && !requestQuery.isBlank()) {
            String fileName = req.getParameter("name");
            String filePath = req.getParameter("path");

            if (fileName != null && !fileName.isBlank()
                    && filePath != null && !filePath.isBlank()) {
                File fileToSave = new File(fileName, filePath);
                File fileSaved = fileService.save(fileToSave);

                if (fileSaved != null) {
                    responseData.add(fileSaved);
                    resp.setStatus(201);
                } else {
                    // Something went wrong while saving. Our side.
                    responseData = null;
                    resp.setStatus(500);
                }
            } else {
                // Query is incorrect (epmty values)
                responseData = null;
                resp.setStatus(400);
            }
        } else {
            // No query - nothing to add
            responseData = null;
            resp.setStatus(400);
        }
        String jsonResponse = new ObjectMapper().writeValueAsString(responseData);
        PrintWriter message = resp.getWriter();
        message.write(jsonResponse);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding(ENCODING);
        resp.setContentType(CONTENT_TYPE);
        Boolean responseData = false;
        String requestQuery = req.getQueryString();
        if (requestQuery != null && !requestQuery.isBlank()) {
            String parameterId = req.getParameter("id");
            try {
                Integer id = Integer.valueOf(parameterId);
                if (fileService.deleteById(id)) {
                    responseData = true;
                    resp.setStatus(200);
                } else {
                    responseData = false;
                    resp.setStatus(200);
                }
            } catch (NumberFormatException e) {
                // Query has incorrect ID or no ID
                responseData = null;
                resp.setStatus(400);
            }

        } else {
            // No query - nothing to delete
            responseData = null;
            resp.setStatus(400);
        }
        String jsonResponse = new ObjectMapper().writeValueAsString(responseData);
        PrintWriter message = resp.getWriter();
        message.write(jsonResponse);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding(ENCODING);
        resp.setContentType(CONTENT_TYPE);
        List<File> responseData = new ArrayList<>();
        String requestQuery = req.getQueryString();

        if (requestQuery != null && !requestQuery.isBlank()) {
            String parameterId = req.getParameter("id");
            String newFileName = req.getParameter("name");
            String newFilePath = req.getParameter("path");

            try {
                Integer id = Integer.valueOf(parameterId);
                if (newFileName != null && !newFileName.isBlank()
                        && newFilePath != null && !newFilePath.isBlank()) {
                    File fileToUpdate = fileService.getById(id);
                    // What if not found by id? Add new If?
                    fileToUpdate.setName(newFileName);
                    fileToUpdate.setFilePath(newFilePath);
                    File fileUpdated = fileService.update(fileToUpdate);

                    if (fileUpdated != null) {
                        responseData.add(fileUpdated);
                        resp.setStatus(200);
                    } else {
                        // Something went wrong while updating.
                        // Our side or wrong ID.
                        // 4xx or 5xx or add new if?
                        responseData = null;
                        resp.setStatus(400);
                    }
                } else {
                    // Query has incorrect file name and/or file path
                    responseData = null;
                    resp.setStatus(400);
                }

            } catch (NumberFormatException e) {
                // Query has incorrect ID or no ID
                responseData = null;
                resp.setStatus(400);
            }
        } else {
            // No query - nothing to update
            responseData = null;
            resp.setStatus(400);
        }

        String jsonResponse = new ObjectMapper().writeValueAsString(responseData);
        PrintWriter message = resp.getWriter();
        message.write(jsonResponse);
    }
}
