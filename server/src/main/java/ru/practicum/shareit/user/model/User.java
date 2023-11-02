package ru.practicum.shareit.user.model;

import lombok.*;

import javax.persistence.*;

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
    private String name;

    /**
     * Поле электронная почта
     */
    @Column(unique = true)
    private String email;
}
