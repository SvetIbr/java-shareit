package ru.practicum.shareit.user.service;


import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

/**
 * Интерфейс сервиса пользователей
 *
 * @author Светлана Ибраева
 * @version 1.0
 */
public interface UserService {
    /**
     * Метод добавления пользователя в хранилище
     *
     * @param userDto {@link UserDto}
     * @return копию объекта userDto с добавленным id
     */
    UserDto create(UserDto userDto);

    /**
     * Метод обновления информации о пользователе в хранилище
     *
     * @param id      - идентификатор пользователя
     * @param userDto {@link UserDto} - данные для обновления
     * @return копию объекта userDto с обновленными полями
     */
    UserDto update(Long id, UserDto userDto);

    /**
     * Метод получения информации о пользователе из хранилища по идентификатору
     *
     * @param id - идентификатор пользователя
     * @return копию объекта userDto
     */
    UserDto getById(Long id);

    /**
     * Метод получения всего списка пользователей из хранилища
     *
     * @return список всех пользователей
     */
    List<UserDto> getAll();

    /**
     * Метод удаления пользователя из хранилища по идентификатору
     *
     * @param id - идентификатор пользователя
     */
    void deleteById(Long id);
}
