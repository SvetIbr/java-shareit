package ru.practicum.shareit.user.mapper;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserShortDto;
import ru.practicum.shareit.user.model.User;

/**
 * Mapper-класс для преобразования объектов сервиса пользователей
 *
 * @author Светлана Ибраева
 * @version 1.0
 */
public class UserMapper {
    /**
     * Метод преобразования объекта User в UserDto
     *
     * @param user {@link User}
     * @return UserDto {@link UserDto}
     */
    public static UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    /**
     * Метод преобразования объекта UserDto в User
     *
     * @param userDto {@link UserDto}
     * @return User {@link User} c идентичной информацией
     */
    public static User toUser(UserDto userDto) {
        return User.builder()
                .id(userDto.getId())
                .name(userDto.getName())
                .email(userDto.getEmail())
                .build();
    }

    /**
     * Метод преобразования объекта User в UserShortDto
     *
     * @param user {@link User}
     * @return UserShortDto {@link UserShortDto}
     */
    public static UserShortDto toUserShortDto(User user) {
        return UserShortDto.builder()
                .id(user.getId())
                .build();
    }
}
