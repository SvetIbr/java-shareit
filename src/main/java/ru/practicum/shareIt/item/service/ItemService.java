package ru.practicum.shareIt.item.service;

import ru.practicum.shareIt.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto create(Long userId, ItemDto itemDto);

    ItemDto update(Long userId, Long itemId, ItemDto itemDto);

    ItemDto getById(Long userId, Long itemId);

    List<ItemDto> findByOwner(Long userId);

    List<ItemDto> search(String text);
}
