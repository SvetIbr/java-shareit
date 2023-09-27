package ru.practicum.shareIt.user.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
    @NotNull(message = "Не указано имя")
    @NotBlank(message = "Не заполнено поле \"имя\"")
    private String name;

    /**
     * Поле электронная почта
     */
    @NotNull(message = "Не указан email")
    @Email(message = "Некорректный email")
    @NotBlank(message = "Не заполнено поле \"email\"")
    private String email;
}

