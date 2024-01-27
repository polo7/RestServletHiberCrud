package dev.lesechko.proselyte.repository.hibernate;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import dev.lesechko.proselyte.model.File;
import dev.lesechko.proselyte.model.Status;
import dev.lesechko.proselyte.repository.FileRepository;
import dev.lesechko.proselyte.utils.HibernateConnectionUtils;


public class HibernateFileRepositoryImpl implements FileRepository {
    private void rollbackTransaction(Transaction t) {
        if (t != null) {
            t.rollback();
            System.err.println("Hibernate: Rolling back transaction");
        }
    }

    private boolean hasValidValues(File file) {
        if (file.getName() == null ||  file.getName().isBlank()
            || file.getFilePath() == null || file.getFilePath() == null
            || file.getStatus() == null) {
            return false;
        }
        return true;
    }

    @Override
    public List<File> getAll() {
        try (Session session = HibernateConnectionUtils.getNewSession()) {
            return session.createQuery("FROM File", File.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public File getById(Integer id) {
        try (Session session = HibernateConnectionUtils.getNewSession()) {
            return session.get(File.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public File save(File file) {
        if (file == null) {
            return null;
        }
        Transaction transaction = null;
        try (Session session = HibernateConnectionUtils.getNewSession()) {
            if (!hasValidValues(file)) {
                throw new Exception("Incorrect field values");
            }
            transaction = session.beginTransaction();
            session.persist(file);
            transaction.commit();
            return file;
        } catch (Exception e ) {
            rollbackTransaction(transaction);
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public File update(File file) {
        if (file == null) {
            return null;
        }
        Transaction transaction = null;
        try (Session session = HibernateConnectionUtils.getNewSession()) {
            if (session.get(File.class, file.getId()) == null) {
                throw new Exception("ID is not found. Nothing to update");
            }
            if (!hasValidValues(file)) {
                throw new Exception("Incorrect field values");
            }
            transaction = session.beginTransaction();
            session.merge(file);
            transaction.commit();
            return file;
        } catch (Exception e) {
            rollbackTransaction(transaction);
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean deleteById(Integer id) {
        Transaction transaction = null;
        try (Session session = HibernateConnectionUtils.getNewSession()) {
            File file = session.get(File.class, id);
            if (file == null) {
                throw new Exception("ID is not found. Nothing to delete.");
            }
            file.setStatus(Status.DELETED);
            transaction = session.beginTransaction();
            session.merge(file);
            transaction.commit();
            return true;
        } catch (Exception e) {
            rollbackTransaction(transaction);
            e.printStackTrace();
            return false;
        }
    }
}
