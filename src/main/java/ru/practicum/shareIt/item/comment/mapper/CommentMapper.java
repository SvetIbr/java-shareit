package ru.practicum.shareIt.item.comment.mapper;

import ru.practicum.shareIt.item.comment.dto.CommentDto;
import ru.practicum.shareIt.item.comment.model.Comment;
import ru.practicum.shareIt.item.model.Item;
import ru.practicum.shareIt.user.model.User;

import java.time.LocalDateTime;

public class CommentMapper {
    public static CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorName(comment.getAuthor().getName())
                .created(comment.getCreated())
                .build();
    }

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
