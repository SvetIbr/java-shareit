package ru.practicum.shareit.request.service;


import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

/**
 * Интерфейс сервиса запросов на вещи
 *
 * @author Светлана Ибраева
 * @version 1.0
 */
public interface ItemRequestService {
    /**
     * Метод добавления вещи в хранилище
     *
     * @param userId         - идентификатор создателя запроса
     * @param itemRequestDto {@link ItemRequestDto}
     * @return копию объекта ItemRequestDto с добавленным id
     */
    ItemRequestDto create(Long userId, ItemRequestDto itemRequestDto);

    /**
     * Метод получения списка своих запросов вместе с данными об ответах на них
     *
     * @param userId - идентификатор создателя запроса
     * @return список объектов {@link ItemRequestDto}
     */
    List<ItemRequestDto> getAllItemRequestByUser(Long userId);

    /**
     * Метод получения данных об одном конкретном запросе вместе с данными об ответах на них
     *
     * @param userId    - идентификатор текущего пользователя
     * @param requestId - идентификатор запроса
     * @return список объектов {@link ItemRequestDto}
     */
    ItemRequestDto getItemRequestById(Long userId, Long requestId);

    /**
     * Метод получения списка запросов, созданных другими пользователями.
     * Результаты возвращаются постранично
     *
     * @param userId - идентификатор текущего пользователя
     * @param from   - индекс первого элемента, начиная с 0
     * @param size   - количество элементов для отображения
     * @return список объектов {@link ItemRequestDto}
     */
    List<ItemRequestDto> getAllItemRequests(Long userId, Integer from, Integer size);
}
