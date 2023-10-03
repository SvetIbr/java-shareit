package ru.practicum.shareIt.booking.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Класс бронирования со свойствами <b>id</b>, <b>start</b>, <b>end</b>,
 * <b>bookerId</b> для работы через REST-интерфейс
 *
 * @author Светлана Ибраева
 * @version 1.0
 */
@Builder
@Data
public class BookingShortDto {
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
     * Поле идентификатор арендатора вещи
     */
    private Long bookerId;
}
