package ru.practicum.shareIt.user.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class UserDto {

    private Long id;

    @NotNull(message = "Не указано имя")
    @NotBlank(message = "Имя не может быть пустым")
    private String name;

    @NotNull(message = "Email не должен равняться null")
    @Email(message = "Некорректный email")
    @NotBlank(message = "Email не может быть пустым")
    private String email;
}

