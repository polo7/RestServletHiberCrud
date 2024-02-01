package dev.lesechko.proselyte.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.fasterxml.jackson.databind.ObjectMapper;

import dev.lesechko.proselyte.model.Event;
import dev.lesechko.proselyte.model.User;
import dev.lesechko.proselyte.service.EventService;
import dev.lesechko.proselyte.service.UserService;
import dev.lesechko.proselyte.model.File;
import dev.lesechko.proselyte.service.FileService;


/*
    GET
    http://localhost:8080/api/v1/files - get JSON displaying all files
    http://localhost:8080/api/v1/files/{id} - get JSON file by ID

    POST
    http://localhost:8080/api/v1/files + JSON - create new file via POSTing a real file to this endpont

    DELETE
    http://localhost:8080/api/v1/files/{id} - delete file by id

    PUT
    http://localhost:8080/api/v1/files + JSON - update file using JSON
*/

@WebServlet(
        name = "FileRestController",
        urlPatterns = {"/api/v1/files/*"}
)
public class FileRestController extends HttpServlet {
    private final String ENCODING = "UTF-8";
    private final String CONTENT_TYPE = "application/json; charset=" + ENCODING;
    private final String FILE_STORAGE_PATH = "./src/main/resources/uploads/";
    private final int MEM_MAX_SIZE = 10_000 * 1024; // disk space = 10 Mb
    private final int FILE_MAX_SIZE = 1_000 * 1024; // file size = 1 Mb

    private final FileService fileService = new FileService();
    private final UserService userService = new UserService();
    private final EventService eventService = new EventService();

    private Integer extractIdFromPath(String pathInfo) {
        try {
            String[] pathParams = pathInfo.split("/");
            return Integer.valueOf(pathParams[1]);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String approveFileName(String path, String fileName) {
        while (Files.exists(Path.of(path, fileName))) fileName = "Copy " + fileName;
        return fileName;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding(ENCODING);
        resp.setContentType(CONTENT_TYPE);
        List<File> responseData = new ArrayList<>();
        String pathInfo = req.getPathInfo();

        if (pathInfo == null || "/".equals(pathInfo)) {
            // Just "/files" w/o parameters - display all files
            List<File> files = fileService.getAll();
            if (files != null && !files.isEmpty()) {
                responseData.addAll(files);
            }
            resp.setStatus(200);
        } else {
            Integer id = extractIdFromPath(pathInfo);
            File file = fileService.getById(id);
            if (file != null) {
                responseData.add(file);
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
        List<File> responseData = new ArrayList<>();
        String pathInfo = req.getPathInfo();

        if (pathInfo == null || "/".equals(pathInfo)) {
            // POST /files is the only point to upload files
            DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
            diskFileItemFactory.setRepository(new java.io.File(FILE_STORAGE_PATH));
            diskFileItemFactory.setSizeThreshold(MEM_MAX_SIZE);

            ServletFileUpload upload = new ServletFileUpload(diskFileItemFactory);
            upload.setSizeMax(FILE_MAX_SIZE);

            try {
                List<FileItem> fileItems = upload.parseRequest(req);
                Iterator<FileItem> iterator = fileItems.iterator();

                while (iterator.hasNext()) {
                    FileItem fileItem = iterator.next();
                    String fileName = fileItem.getName();

                    // write on disk
                    fileName = approveFileName(FILE_STORAGE_PATH, fileName);
                    java.io.File fileOnDisk = new java.io.File(FILE_STORAGE_PATH + fileName);
                    fileItem.write(fileOnDisk);

                    // insert into DB
                    File fileToSave = new File(fileName, FILE_STORAGE_PATH);
                    File fileSaved = fileService.save(fileToSave);
                    if (fileSaved != null) {
                        Integer userId = Integer.valueOf(req.getHeader("User-Id"));
                        User currentUser = userService.getById(userId);
                        Event currentEvent = new Event(currentUser, fileSaved);
                        List<Event> eventsForUser = currentUser.getEvents();
                        eventsForUser.add(currentEvent);
                        currentUser.setEvents(eventsForUser);
                        eventService.save(currentEvent);
                        responseData.add(fileSaved);
                        resp.setStatus(201);
                    } else {
                        // Something went wrong while saving to DB. Our side.
                        if (fileOnDisk.exists()) {
                            fileOnDisk.delete();
                        }
                        fileItem.delete();
                        responseData = null;
                        resp.setStatus(503);
                    }
                }
            } catch (Exception e) {
                responseData = null;
                resp.setStatus(400);
                e.printStackTrace();
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
        resp.setCharacterEncoding(ENCODING);
        resp.setContentType(CONTENT_TYPE);
        Boolean responseData = false;
        String pathInfo = req.getPathInfo();

        if (pathInfo == null || "/".equals(pathInfo)) {
            // No ID in request
            responseData = null;
            resp.setStatus(400);
        } else {
            Integer id = extractIdFromPath(pathInfo);
            if (fileService.deleteById(id)) {
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
        resp.setCharacterEncoding(ENCODING);
        resp.setContentType(CONTENT_TYPE);
        List<File> responseData = new ArrayList<>();
        String pathInfo = req.getPathInfo();

        if (pathInfo == null || "/".equals(pathInfo)) {
            // PUT /files is the only point to upload JSON with updates
            File file = new ObjectMapper().readValue(req.getInputStream(), File.class);
            File updatedFile = fileService.update(file);
            if (updatedFile != null) {
                responseData.add(updatedFile);
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
