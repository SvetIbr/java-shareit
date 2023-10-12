package ru.practicum.shareIt.booking.service;

import ru.practicum.shareIt.booking.dto.BookingRequestDto;
import ru.practicum.shareIt.booking.dto.BookingResponseDto;

import java.util.List;

/**
 * Интерфейс сервиса бронирований
 *
 * @author Светлана Ибраева
 * @version 1.0
 */
public interface BookingService {
    /**
     * Метод добавления бронирования в хранилище
     *
     * @param userId     - идентификатор пользователя, создающего бронирование
     * @param bookingDto {@link BookingRequestDto}
     * @return {@link BookingResponseDto} с добавленным id и код ответа API 201
     */
    BookingResponseDto create(long userId, BookingRequestDto bookingDto);

    /**
     * Метод подтверждения/отклонения запроса на бронирование вещи
     *
     * @param userId    - идентификатор владельца вещи
     * @param bookingId - идентификатор бронирования
     * @param approved  - значение смены статуса: true, если подтверждение запроса на бронирование,
     *                  false, если отклонение запроса
     * @return {@link BookingResponseDto}
     */
    BookingResponseDto approve(Long userId, Long bookingId, Boolean approved);

    /**
     * Метод получения информации о конкретном бронировании из хранилища.
     * Может быть выполнено либо автором бронирования,
     * либо владельцем вещи, к которой относится бронирование.
     *
     * @param userId    - идентификатор пользователя, запрашивающего информацию
     * @param bookingId - идентификатор бронирования
     * @return {@link BookingResponseDto}
     */
    BookingResponseDto getById(Long userId, Long bookingId);

    /**
     * Метод получения списка всех бронирований текущего пользователя из хранилища
     *
     * @param userId - идентификатор пользователя, запрашивающего список
     * @return список объектов {@link BookingResponseDto}
     */
    List<BookingResponseDto> getAllBookingByUser(Long userId, String state, Integer from, Integer size);

    /**
     * Метод получения списка бронирований для всех вещей текущего пользователя
     *
     * @param userId - идентификатор пользователя, запрашивающего список (владелец)
     * @return список объектов {@link BookingResponseDto}
     */
    List<BookingResponseDto> getAllBookingByOwner(Long userId, String state, Integer from, Integer size);
}
