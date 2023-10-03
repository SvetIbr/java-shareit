package ru.practicum.shareIt.item.comment.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
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
    @NotBlank
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
