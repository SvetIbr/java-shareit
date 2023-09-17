package ru.practicum.shareIt.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareIt.error.exception.BadRequestException;
import ru.practicum.shareIt.error.exception.DuplicateEmailException;
import ru.practicum.shareIt.error.exception.UserNotFoundException;
import ru.practicum.shareIt.item.model.Item;
import ru.practicum.shareIt.item.repository.ItemRepository;
import ru.practicum.shareIt.user.dto.UserDto;
import ru.practicum.shareIt.user.mapper.UserMapper;
import ru.practicum.shareIt.user.model.User;
import ru.practicum.shareIt.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final ItemRepository itemRepository;

    public UserDto create(UserDto userDto) {
        checkDuplicateEmail(userDto.getEmail());
        User user = repository.create(UserMapper.toUser(userDto));
        return UserMapper.toUserDto(user);
    }

    public UserDto update(Long id, UserDto userDto) {
        if (id == null) {
            throw new BadRequestException("Не указан идентификатор пользователя " +
                    "для обновления информации");
        }
        User userToUpdate = checkUserInStorage(id);

        if (userDto.getEmail() != null) {
            if (!userDto.getEmail().equals(userToUpdate.getEmail())) {
                checkDuplicateEmail(userDto.getEmail());
            }
            userToUpdate.setEmail(userDto.getEmail());
        }
        if (userDto.getName() != null) {
            userToUpdate.setName(userDto.getName());
        }
        repository.update(userToUpdate);
        return UserMapper.toUserDto(userToUpdate);
    }

    public UserDto getById(Long id) {
        User user = checkUserInStorage(id);
        return UserMapper.toUserDto(user);
    }

    public List<UserDto> getAll() {
        return repository.getAll().stream()
                .map(UserMapper::toUserDto).collect(Collectors.toList());
    }

    public void deleteById(Long id) {
        checkUserInStorage(id);
        List<Long> idsItemsOfUser = itemRepository.getByOwner(id).stream()
                .map(Item::getId).collect(Collectors.toList());
        for (Long curId : idsItemsOfUser) {
            itemRepository.deleteById(curId);
        }
        repository.deleteById(id);
    }

    private User checkUserInStorage(Long userId) {
        User user = repository.getById(userId);
        if (user == null) {
            throw new UserNotFoundException(String.format("Пользователь " +
                    "с идентификатором %d не найден", userId));
        }
        return user;
    }

    private void checkDuplicateEmail(String email) {
        if (repository.isDuplicateEmail(email)) {
            throw new DuplicateEmailException(String.format("Пользователь с email %s " +
                    "уже зарегистрирован", email));
        }
    }
}
