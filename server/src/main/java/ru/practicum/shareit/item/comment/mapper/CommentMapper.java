package ru.practicum.shareit.item.comment.mapper;


import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

/**
 * Mapper-класс для преобразования объектов Comment в CommentDto и наоборот
 *
 * @author Светлана Ибраева
 * @version 1.0
 */
public class CommentMapper {
    /**
     * Метод преобразования объекта Comment в CommentDto
     *
     * @param comment {@link Comment}
     * @return {@link CommentDto}
     */
    public static CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorName(comment.getAuthor().getName())
                .created(comment.getCreated())
                .build();
    }

    /**
     * Метод преобразования объекта CommentDto в Comment
     *
     * @param commentDto {@link CommentDto}
     * @param item       {@link Item}
     * @param author     {@link User}
     * @param time       - дата создания
     * @return {@link Comment}
     */
    public static Comment toComment(CommentDto commentDto, Item item, User author, LocalDateTime time) {
        return Comment.builder()
                .id(commentDto.getId())
                .text(commentDto.getText())
                .item(item)
                .author(author)
                .created(time)
                .build();
    }
}
