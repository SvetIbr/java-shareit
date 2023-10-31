package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.error.exception.BadRequestException;
import ru.practicum.shareit.error.exception.UserNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

import javax.transaction.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    private final UserService userService;
    private final User user1 = new User(1L, "user1", "user1@mail.ru");
    private final User user2 = new User(2L, "user2", "user2@mail.ru");

    @Autowired
    public UserServiceTest(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
        userRepository.save(user1);
        userRepository.save(user2);
    }

    @Test
    void createUserTest() {
        UserDto newUser = userService.create(UserMapper.toUserDto(user1));
        System.out.println(newUser);
        assertEquals(userService.getById(newUser.getId()).getId(), user1.getId());
        assertEquals(userService.getById(newUser.getId()).getName(), user1.getName());
        assertEquals(userService.getById(newUser.getId()).getEmail(), user1.getEmail());
    }

    @Test
    void updateUserTest() {
        UserDto userDto = userService.getById(1L);
        userDto.setName("updateUser1");
        userDto.setEmail("updateUser1@mail.ru");
        userService.update(1L, userDto);
        assertEquals("updateUser1", userService.getById(1L).getName());
        assertEquals("updateUser1@mail.ru", userService.getById(1L).getEmail());
    }

    @Test
    void updateUserWithFailIdTest() {
        UserDto userDto = userService.getById(1L);
        final UserNotFoundException exception = assertThrows(UserNotFoundException.class, ()
                -> userService.update(7L, userDto));
        assertEquals("Пользователь с идентификатором 7 не найден", exception.getMessage());
    }

    @Test
    void getByIdTest() {
        assertEquals(UserMapper.toUserDto(user1).getId(), userService.getById(1L).getId());
        assertEquals(UserMapper.toUserDto(user1).getName(), userService.getById(1L).getName());
        assertEquals(UserMapper.toUserDto(user1).getEmail(), userService.getById(1L).getEmail());
    }

    @Test
    void getByIdWithFailIdTest() {
        final UserNotFoundException exception = assertThrows(UserNotFoundException.class, ()
                -> userService.getById(8L));
        assertEquals("Пользователь с идентификатором 8 не найден", exception.getMessage());
    }

    @Test
    void getAllTest() {
        List<UserDto> usersFromStorage = userService.getAll();
        assertEquals(2, usersFromStorage.size());
        assertEquals(UserMapper.toUserDto(user1), usersFromStorage.get(0));
        assertEquals(UserMapper.toUserDto(user2), usersFromStorage.get(1));
    }

    @Test
    void deleteByIdTest() {
        userService.deleteById(1L);
        assertNull(userRepository.findById(1L).orElse(null));
    }

    @Test
    void deleteByIdWithFailIdTest() {
        final UserNotFoundException exception = assertThrows(UserNotFoundException.class, ()
                -> userService.deleteById(9L));
        assertEquals("Пользователь с идентификатором 9 не найден", exception.getMessage());
    }
}
