package ru.practicum.shareIt.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareIt.booking.dto.BookingRequestDto;
import ru.practicum.shareIt.booking.dto.BookingResponseDto;
import ru.practicum.shareIt.booking.service.BookingService;

import javax.validation.Valid;

import java.util.List;

import static ru.practicum.shareIt.item.controller.ItemController.HEADER_WITH_OWNER_ID;

/**
 * Класс контроллера для работы с запросами к сервису бронирования
 *
 * @author Светлана Ибраева
 * @version 1.0
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    /**
     * Поле сервис для работы с хранилищем бронирования
     */
    private final BookingService service;

    /**
     * Метод добавления бронирования в хранилище сервиса через запрос
     *
     * @param userId     - идентификатор пользователя, создающего бронирование
     * @param bookingDto {@link BookingRequestDto}
     * @return {@link BookingResponseDto} с добавленным id и код ответа API 201
     */
    @PostMapping
    public BookingResponseDto create(@RequestHeader(HEADER_WITH_OWNER_ID) long userId,
                                     @Valid @RequestBody BookingRequestDto bookingDto) {
        return new ResponseEntity<>(service.create(userId, bookingDto), HttpStatus.CREATED).getBody();
    }

    /**
     * Метод подтверждения/отклонения запроса на бронирование вещи
     *
     * @param userId    - идентификатор владельца вещи
     * @param bookingId - идентификатор бронирования
     * @param approved  - значение смены статуса: true, если подтверждение запроса на бронирование,
     *                  false, если отклонение запроса
     * @return {@link BookingResponseDto}
     */
    @PatchMapping("/{bookingId}")
    public BookingResponseDto approve(@RequestHeader(HEADER_WITH_OWNER_ID) Long userId,
                                      @PathVariable Long bookingId,
                                      @RequestParam Boolean approved) {
        return service.approve(userId, bookingId, approved);
    }

    /**
     * Метод получения информации о конкретном бронировании из хранилища сервиса
     * по идентификатору через запрос. Может быть выполнено либо автором бронирования,
     * либо владельцем вещи, к которой относится бронирование.
     *
     * @param userId    - идентификатор пользователя, запрашивающего информацию
     * @param bookingId - идентификатор бронирования
     * @return {@link BookingResponseDto}
     */
    @GetMapping("/{bookingId}")
    public BookingResponseDto getById(@RequestHeader(HEADER_WITH_OWNER_ID) Long userId,
                                      @PathVariable Long bookingId) {
        return service.getById(userId, bookingId);
    }

    /**
     * Метод получения списка всех бронирований текущего пользователя из хранилища сервиса через запрос
     *
     * @param userId - идентификатор пользователя, запрашивающего список
     * @param state  - состояние запрашиваемых бронирований
     * @param from   - индекс первого элемента, начиная с 0
     * @param size   - количество элементов для отображения
     * @return список объектов {@link BookingResponseDto}
     */
    @GetMapping
    public List<BookingResponseDto> get(@RequestHeader(HEADER_WITH_OWNER_ID) Long userId,
                                        @RequestParam(defaultValue = "ALL", required = false) String state,
                                        @RequestParam(required = false, defaultValue = "0") Integer from,
                                        @RequestParam(required = false, defaultValue = "10") Integer size) {
        return service.getAllBookingByUser(userId, state, from, size);
    }

    /**
     * Метод получения списка бронирований для всех вещей текущего пользователя
     * из хранилища сервиса через запрос
     *
     * @param userId - идентификатор пользователя, запрашивающего список (владелец)
     * @param state  - состояние запрашиваемых бронирований
     * @param from   - индекс первого элемента, начиная с 0
     * @param size   - количество элементов для отображения
     * @return список объектов {@link BookingResponseDto}
     */
    @GetMapping("/owner")
    public List<BookingResponseDto> getByOwner(@RequestHeader(HEADER_WITH_OWNER_ID) Long userId,
                                               @RequestParam(defaultValue = "ALL", required = false) String state,
                                               @RequestParam(required = false, defaultValue = "0") Integer from,
                                               @RequestParam(required = false, defaultValue = "10") Integer size) {
        return service.getAllBookingByOwner(userId, state, from, size);
    }
}
