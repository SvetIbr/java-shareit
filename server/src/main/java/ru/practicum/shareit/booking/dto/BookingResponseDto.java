package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.status.BookingStatus;
import ru.practicum.shareit.item.dto.ItemShortDto;
import ru.practicum.shareit.user.dto.UserShortDto;

import java.time.LocalDateTime;

/**
 * Класс бронирования со свойствами <b>id</b>, <b>start</b>, <b>end</b>,
 * <b>item</b>, <b>booker</b> и <b>status</b> для работы через REST-интерфейс
 *
 * @author Светлана Ибраева
 * @version 1.0
 */
@Data
@Builder
public class BookingResponseDto {
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
     * Поле вещь с краткой информацией о ней
     */
    private ItemShortDto item;

    /**
     * Поле арендатор с краткой информацией о нем
     */
    private UserShortDto booker;

    /**
     * Поле статус бронирования
     */
    private BookingStatus status;
}
