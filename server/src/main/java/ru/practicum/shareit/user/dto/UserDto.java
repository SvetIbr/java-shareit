package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Data;

/**
 * Класс пользователя со свойствами <b>id</b>, <b>name</b>, <b>email</b>
 * для работы через REST-интерфейс
 *
 * @author Светлана Ибраева
 * @version 1.0
 */
@Data
@Builder
public class UserDto {
    /**
     * Поле идентификатор
     */
    private Long id;

    /**
     * Поле имя
     */
    private String name;

    /**
     * Поле электронная почта
     */
    private String email;
}

