package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.usertype.UserCollectionType;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {

    private final Util util = new Util();
    private final SessionFactory sessionFactory;

    public UserDaoHibernateImpl() {
        sessionFactory = util.getSession();
    }

    @Override
    public void createUsersTable() {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.createNativeQuery("CREATE TABLE IF NOT EXISTS user (id BIGINT PRIMARY KEY AUTO_INCREMENT, "
                    +"name VARCHAR(50) NOT NULL, lastname VARCHAR(50) NOT NULL, age TINYINT NOT NULL)").executeUpdate();
            transaction.commit();
        } catch (HibernateException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void dropUsersTable() {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.createNativeQuery("DROP TABLE IF EXISTS user").executeUpdate();
            transaction.commit();
        } catch (HibernateException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.save(new User(name, lastName, age));
            transaction.commit();
        } catch (HibernateException e) {
            System.out.println(e.getMessage());
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
        }
    }

    @Override
    public void removeUserById(long id) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            User user = session.get(User.class, id);
            if (user != null) {
                transaction = session.beginTransaction();
                session.delete(user);
                transaction.commit();
            }
        } catch (HibernateException e) {
            System.out.println(e.getMessage());
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = null;
        try (Session session = sessionFactory.openSession()) {
            users = session.createQuery("from User", User.class).list();
        } catch (HibernateException e) {
            System.out.println(e.getMessage());
        }
        return users;
    }

    @Override
    public void cleanUsersTable() {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.createQuery("delete from User").executeUpdate();
            transaction.commit();
        } catch (HibernateException e) {
            System.out.println(e.getMessage());
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
        }
    }
}