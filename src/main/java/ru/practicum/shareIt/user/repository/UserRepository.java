package ru.practicum.shareIt.user.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareIt.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
public class UserRepository {

    private final Map<Long, User> users = new HashMap<>();
    private long nextId = 1;

    public User create(User user) {
        user.setId(nextId);
        users.put(nextId, user);
        nextId++;
        log.info("Добавлен пользователь {}", user);
        return user;
    }

    public User getById(Long id) {
        return users.getOrDefault(id, null);
    }

    public boolean isDuplicateEmail(String email) {
        int count = (int) users.values().stream().filter(user -> user.getEmail().equals(email)).count();
        return count != 0;
    }

    public void update(User user) {
        users.put(user.getId(), user);
        log.info("Обновлен пользователь " + user);
    }

    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    public void deleteById(Long id) {
        log.info("Удален пользователь " + users.get(id));
        users.remove(id);
    }
}

