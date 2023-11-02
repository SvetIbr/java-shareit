package ru.practicum.shareit.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.user.model.User;

/**
 * Интерфейс хранилища всех пользователей
 *
 * @author Светлана Ибраева
 * @version 1.0
 */
public interface UserRepository extends JpaRepository<User, Long> {
}
