package ru.practicum.shareIt.user;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareIt.user.dto.UserDto;
import ru.practicum.shareIt.user.mapper.UserMapper;
import ru.practicum.shareIt.user.model.User;
import ru.practicum.shareIt.user.repository.UserRepository;
import ru.practicum.shareIt.user.service.UserService;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@Transactional
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    private final User user1;
    private final User user2;
    private final User user3 = new User(3L, "user3", "user2@mail.ru");

    @Autowired
    public UserServiceTest(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
        userRepository.save(user1 = new User(1L, "user1", "user1@mail.ru"));
        userRepository.save(user2 = new User(2L, "user2", "user2@mail.ru"));
    }

    @Test
    void createUserTest() {
        UserDto newUser = userService.create(UserMapper.toUserDto(user1));
        assertEquals(userService.getById(newUser.getId()).getId(), user1.getId());
    }
}
