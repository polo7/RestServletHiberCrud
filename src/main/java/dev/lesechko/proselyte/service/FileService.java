package dev.lesechko.proselyte.service;

import java.util.List;

import dev.lesechko.proselyte.model.File;
import dev.lesechko.proselyte.repository.FileRepository;
import dev.lesechko.proselyte.repository.hibernate.HibernateFileRepositoryImpl;


public class FileService {
    private final FileRepository fileRepository = new HibernateFileRepositoryImpl();

    public List<File> getAll() {
        return fileRepository.getAll();
    }

    public File getById(Integer id) {
        return fileRepository.getById(id);
    }

    public File save(File file) {
        return fileRepository.save(file);
    }

    public boolean deleteById(Integer id) {
        return fileRepository.deleteById(id);
    }

    public File update(File file) {
        return fileRepository.update(file);
    }
}
