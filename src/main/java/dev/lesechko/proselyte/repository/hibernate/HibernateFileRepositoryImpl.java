package dev.lesechko.proselyte.repository.hibernate;

import dev.lesechko.proselyte.utils.HibernateConnectionUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;

import dev.lesechko.proselyte.model.File;
import dev.lesechko.proselyte.repository.FileRepository;

import java.util.List;

public class HibernateFileRepositoryImpl implements FileRepository {
    private void rollbackTransaction(Transaction t) {
        if (t != null) {
            t.rollback();
            System.err.println("Hibernate: Rolling back transaction");
        }
    }

    @Override
    public List<File> getAll() {
        Transaction transaction = null;
        try (Session session = HibernateConnectionUtils.getNewSession()) {
            transaction = session.beginTransaction();
            List<File> files = session.createQuery("FROM File", File.class).list();
            transaction.commit();
            return files;
        } catch (Exception e) {
            rollbackTransaction(transaction);
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public File getById(Integer id) {
        Transaction transaction = null;
        try (Session session = HibernateConnectionUtils.getNewSession()) {
            transaction = session.beginTransaction();
            File file = session.get(File.class, id);
            transaction.commit();
            return file;
        } catch (Exception e) {
            rollbackTransaction(transaction);
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public File save(File file) {
        Transaction transaction = null;
        try (Session session = HibernateConnectionUtils.getNewSession()) {
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
        Transaction transaction = null;
        try (Session session = HibernateConnectionUtils.getNewSession()) {
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
            transaction = session.beginTransaction();
            File file = session.get(File.class, id);
            if (file == null) {
                transaction.commit();
                return false;
            }
            session.remove(file);
            transaction.commit();
            return true;
        } catch (Exception e) {
            rollbackTransaction(transaction);
            e.printStackTrace();
            return false;
        }
    }
}
