package ru.practicum.shareit.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.error.exception.*;
import ru.practicum.shareit.item.model.Item;

/**
 * Класс обработчика возникающих исключений
 *
 * @author Светлана Ибраева
 * @version 1.0
 */
@RestControllerAdvice
public class ErrorHandler {
    /**
     * Метод обработки исключений при отсутствии
     * искомых объектов user{@link ru.practicum.shareit.user.model.User} по идентификатору
     *
     * @return сообщение с описанием причины возникновения ошибки и статусом 404
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserNotFoundException(final UserNotFoundException e) {
        return new ErrorResponse(e.getMessage());
    }

    /**
     * Метод обработки исключений при отсутствии необходимых полей в запросах
     * с объектами user{@link ru.practicum.shareit.user.model.User}
     * и item{@link ru.practicum.shareit.item.model.Item}
     *
     * @return сообщение с описанием причины возникновения ошибки и статусом 400
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequestException(final BadRequestException e) {
        return new ErrorResponse(e.getMessage());
    }

    /**
     * Метод обработки исключений при отсутствии
     * искомых объектов item{@link ru.practicum.shareit.item.model.Item} по идентификатору
     *
     * @return сообщение с описанием причины возникновения ошибки и статусом 404
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleItemNotFoundException(final ItemNotFoundException e) {
        return new ErrorResponse(e.getMessage());
    }

    /**
     * Метод обработки исключений при попытке редактирования параметров вещи пользователем,
     * который не является владельцем данной вещи {@link Item#getOwner()}
     *
     * @return сообщение с описанием причины возникновения ошибки и статусом 403
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNoAccessException(final NoAccessException e) {
        return new ErrorResponse(e.getMessage());
    }

    /**
     * Метод обработки исключений при отсутствии
     * искомых объектов booking {@link ru.practicum.shareit.booking.model.Booking} по идентификатору
     *
     * @return сообщение с описанием причины возникновения ошибки и статусом 404
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleBookingNotFoundException(final BookingNotFoundException e) {
        return new ErrorResponse(e.getMessage());
    }

    /**
     * Метод обработки исключений при статусе бронирования, который не предусмотрен списком статусов
     *
     * @return сообщение с описанием причины возникновения ошибки и статусом 400
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInvalidStatusException(final InvalidStatusException e) {
        return new ErrorResponse(e.getMessage());
    }

    /**
     * Метод обработки исключений при невозможных значения времени бронирования
     *
     * @return сообщение с описанием причины возникновения ошибки и статусом 400
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInvalidDateTimeException(final InvalidDateTimeException e) {
        return new ErrorResponse(e.getMessage());
    }

    /**
     * Метод обработки исключений при отсутствии
     * искомых объектов ItemRequest {@link ru.practicum.shareit.request.model.ItemRequest} по идентификатору
     *
     * @return сообщение с описанием причины возникновения ошибки и статусом 404
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleItemRequestNotFoundException(final ItemRequestNotFoundException e) {
        return new ErrorResponse(e.getMessage());
    }
}

