package ru.practicum.shareit.request.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

/**
 * Интерфейс хранилища всех запросов на вещи
 *
 * @author Светлана Ибраева
 * @version 1.0
 */
public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {
    /**
     * Метод получения списка своих запросов вместе с данными об ответах на них,
     * отсортированных по дате создания - от более новых к более старым
     *
     * @param userId - идентификатор владельца
     * @return список объектов ItemRequest {@link ItemRequest}
     */
    List<ItemRequest> getByRequestorIdOrderByCreatedAsc(Long userId);

    /**
     * Метод получения списка вещей теккущего пользовтеля, созданных на какой-либо запрос.
     * Результаты возвращаются постранично
     *
     * @param userId - текущий пользователь
     * @return список объектов ItemRequest {@link ItemRequest}
     */
    List<ItemRequest> getByRequestorIdNotOrderByCreatedDesc(Long userId, PageRequest of);
}
