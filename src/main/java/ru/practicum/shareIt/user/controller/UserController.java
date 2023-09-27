package ru.practicum.shareIt.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareIt.user.dto.UserDto;
import ru.practicum.shareIt.user.service.UserServiceImpl;

import javax.validation.Valid;
import java.util.List;

/**
 * Класс контроллера для работы с запросами к сервису пользователей
 *
 * @author Светлана Ибраева
 * @version 1.0
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    /**
     * Поле сервис для работы с хранилищем пользователей
     */
    private final UserServiceImpl service;

    /**
     * Метод добавления пользователя в хранилище сервиса через запрос
     *
     * @param userDto {@link UserDto}
     * @return копию объекта userDto с добавленным id и код ответа API 201
     */
    @PostMapping
    public UserDto create(@Valid @RequestBody UserDto userDto) {
        return new ResponseEntity<>(service.create(userDto), HttpStatus.CREATED).getBody();
    }

    /**
     * Метод обновления информации о пользователе в хранилище сервиса через запрос
     *
     * @param id      - идентификатор пользователя
     * @param userDto {@link UserDto} - данные для обновления
     * @return копию объекта userDto с обновленными полями
     */
    @PatchMapping("/{id}")
    public UserDto update(@RequestBody UserDto userDto, @PathVariable Long id) {
        return service.update(id, userDto);
    }

    /**
     * Метод получения информации о пользователе из хранилища сервиса
     * по идентификатору через запрос
     *
     * @param id - идентификатор пользователя
     * @return копию объекта userDto с указанным идентификатором
     */
    @GetMapping("/{id}")
    public UserDto findById(@PathVariable Long id) {
        return service.getById(id);
    }

    /**
     * Метод получения всего списка пользователей из хранилища через запрос
     *
     * @return список всех пользователей
     */
    @GetMapping
    public List<UserDto> findAll() {
        return service.getAll();
    }

    /**
     * Метод удаления пользователя из хранилища сервиса по идентификатору через запрос
     *
     * @param id - идентификатор пользователя
     */
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        service.deleteById(id);
    }
}
