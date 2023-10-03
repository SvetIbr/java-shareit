package ru.practicum.shareIt.item.comment.service;

import ru.practicum.shareIt.booking.dto.BookingResponseDto;
import ru.practicum.shareIt.item.comment.dto.CommentDto;

import java.util.List;

/**
 * Интерфейс сервиса комментариев
 *
 * @author Светлана Ибраева
 * @version 1.0
 */
public interface CommentService {
    /**
     * Метод добавления комментария
     *
     * @param userId     - идентификатор пользователя, добавляющего комментарий
     * @param itemId     - идентификатор вещи, которой оставляют комментарий
     * @param commentDto {@link CommentDto}
     * @return {@link CommentDto} с добавленным id и код ответа API 201
     */
    CommentDto addComment(Long userId, Long itemId, CommentDto commentDto);

    /**
     * Метод получения списка комментариев определенной вещи
     *
     * @param itemId - идентификатор вещи
     * @return {@link BookingResponseDto}
     */
    List<CommentDto> getCommentsByItem(Long itemId);
}
