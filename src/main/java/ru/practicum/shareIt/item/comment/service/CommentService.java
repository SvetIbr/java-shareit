package ru.practicum.shareIt.item.comment.service;

import ru.practicum.shareIt.item.comment.dto.CommentDto;

import java.util.List;

public interface CommentService {
    CommentDto addComment(Long userId, Long itemId, CommentDto commentDto);

    List<CommentDto> getCommentsByItem(Long itemId);
}
