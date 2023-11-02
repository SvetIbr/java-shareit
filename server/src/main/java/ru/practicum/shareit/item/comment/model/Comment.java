package ru.practicum.shareit.item.comment.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

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
    private String text;

    /**
     * Поле вещь, на которую оставляют комментарий
     */
    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    /**
     * Поле автор
     */
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
