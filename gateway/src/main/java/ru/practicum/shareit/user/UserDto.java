package ru.practicum.shareit.user;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class UserDto {
    private Long id;

    @NotNull(message = "Не указано имя")
    @NotBlank(message = "Не заполнено поле \"имя\"")
    private String name;

    @NotNull(message = "Не указан email")
    @Pattern(regexp = "^[^/:*?\"<>|]+$", message = "Не используйте символы для указания имени " +
            "- только буквы и цифры")
    @Email(message = "Некорректный email")
    @NotBlank(message = "Не заполнено поле \"email\"")
    private String email;
}
