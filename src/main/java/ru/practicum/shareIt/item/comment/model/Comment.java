package ru.practicum.shareIt.item.comment.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.shareIt.item.model.Item;
import ru.practicum.shareIt.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Класс комментариев со свойствами <b>id</b>, <b>text</b>, <b>item</b>,
 * <b>author</b>, <b>created</b> для работы с базой данных
 *
 * @author Светлана Ибраева
 * @version 1.0
 */
@Entity
@Table(name = "comments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {
    /**
     * Поле идентификатор
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Поле текст
     */
    @Column(name = "text")
    @NotNull(message = "Не указан текст комментария")
    @NotBlank(message = "Не заполнено поле \"текст\"")
    private String text;

    /**
     * Поле вещь, на которую оставляют комментарий
     */
    @NotNull(message = "Нет информации о вещи")
    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    /**
     * Поле автор
     */
    @NotNull(message = "Нет информации об авторе")
    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    /**
     * Поле дата создания
     */
    @Column(name = "create_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd, hh:mm:ss")
    private LocalDateTime created;
}
