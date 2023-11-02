package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.BadRequestException;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Valid
@Slf4j
public class UserController {
    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody @Valid UserDto userDto) {
        log.info("Create new user={}", userDto);
        return userClient.createUser(userDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateUser(@RequestBody UserDto userDto, @PathVariable Long id) {
        if (id == null) {
            throw new BadRequestException("Не указан идентификатор пользователя " +
                    "для обновления информации");
        }
        log.info("Update user={}", userDto);
        return userClient.updateUser(id, userDto);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable Long id) {
        if (id == null) {
            throw new BadRequestException("Не указан идентификатор пользователя " +
                    "для обновления информации");
        }
        log.info("Get user with id={}", id);
        return userClient.getUser(id);
    }

    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        log.info("Get all users");
        return userClient.getUsers();
    }

    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable Long id) {
        if (id == null) {
            throw new BadRequestException("Не указан идентификатор пользователя " +
                    "для обновления информации");
        }
        log.info("Remove user with ID = {}", id);
        userClient.deleteUserById(id);
    }
}
