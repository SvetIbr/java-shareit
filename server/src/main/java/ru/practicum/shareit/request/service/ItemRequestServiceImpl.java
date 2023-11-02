package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.error.exception.ItemRequestNotFoundException;
import ru.practicum.shareit.error.exception.UserNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository repository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public ItemRequestDto create(Long userId, ItemRequestDto itemRequestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(String.format("Пользователь " +
                        "с идентификатором %d не найден", userId)));
        itemRequestDto.setCreated(LocalDateTime.now());
        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestDto, user);
        return ItemRequestMapper.toItemRequestDto(repository.save(itemRequest), null);
    }

    public List<ItemRequestDto> getAllItemRequestByUser(Long userId) {
        checkUserInUserStorage(userId);
        return repository.getByRequestorIdOrderByCreatedAsc(userId)
                .stream()
                .map(itemRequest -> {
                    List<Item> items = itemRepository.findItemsByRequestIdOrderByIdDesc(itemRequest.getId());
                    return ItemRequestMapper.toItemRequestDto(itemRequest, items);
                })
                .collect(Collectors.toList());
    }

    public ItemRequestDto getItemRequestById(Long userId, Long requestId) {
        checkUserInUserStorage(userId);
        ItemRequest itemRequest = repository.findById(requestId)
                .orElseThrow(() -> new ItemRequestNotFoundException(String.format("Запрос " +
                        "с идентификатором %d не найден", requestId)));
        List<Item> items = itemRepository.findItemsByRequestIdOrderByIdDesc(itemRequest.getId());
        return ItemRequestMapper.toItemRequestDto(itemRequest, items);
    }

    public List<ItemRequestDto> getAllItemRequests(Long userId, Integer from, Integer size) {
        checkUserInUserStorage(userId);
        return repository.getByRequestorIdNotOrderByCreatedDesc(userId,
                        PageRequest.of(from / size, size))
                .stream()
                .map(itemRequest -> {
                    List<Item> items = itemRepository.findItemsByRequestIdOrderByIdDesc(itemRequest.getId());
                    return ItemRequestMapper.toItemRequestDto(itemRequest, items);
                })
                .collect(Collectors.toList());
    }

    private void checkUserInUserStorage(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(String.format("Пользователь " +
                    "с идентификатором %d не найден", userId));
        }
    }
}
