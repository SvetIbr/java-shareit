package ru.practicum.shareIt.booking.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareIt.booking.status.BookingStatus;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Класс бронирования со свойствами <b>id</b>, <b>start</b>, <b>end</b>,
 * <b>itemId</b>, <b>bookerId</b> и <b>status</b> для работы через REST-интерфейс
 *
 * @author Светлана Ибраева
 * @version 1.0
 */
@Data
@Builder
public class BookingRequestDto {
    /**
     * Поле идентификатор
     */
    private Long id;

    /**
     * Поле дата начала бронирования
     */
    @NotNull
    @FutureOrPresent(message = "Дата старта не может быть в прошлом")
    private LocalDateTime start;

    /**
     * Поле дата окончания бронирования
     */
    @NotNull
    @Future(message = "Дата окончания не может быть в прошлом или настоящем")
    private LocalDateTime end;

    /**
     * Поле идентификатор арендуемой вещи
     */
    @NotNull
    private Long itemId;

    /**
     * Поле идентификатор арендатора вещи
     */
    private Long bookerId;

    /**
     * Поле статус бронирования
     */
    private BookingStatus status;
}

