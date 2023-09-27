package ru.practicum.shareIt.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareIt.error.exception.*;
import ru.practicum.shareIt.item.model.Item;
import ru.practicum.shareIt.user.model.User;

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
     * искомых объектов user{@link ru.practicum.shareIt.user.model.User} по идентификатору
     *
     * @return сообщение с описанием причины возникновения ошибки и статусом 404
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserNotFoundException(final UserNotFoundException e) {
        return new ErrorResponse(e.getMessage());
    }

    /**
     * Метод обработки исключений при обнаружении email{@link User#getEmail()},
     * идентичного одному из уже имеющихся в хранилище
     *
     * @return сообщение с описанием причины возникновения ошибки и статусом 409
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDuplicateEmailException(final DuplicateEmailException e) {
        return new ErrorResponse(e.getMessage());
    }

    /**
     * Метод обработки исключений при отсутствии необходимых полей в запросах
     * с объектами user{@link ru.practicum.shareIt.user.model.User}
     * и item{@link ru.practicum.shareIt.item.model.Item}
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
     * искомых объектов item{@link ru.practicum.shareIt.item.model.Item} по идентификатору
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
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleNoAccessException(final NoAccessException e) {
        return new ErrorResponse(e.getMessage());
    }
}

