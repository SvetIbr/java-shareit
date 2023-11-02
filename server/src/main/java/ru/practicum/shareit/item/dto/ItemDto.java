package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.item.comment.dto.CommentDto;

import java.util.List;

/**
 * Класс вещи со свойствами <b>id</b>, <b>name</b>, <b>description</b>,
 * <b>available</b>, <b>owner</b>, <b>request</b>, <b>lastBooking</b>, <b>nextBooking</b>
 * и <b>comments</b> для работы через REST-интерфейс
 *
 * @author Светлана Ибраева
 * @version 1.0
 */
@Data
@Builder
public class ItemDto {
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
     * Поле идентификатор владельца
     */
    private Long owner;

    /**
     * Поле идентификатор запроса на вещь
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long requestId;

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

