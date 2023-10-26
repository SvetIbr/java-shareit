package ru.practicum.shareIt.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareIt.error.exception.BadRequestException;
import ru.practicum.shareIt.error.exception.ItemRequestNotFoundException;
import ru.practicum.shareIt.error.exception.UserNotFoundException;
import ru.practicum.shareIt.item.model.Item;
import ru.practicum.shareIt.item.repository.ItemRepository;
import ru.practicum.shareIt.request.dto.ItemRequestDto;
import ru.practicum.shareIt.request.mapper.ItemRequestMapper;
import ru.practicum.shareIt.request.model.ItemRequest;
import ru.practicum.shareIt.request.repository.ItemRequestRepository;
import ru.practicum.shareIt.user.model.User;
import ru.practicum.shareIt.user.repository.UserRepository;

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
        if (userId == null) {
            throw new BadRequestException("Не указан идентификатор владельца");
        }
        checkUserInUserStorage(userId);
        if (from < 0 || size <= 0) {
            throw new BadRequestException("Параметры для отображения данных " +
                    "заданы не верно (начало не может быть меньше 0, а размер - меньше 1)");
        }
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
