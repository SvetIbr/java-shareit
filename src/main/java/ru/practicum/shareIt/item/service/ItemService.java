package ru.practicum.shareIt.item.service;

import ru.practicum.shareIt.item.dto.ItemDto;

import java.util.List;

/**
 * Интерфейс сервиса вещей
 *
 * @author Светлана Ибраева
 * @version 1.0
 */
public interface ItemService {
    /**
     * Метод добавления вещи в хранилище
     *
     * @param itemDto {@link ItemDto}
     * @return копию объекта itemDto с добавленным id
     */
    ItemDto create(Long userId, ItemDto itemDto);

    /**
     * Метод обновления информации о вещи в хранилище
     *
     * @param userId  - идентификатор пользователя, который обновляет информацию
     * @param itemId  - идентификатор вещи, информацию о котором обновляют
     * @param itemDto - новая информация о вещи {@link ItemDto}
     */
    ItemDto update(Long userId, Long itemId, ItemDto itemDto);

    /**
     * Метод получения вещи по идентификатору их хранилища
     *
     * @param userId - идентификатор пользователя, который запрашивает информацию
     * @param itemId - идентификатор вещи, которую запрашивают
     * @return копию объекта itemDto
     */
    ItemDto getById(Long userId, Long itemId);

    /**
     * Метод получения владельцем всех своих вещей из хранилища
     *
     * @param userId - идентификатор владельца
     * @return список объектов Item
     */
    List<ItemDto> findByOwner(Long userId);

    /**
     * Метод получения списка вещей, в названии или описании которых содержится text
     *
     * @param text - текст
     * @return список вещей, доступных для аренды и содержащх в описании или названии text
     */
    List<ItemDto> search(String text);
}
