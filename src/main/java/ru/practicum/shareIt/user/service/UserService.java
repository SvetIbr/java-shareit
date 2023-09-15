package ru.practicum.shareIt.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareIt.error.exception.BadRequestException;
import ru.practicum.shareIt.error.exception.DuplicateEmailException;
import ru.practicum.shareIt.error.exception.UserNotFoundException;
import ru.practicum.shareIt.user.dto.UserDto;
import ru.practicum.shareIt.user.mapper.UserMapper;
import ru.practicum.shareIt.user.model.User;
import ru.practicum.shareIt.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    public UserDto create(UserDto userDto) {
        if (repository.isDuplicateEmail(userDto.getEmail())) {
            throw new DuplicateEmailException("Пользователь с email "
                    + userDto.getEmail() + " уже есть в базе");
        }
        User user = repository.create(UserMapper.toUser(userDto));
        return UserMapper.toUserDto(user);
    }

    public UserDto update(Long id, UserDto userDto) {
        if (id == null) {
            throw new BadRequestException("Не хватает идентификатора для обновления пользователя");
        }
        User userToUpdate = checkUserInStorage(id);

        if (userDto.getEmail() != null) {
            if (!userDto.getEmail().equals(userToUpdate.getEmail())) {
                if (repository.isDuplicateEmail(userDto.getEmail())) {
                    throw new DuplicateEmailException("Пользователь с email "
                            + userDto.getEmail() + " уже есть в базе");
                }
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
        repository.deleteById(id);
    }

    private User checkUserInStorage(Long userId) {
        User user = repository.getById(userId);
        if (user == null) {
            throw new UserNotFoundException("Пользователь с идентификатором " + userId + " не найден");
        }
        return user;
    }
}
