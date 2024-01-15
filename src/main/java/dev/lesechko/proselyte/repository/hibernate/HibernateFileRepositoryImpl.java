package dev.lesechko.proselyte.repository.hibernate;

import dev.lesechko.proselyte.model.File;
import dev.lesechko.proselyte.repository.FileRepository;

import java.util.List;

public class HibernateFileRepositoryImpl implements FileRepository { //TODO: implement FileRepo
    @Override
    public List<File> getAll() {
        return null;
    }

    @Override
    public File getById(Integer integer) {
        return null;
    }

    @Override
    public File save(File file) {
        return null;
    }

    @Override
    public File update(File file) {
        return null;
    }

    @Override
    public boolean deleteById(Integer integer) {
        return false;
    }
}
