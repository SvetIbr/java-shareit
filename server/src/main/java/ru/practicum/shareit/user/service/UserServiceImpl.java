package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.error.exception.UserNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    @Transactional
    public UserDto create(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        user = repository.save(user);
        return UserMapper.toUserDto(user);
    }

    @Transactional
    public UserDto update(Long id, UserDto userDto) {
        User userToUpdate = repository.findById(id).orElseThrow(() ->
                new UserNotFoundException(String.format("Пользователь " +
                        "с идентификатором %d не найден", id)));

        if (userDto.getEmail() != null) {
            userToUpdate.setEmail(userDto.getEmail());
        }
        if (userDto.getName() != null) {
            userToUpdate.setName(userDto.getName());
        }
        return UserMapper.toUserDto(repository.save(userToUpdate));
    }

    @Transactional
    public UserDto getById(Long id) {
        User user = repository.findById(id).orElseThrow(() ->
                new UserNotFoundException(String.format("Пользователь " +
                        "с идентификатором %d не найден", id)));
        return UserMapper.toUserDto(user);
    }

    @Transactional(readOnly = true)
    public List<UserDto> getAll() {
        return repository.findAll().stream()
                .map(UserMapper::toUserDto).collect(Collectors.toList());
    }

    @Transactional
    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new UserNotFoundException(String.format("Пользователь " +
                    "с идентификатором %d не найден", id));
        }
        repository.deleteById(id);
    }

//    private void checkUserInStorage(Long userId) {
//        if (!repository.existsById(userId)) {
//            throw new UserNotFoundException(String.format("Пользователь " +
//                    "с идентификатором %d не найден", userId));
//        }
//    }
}
