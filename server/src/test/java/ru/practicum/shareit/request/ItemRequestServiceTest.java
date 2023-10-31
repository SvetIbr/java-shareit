package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.error.exception.BadRequestException;
import ru.practicum.shareit.error.exception.ItemRequestNotFoundException;
import ru.practicum.shareit.error.exception.UserNotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class ItemRequestServiceTest {
    private final ItemRequestRepository itemRequestRepository;
    private final ItemRequestService itemRequestService;
    private final User user = new User(1L, "user1", "user1@mail.ru");
    private final User user2 = new User(2L, "user2", "user2@mail.ru");
    private final ItemRequest itemRequest = new ItemRequest(1L, "дрель", user, LocalDateTime.now());

    @Autowired
    public ItemRequestServiceTest(ItemRequestRepository itemRequestRepository,
                                  ItemRequestService itemRequestService, UserService userService) {
        this.itemRequestRepository = itemRequestRepository;
        this.itemRequestService = itemRequestService;
        userService.create(UserMapper.toUserDto(user));
        userService.create(UserMapper.toUserDto(user2));
        itemRequestRepository.save(itemRequest);
    }

    @Test
    void createTest() {
        ItemRequestDto itemRequestAfterCreate = itemRequestService
                .create(itemRequest.getRequestor().getId(),
                        ItemRequestMapper.toItemRequestDto(itemRequest, new ArrayList<>()));
        assertEquals(ItemRequestMapper.toItemRequestDto(itemRequestRepository
                        .findById(itemRequestAfterCreate.getId()).orElseThrow(), new ArrayList<>()).getId(),
                itemRequestAfterCreate.getId());
    }

    @Test
    void createWithFailUserIdTest() {
        final UserNotFoundException exception = assertThrows(UserNotFoundException.class, ()
                -> itemRequestService.create(5L,
                ItemRequestMapper.toItemRequestDto(itemRequest, new ArrayList<>())));
        assertEquals("Пользователь с идентификатором 5 не найден",
                exception.getMessage());
    }

    @Test
    void getAllItemRequestByUserTest() {
        assertEquals(itemRequestService.getAllItemRequestByUser(user.getId()).get(0).getId(),
                List.of(ItemRequestMapper.toItemRequestDto(itemRequest, new ArrayList<>())).get(0).getId());
    }

    @Test
    void getAllItemRequestWithFailUserIdTest() {
        final UserNotFoundException exception = assertThrows(UserNotFoundException.class, ()
                -> itemRequestService.getAllItemRequests(21L, 0, 10));
        assertEquals("Пользователь с идентификатором 21 не найден",
                exception.getMessage());
    }

    @Test
    void getItemRequestByIdTest() {
        assertEquals(itemRequestService.getItemRequestById(itemRequest.getId(),
                user.getId()).getId(), ItemRequestMapper.toItemRequestDto(itemRequest, new ArrayList<>()).getId());
    }

    @Test
    void getItemRequestByIdWithFailIdTest() {
        final ItemRequestNotFoundException exception = assertThrows(ItemRequestNotFoundException.class, ()
                -> itemRequestService.getItemRequestById(user.getId(), 16L));
        assertEquals("Запрос с идентификатором 16 не найден",
                exception.getMessage());
    }

    @Test
    void getAllItemRequestsTest() {
        assertEquals(itemRequestService.getAllItemRequests(user.getId(), 0, 10),
                new ArrayList<>());
    }

    @Test
    void getAllItemRequestsNotOwnerTest() {
        assertEquals(itemRequestService.getAllItemRequests(user2.getId(), 0, 10).get(0).getId(),
                List.of(ItemRequestMapper.toItemRequestDto(itemRequest, new ArrayList<>())).get(0).getId());
    }
}
