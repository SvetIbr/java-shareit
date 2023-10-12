package ru.practicum.shareIt.request.service;

import ru.practicum.shareIt.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {

    ItemRequestDto create(Long userId, ItemRequestDto itemRequestDto);

    List<ItemRequestDto> getAllItemRequestByUser(Long userId);

    ItemRequestDto getItemRequestById(Long userId, Long requestId);

    List<ItemRequestDto> getAllItemRequests(Long userId, Integer from, Integer size);
}
