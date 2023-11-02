package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.status.BookingStatus;

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
    private LocalDateTime start;

    /**
     * Поле дата окончания бронирования
     */
    private LocalDateTime end;

    /**
     * Поле идентификатор арендуемой вещи
     */
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

