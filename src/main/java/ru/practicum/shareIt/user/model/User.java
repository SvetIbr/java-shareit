package ru.practicum.shareIt.user.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Objects;

/**
 * Класс пользователя со свойствами <b>id</b>, <b>name</b>, <b>email</b>
 * для работы с базой данных
 *
 * @author Светлана Ибраева
 * @version 1.0
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    /**
     * Поле идентификатор
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Поле имя
     */
    @Pattern(regexp = "^[^/:*?\"<>|]+$", message = "Не используйте символы для указания имени " +
            "- только буквы и цифры")
    @NotNull(message = "Не указано имя")
    @NotBlank(message = "Не заполнено поле \"имя\"")
    private String name;

    /**
     * Поле электронная почта
     */
    @Column(unique = true)
    @Email(message = "Некорректный email")
    @NotNull(message = "Не указан email")
    @NotBlank(message = "Не заполнено поле \"email\"")
    private String email;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
