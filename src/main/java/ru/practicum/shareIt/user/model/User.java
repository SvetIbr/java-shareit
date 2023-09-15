package ru.practicum.shareIt.user.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Builder
public class User {

    private Long id;

    @NotNull(message = "Не указано имя")
    @NotBlank(message = "Имя не может быть пустым")
    private String name;

    @Email(message = "Некорректный email")
    @NotNull(message = "Email не должен равняться null")
    @NotBlank(message = "Email не может быть пустым")
    private String email;
}
