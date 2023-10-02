package ru.practicum.shareIt.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareIt.user.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsUserByEmail(String email);
}
