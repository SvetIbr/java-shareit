package ru.practicum.shareIt.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareIt.booking.dto.BookingShortDto;
import ru.practicum.shareIt.item.comment.dto.CommentDto;

import java.util.List;

/**
 * Класс вещи со свойствами <b>id</b>, <b>name</b>, <b>description</b>,
 * <b>available</b>, <b>lastBooking</b>, <b>nextBooking</b>
 * и <b>comments</b> для работы через REST-интерфейс
 *
 * @author Светлана Ибраева
 * @version 1.0
 */
@Data
@Builder
public class ItemOwnerDto {
    /**
     * Поле идентификатор
     */
    private Long id;

    /**
     * Поле наименование
     */
    private String name;

    /**
     * Поле описание
     */
    private String description;

    /**
     * Поле статус доступности для аренды
     */
    private Boolean available;

    /**
     * Поле последнее бронирование с краткой информацией о нем
     */
    private BookingShortDto lastBooking;

    /**
     * Поле следующее бронирование с краткой информацией о нем
     */
    private BookingShortDto nextBooking;

    /**
     * Поле список комментариев
     */
    private List<CommentDto> comments;
}
