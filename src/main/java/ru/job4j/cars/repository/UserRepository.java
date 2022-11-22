package ru.job4j.cars.repository;

import org.hibernate.query.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import java.util.ArrayList;
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
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
        }
        return user;
    }

    /**
     * Обновить в базе пользователя.
     * @param user пользователь.
     */
    public void update(User user) {
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            session.createQuery("update User set login = :flogin, password = :fpassword where id = :fid")
                    .setParameter("flogin", user.getLogin())
                    .setParameter("fpassword", user.getPassword())
                    .setParameter("fid", user.getId())
                    .executeUpdate();
            session.getTransaction().commit();
        }
    }

    /**
     * Удалить пользователя по id.
     * @param userId ID
     */
    public void delete(int userId) {
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            session.createQuery("delete User where id = :fid")
                    .setParameter("fid", userId)
                    .executeUpdate();
            session.getTransaction().commit();
        }
    }

    /**
     * Список пользователей, отсортированных по id.
     * @return список пользователей.
     */
    public List<User> findAllOrderById() {
        List<User> result = new ArrayList<>();
        try (Session session = sf.openSession()) {
            Query query = session.createQuery("from User");
            for (Object el:query.list()) {
                result.add((User) el);
            }
        }
        return result;
    }

    /**
     * Найти пользователя по ID
     * @return пользователь.
     */
    public Optional<User> findById(int id) {
        Optional result = Optional.empty();
        try (Session session = sf.openSession()) {
            Query query = session.createQuery("from User where id = :fid")
                    .setParameter("fid", id);
            result = (query.uniqueResultOptional());
        }
        return result;
    }

    /**
     * Список пользователей по login LIKE %key%
     * @param key key
     * @return список пользователей.
     */
    public List<User> findByLikeLogin(String key) {
        List<User> result = new ArrayList<>();
        try (Session session = sf.openSession()) {
            Query query = session.createQuery("from User as u where u.login like :fkey")
                    .setParameter("fkey", key);
            for (Object el: query.list()) {
                result.add((User) el);
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
        Optional<User> user = Optional.empty();
        try (Session session = sf.openSession()) {
            Query query = session.createQuery("from User where login = :flogin")
                    .setParameter("flogin", login);
            user = Optional.of((User) query.list().get(0));
        }
        return user;
    }
}