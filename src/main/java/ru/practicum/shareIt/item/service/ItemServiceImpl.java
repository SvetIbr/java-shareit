package ru.practicum.shareIt.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareIt.error.exception.BadRequestException;
import ru.practicum.shareIt.error.exception.ItemNotFoundException;
import ru.practicum.shareIt.error.exception.NoAccessException;
import ru.practicum.shareIt.error.exception.UserNotFoundException;
import ru.practicum.shareIt.item.dto.ItemDto;
import ru.practicum.shareIt.item.mapper.ItemMapper;
import ru.practicum.shareIt.item.model.Item;
import ru.practicum.shareIt.user.model.User;
import ru.practicum.shareIt.item.repository.ItemRepository;
import ru.practicum.shareIt.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository repository;

    private final UserRepository userRepository;

    public ItemDto create(Long userId, ItemDto itemDto) {
        User user = checkUserInUserStorage(userId);
        Item item = repository.create(ItemMapper.toItem(itemDto, user));
        return ItemMapper.toItemDto(item);
    }

    public ItemDto update(Long userId, Long itemId, ItemDto itemDto) {
        if (userId == null) {
            throw new BadRequestException("Не указан идентификатор пользователя");
        }
        if (itemId == null) {
            throw new BadRequestException("Не хватает идентификатора вещи для обновления");
        }

        checkUserInUserStorage(userId);

        Item itemToUpdate = checkItemInStorage(itemId);

        if (!userId.equals(itemToUpdate.getOwner().getId())) {
            throw new NoAccessException("У пользователя нет прав для редактирования данной вещи");
        }
        if (itemDto.getName() != null) itemToUpdate.setName(itemDto.getName());
        if (itemDto.getDescription() != null) itemToUpdate.setDescription(itemDto.getDescription());
        if (itemDto.getAvailable() != null) itemToUpdate.setAvailable(itemDto.getAvailable());

        repository.update(itemToUpdate);
        return ItemMapper.toItemDto(itemToUpdate);
    }

    public ItemDto getById(Long userId, Long itemId) {
        if (userId == null) {
            throw new BadRequestException("Не указан идентификатор пользователя");
        }
        if (itemId == null) {
            throw new BadRequestException("Не указан идентификатор вещи для обновления информации");
        }

        checkUserInUserStorage(userId);

        Item item = checkItemInStorage(itemId);

        return ItemMapper.toItemDto(item);
    }

    public List<ItemDto> findByOwner(Long userId) {
        if (userId == null) {
            throw new BadRequestException("Не указан идентификатор владельца");
        }

        checkUserInUserStorage(userId);

        return repository.getByOwner(userId).stream()
                .map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    public List<ItemDto> search(String text) {
        if (text == null || text.isBlank() || text.isEmpty()) return new ArrayList<>();
        return repository.search(text).stream()
                .map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    private User checkUserInUserStorage(Long userId) {
        User user = userRepository.getById(userId);
        if (user == null) {
            throw new UserNotFoundException(String.format("Пользователь " +
                    "с идентификатором %d не найден", userId));
        }
        return user;
    }

    private Item checkItemInStorage(Long itemId) {
        Item item = repository.getById(itemId);
        if (item == null) {
            throw new ItemNotFoundException(String.format("Вещь " +
                    "с идентификатором %d не найдена", itemId));
        }
        return item;
    }
}
