package ru.practicum.shareIt.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareIt.error.exception.BadRequestException;
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

    @Transactional
    public UserDto create(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        user = repository.save(user);
        return UserMapper.toUserDto(user);
    }

    @Transactional
    public UserDto update(Long id, UserDto userDto) {
        if (id == null) {
            throw new BadRequestException("Не указан идентификатор пользователя " +
                    "для обновления информации");
        }
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
        checkUserInStorage(id);
        User user = repository.findById(id).get();
        return UserMapper.toUserDto(user);
    }

    @Transactional(readOnly = true)
    public List<UserDto> getAll() {
        return repository.findAll().stream()
                .map(UserMapper::toUserDto).collect(Collectors.toList());
    }

    @Transactional
    public void deleteById(Long id) {
        checkUserInStorage(id);
        List<Long> idsItemsOfUser = itemRepository.findAllByOwnerId(id).stream()
                .map(Item::getId).collect(Collectors.toList());
        for (Long curId : idsItemsOfUser) {
            itemRepository.deleteById(curId);
        }
        repository.deleteById(id);
    }

    private void checkUserInStorage(Long userId) {
        if (!repository.existsById(userId)) {
            throw new UserNotFoundException(String.format("Пользователь " +
                    "с идентификатором %d не найден", userId));
        }
    }
}
