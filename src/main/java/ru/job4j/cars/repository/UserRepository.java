package ru.job4j.cars.repository;

import org.hibernate.HibernateException;
import org.hibernate.query.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import ru.job4j.cars.model.User;

public class UserRepository {
    private final SessionFactory sf;

    public UserRepository(SessionFactory sf) {
        this.sf = sf;
    }

    /**
     * Сохранить в базе.
     * @param user пользователь.
     * @return пользователь с id.
     */
    public User create(User user) {
        Session session = sf.openSession();
        session.beginTransaction();
        session.save(user);
        session.getTransaction().commit();
        session.close();
        return user;
    }

    /**
     * Обновить в базе пользователя.
     * @param user пользователь.
     */
    public void update(User user) {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.createQuery(
                    "update User set login = :flogin, password = :fpassword where id = :fid")
                    .setParameter("flogin", user.getLogin())
                    .setParameter("fpassword", user.getPassword())
                    .setParameter("fid", user.getId())
                    .executeUpdate();
            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            session.getTransaction().rollback();
        }
    }

    /**
     * Удалить пользователя по id.
     * @param userId ID
     */
    public void delete(int userId) {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.createQuery("delete User where id = :fid")
                    .setParameter("fid", userId)
                    .executeUpdate();
            session.getTransaction().commit();
            session.close();
        } catch (HibernateException e) {
            session.getTransaction().rollback();
        }
    }

    /**
     * Список пользователей, отсортированных по id.
     * @return список пользователей.
     */
    public List<User> findAllOrderById() {
        Session session = sf.openSession();
        session.beginTransaction();
        Comparator<User> comparator = Comparator.comparing(User::getId);
        List<User> result = session.createQuery("from User", User.class).list();
        result.sort(comparator);
        session.getTransaction().commit();
        session.close();
        return result;
    }

    /**
     * Найти пользователя по ID
     * @return пользователь.
     */
    public Optional<User> findById(int id) {
        Session session = sf.openSession();
        session.beginTransaction();
        session.createQuery("from User where id = :fid", User.class)
                    .setParameter("fid", id);
        Optional<User> user = Optional.ofNullable(session.get(User.class, id));
        session.getTransaction().commit();
        session.close();
        return user;
    }

    /**
     * Список пользователей по login LIKE %key%
     * @param key key
     * @return список пользователей.
     */
    public List<User> findByLikeLogin(String key) {
        Session session = sf.openSession();
        session.beginTransaction();
        List<User> list = session.createQuery(
                "from User", User.class).list();
        session.getTransaction().commit();
        session.close();
        List<User> result = new ArrayList<>();
        for (User user: list) {
            if (user.getLogin().contains(key)) {
                result.add(user);
            }
        }
        return result;
    }

    /**
     * Найти пользователя по login.
     * @param login login.
     * @return Optional or user.
     */
    public Optional<User> findByLogin(String login) {
        Session session = sf.openSession();
        session.beginTransaction();
        Optional<User> user = Optional.ofNullable(
                session.createQuery("from User as i where i.login = :fLogin", User.class)
                .setParameter("fLogin", login).list().get(0));
        session.getTransaction().commit();
        session.close();
        return user;
    }
}