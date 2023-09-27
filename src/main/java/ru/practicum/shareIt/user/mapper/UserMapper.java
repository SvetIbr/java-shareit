package ru.practicum.shareIt.user.mapper;

import ru.practicum.shareIt.user.dto.UserDto;
import ru.practicum.shareIt.user.model.User;

/**
 * Mapper-класс для преобразования объектов User в UserDto и наоборот
 *
 * @author Светлана Ибраева
 * @version 1.0
 */
public class UserMapper {
    /**
     * Метод преобразования объекта User в UserDto
     *
     * @param user {@link User}
     * @return UserDto {@link UserDto} c идентичной информацией
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
}
