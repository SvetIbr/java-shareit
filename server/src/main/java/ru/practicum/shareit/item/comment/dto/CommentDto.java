package ru.practicum.shareit.item.comment.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Класс комментариев со свойствами <b>id</b>, <b>text</b>, <b>authorName</b>,
 * <b>created</b> для работы через REST-интерфейс
 *
 * @author Светлана Ибраева
 * @version 1.0
 */
@Data
@Builder
public class CommentDto {
    /**
     * Поле идентификатор
     */
    private Long id;

    /**
     * Поле текст
     */
    private String text;

    /**
     * Поле имя автора
     */
    private String authorName;

    /**
     * Поле дата создания
     */
    private LocalDateTime created;
}
