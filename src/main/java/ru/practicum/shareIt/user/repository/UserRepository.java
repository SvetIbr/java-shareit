package ru.practicum.shareIt.user.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareIt.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Класс для хранения всех пользователей
 *
 * @author Светлана Ибраева
 * @version 1.0
 */
@Slf4j
@Repository
public class UserRepository {
    /**
     * Поле хранилище всех пользователей
     */
    private final Map<Long, User> users = new HashMap<>();
    /**
     * Поле присваемого идентификатора каждого пользователя при добавлении в хранилище
     */
    private long nextId = 1;

    /**
     * Метод добавления пользователя
     *
     * @param user {@link User}
     * @return копию объекта user с присвоенным идентификатором
     */
    public User create(User user) {
        user.setId(nextId);
        users.put(nextId, user);
        nextId++;
        log.info("Добавлен пользователь {}", user);
        return user;
    }

    /**
     * Метод обновления информации о пользователе
     *
     * @param user {@link User}
     */
    public void update(User user) {
        users.put(user.getId(), user);
        log.info("Обновлен пользователь " + user);
    }

    /**
     * Метод получения информации о пользователе по идентификатору
     *
     * @param id - идентификатор пользователя
     * @return копию объекта user с указанным идентификатором,
     * в случае отсутствия пользователя по идентификатору - null
     */
    public User getById(Long id) {
        return users.getOrDefault(id, null);
    }

    /**
     * Метод получения всего списка пользователей
     *
     * @return список всех пользователей
     */
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    /**
     * Метод удаления пользователя по идентификатору
     *
     * @param id - идентификатор пользователя
     */
    public void deleteById(Long id) {
        log.info("Удален пользователь " + users.get(id));
        users.remove(id);
    }

    /**
     * Метод проверки email пользователя на наличие в хранилище
     *
     * @param email - электронная почта
     * @return true, если в хранилище уже есть пользователь с такой почтой
     * false, если в хранилище еще нет пользователя с такой почтой
     */
    public boolean isDuplicateEmail(String email) {
        int count = (int) users.values().stream().filter(user -> user.getEmail().equals(email)).count();
        return count != 0;
    }
}

