package ru.practicum.shareIt.item.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareIt.item.comment.model.Comment;

import java.util.List;

/**
 * Интерфейс хранилища всех комментариев
 *
 * @author Светлана Ибраева
 * @version 1.0
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {
    /**
     * Метод получения списка всех комментариев конкретной вещи,
     *
     * @param itemId - идентификатор вещи
     * @return список объектов Comment
     */
    List<Comment> findAllByItemId(Long itemId);
}
