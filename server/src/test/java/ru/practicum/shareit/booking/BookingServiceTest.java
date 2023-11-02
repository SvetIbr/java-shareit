package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.status.BookingStatus;
import ru.practicum.shareit.error.exception.*;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
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
public class BookingServiceTest {
    private final BookingRepository bookingRepository;
    private final BookingService bookingService;
    private final User user = new User(1L, "user1", "user1@mail.ru");
    private final User user2 = new User(2L, "user2", "user2@mail.ru");
    private final User user3 = new User(3L, "user3", "user3@mail.ru");
    private final Item item = new Item(1L, "Дрель", "Простая дрель",
            true, user, null);
    private final Item itemFalseAvailable = new Item(2L, "Молоток", "Простой молоток",
            false, user, null);
    private final Booking booking = new Booking(1L, LocalDateTime.now().plusDays(10),
            LocalDateTime.now().plusDays(20),
            item, user2, BookingStatus.WAITING);
    private final Booking bookingApprove = new Booking(2L, LocalDateTime.now().plusDays(1),
            LocalDateTime.now().plusDays(3),
            item, user2, BookingStatus.APPROVED);
    private final Booking bookingReject = new Booking(3L, LocalDateTime.now().plusDays(5),
            LocalDateTime.now().plusDays(6),
            item, user2, BookingStatus.REJECTED);


    @Autowired
    public BookingServiceTest(BookingRepository bookingRepository,
                              BookingService bookingService,
                              ItemService itemService,
                              UserService userService) {
        this.bookingRepository = bookingRepository;
        this.bookingService = bookingService;
        userService.create(UserMapper.toUserDto(user));
        userService.create(UserMapper.toUserDto(user2));
        userService.create(UserMapper.toUserDto(user3));
        itemService.create(item.getOwner().getId(), ItemMapper.toItemDto(item));
        itemService.create(itemFalseAvailable.getOwner().getId(), ItemMapper.toItemDto(itemFalseAvailable));
        bookingRepository.save(booking);
        bookingRepository.save(bookingApprove);
        bookingRepository.save(bookingReject);
    }

    @Test
    void createBookingTest() {
        BookingRequestDto request = BookingRequestDto.builder()
                .start(LocalDateTime.now().plusMinutes(1))
                .end(LocalDateTime.now().plusDays(2))
                .itemId(1L)
                .build();
        BookingResponseDto response = bookingService.create(2L, request);
        Booking bookingFromStorage = bookingRepository.findById(response.getId()).get();
        assertEquals(response.getId(), bookingFromStorage.getId());
        assertEquals(response.getStart(), bookingFromStorage.getStart());
        assertEquals(response.getEnd(), bookingFromStorage.getEnd());
        assertEquals(response.getItem().getId(), bookingFromStorage.getItem().getId());
        assertEquals(response.getItem().getName(), bookingFromStorage.getItem().getName());
        assertEquals(response.getBooker().getId(), bookingFromStorage.getBooker().getId());
        assertEquals(response.getStatus(), bookingFromStorage.getStatus());
    }

    @Test
    void createWithFailUserIdTest() {
        BookingRequestDto request = BookingRequestDto.builder()
                .start(LocalDateTime.now().plusMinutes(1))
                .end(LocalDateTime.now().plusDays(2))
                .itemId(1L)
                .build();
        final UserNotFoundException exception = assertThrows(UserNotFoundException.class, ()
                -> bookingService.create(23L, request));
        assertEquals("Пользователь с идентификатором 23 не найден",
                exception.getMessage());
    }

    @Test
    void createWithFailItemIdTest() {
        BookingRequestDto request = BookingRequestDto.builder()
                .start(LocalDateTime.now().plusMinutes(1))
                .end(LocalDateTime.now().plusDays(2))
                .itemId(56L)
                .build();
        final ItemNotFoundException exception = assertThrows(ItemNotFoundException.class, ()
                -> bookingService.create(2L, request));
        assertEquals("Вещь с идентификатором 56 не найдена",
                exception.getMessage());
    }

    @Test
    void createByOwnerTest() {
        BookingRequestDto request = BookingRequestDto.builder()
                .start(LocalDateTime.now().plusMinutes(1))
                .end(LocalDateTime.now().plusDays(2))
                .itemId(1L)
                .build();
        final UserNotFoundException exception = assertThrows(UserNotFoundException.class, ()
                -> bookingService.create(1L, request));
        assertEquals("Пользователь для заявки на бронирование не найден",
                exception.getMessage());
    }

    @Test
    void createWithStartAfterEndTest() {
        BookingRequestDto request = BookingRequestDto.builder()
                .start(LocalDateTime.now().plusDays(9))
                .end(LocalDateTime.now().plusDays(2))
                .itemId(1L)
                .build();
        final InvalidDateTimeException exception = assertThrows(InvalidDateTimeException.class, ()
                -> bookingService.create(2L, request));
        assertEquals("Дата окончания бронирования не может быть раньше даты начала",
                exception.getMessage());
    }

    @Test
    void createWithNullAvailableTest() {
        BookingRequestDto request = BookingRequestDto.builder()
                .start(LocalDateTime.now().plusDays(9))
                .end(LocalDateTime.now().plusDays(2))
                .itemId(2L)
                .build();
        final BadRequestException exception = assertThrows(BadRequestException.class, ()
                -> bookingService.create(2L, request));
        assertEquals("Не указан статус доступности вещи",
                exception.getMessage());
    }

    @Test
    void approveWithRejectedStatusTest() {
        bookingService.approve(user.getId(), booking.getId(), false);
        assertEquals(BookingStatus.REJECTED, bookingRepository.findById(booking.getId()).orElseThrow().getStatus());
    }

    @Test
    void approveWithApprovedStatusTest() {
        bookingService.approve(user.getId(), booking.getId(), true);
        assertEquals(BookingStatus.APPROVED, bookingRepository.findById(booking.getId()).orElseThrow().getStatus());
    }


    @Test
    void approveAlreadyApprovedTest() {
        assertThrows(InvalidStatusException.class, () -> bookingService
                .approve(user.getId(), bookingApprove.getId(), true));
    }

    @Test
    void approveAlreadyRejectedTest() {
        assertThrows(InvalidStatusException.class, () -> bookingService
                .approve(user.getId(), bookingReject.getId(), false));
    }

    @Test
    void updateBookingApprovedByNotOwnerTest() {
        assertThrows(NoAccessException.class, () -> bookingService
                .approve(user2.getId(), booking.getId(), true));
    }

    @Test
    void getByIdTest() {
        assertEquals(booking.getId(),
                bookingService.getById(booking.getBooker().getId(), booking.getId()).getId());
    }

    @Test
    void getByIdWithFailBookingIdTest() {
        final BookingNotFoundException exception = assertThrows(BookingNotFoundException.class, ()
                -> bookingService.getById(1L, 16L));
        assertEquals("Заявка на бронирование с идентификатором 16 не найдена",
                exception.getMessage());
    }

    @Test
    void getByIdWithAccessExceptionTest() {
        final NoAccessException exception = assertThrows(NoAccessException.class, ()
                -> bookingService.getById(3L, 1L));
        assertEquals("У пользователя нет прав для просмотра данной заявки на бронирование",
                exception.getMessage());
    }

    @Test
    void getAllBookingByUserTest() {
        List<BookingResponseDto> items = bookingService.getAllBookingByUser(user2.getId(), "ALL", 0, 10);
        assertEquals(3, items.size());
        assertEquals(1, items.get(0).getId());
        assertEquals(3, items.get(1).getId());
        assertEquals(2, items.get(2).getId());
    }

    @Test
    void getAllBookingByUserWithFaiUserIdTest() {
        final UserNotFoundException exception = assertThrows(UserNotFoundException.class, ()
                -> bookingService.getAllBookingByUser(19L, "ALL", 0, 10));
        assertEquals("Пользователь с идентификатором 19 не найден",
                exception.getMessage());
    }

    @Test
    void getPastBookingByUserTest() {
        assertEquals(new ArrayList<>(),
                bookingService.getAllBookingByUser(user2.getId(), "PAST", 0, 10));
    }

    @Test
    void getFutureBookingByUserTest() {
        List<BookingResponseDto> items = bookingService.getAllBookingByUser(user2.getId(), "FUTURE", 0, 10);
        assertEquals(3, items.size());
        assertEquals(1, items.get(0).getId());
        assertEquals(3, items.get(1).getId());
        assertEquals(2, items.get(2).getId());
    }

    @Test
    void getCurrentBookingByUserTest() {
        assertEquals(new ArrayList<>(),
                bookingService.getAllBookingByUser(user2.getId(), "CURRENT", 0, 10));
    }

    @Test
    void getWaitingBookingByUserTest() {
        assertEquals(List.of(booking).get(0).getId(),
                bookingService.getAllBookingByUser(user2.getId(), "WAITING", 0, 10).get(0).getId());
    }

    @Test
    void getRejectedBookingByUserTest() {
        assertEquals(List.of(bookingReject).get(0).getId(),
                bookingService.getAllBookingByUser(user2.getId(), "REJECTED", 0, 10).get(0).getId());
    }

    @Test
    void getAllBookingByOwnerTest() {
        List<BookingResponseDto> items = bookingService.getAllBookingByOwner(user.getId(), "ALL", 0, 10);
        assertEquals(3, items.size());
        assertEquals(1, items.get(0).getId());
        assertEquals(3, items.get(1).getId());
        assertEquals(2, items.get(2).getId());
    }

    @Test
    void getPastBookingByOwnerTest() {
        assertEquals(new ArrayList<>(),
                bookingService.getAllBookingByOwner(user.getId(), "PAST", 0, 10));
    }

    @Test
    void getFutureBookingByOwnerTest() {
        List<BookingResponseDto> items = bookingService.getAllBookingByOwner(user.getId(), "FUTURE", 0, 10);
        assertEquals(3, items.size());
        assertEquals(1, items.get(0).getId());
        assertEquals(3, items.get(1).getId());
        assertEquals(2, items.get(2).getId());
    }

    @Test
    void getCurrentBookingByOwnerTest() {
        assertEquals(0,
                bookingService.getAllBookingByOwner(user.getId(), "CURRENT", 0, 10).size());
    }

    @Test
    void getWaitingBookingByOwnerTest() {
        assertEquals(List.of(booking).get(0).getId(),
                bookingService.getAllBookingByOwner(user.getId(), "WAITING", 0, 10).get(0).getId());
    }

    @Test
    void getRejectedBookingByOwnerTest() {
        assertEquals(List.of(bookingReject).get(0).getId(),
                bookingService.getAllBookingByOwner(user.getId(), "REJECTED", 0, 10).get(0).getId());
    }
}
