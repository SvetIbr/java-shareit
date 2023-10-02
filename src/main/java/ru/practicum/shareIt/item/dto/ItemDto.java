package ru.practicum.shareIt.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareIt.booking.dto.BookingRequestDto;
import ru.practicum.shareIt.booking.dto.BookingShortDto;
import ru.practicum.shareIt.item.comment.dto.CommentDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Класс вещи со свойствами <b>id</b>, <b>name</b>, <b>description</b>,
 * <b>available</b> для работы через REST-интерфейс
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
    @NotNull(message = "Не указано наименование")
    @NotBlank(message = "Не заполнено поле \"наименование\"")
    private String name;

    /**
     * Поле описание
     */
    @NotNull(message = "Не указано описание")
    @NotBlank(message = "Не заполнено поле \"описание\"")
    private String description;

    /**
     * Поле статус доступности для аренды
     */
    @NotNull(message = "Не указан статус доступности")
    private Boolean available;

    private Long owner;

    private Long request;
    private BookingShortDto lastBooking;

    private BookingShortDto nextBooking;

    private List<CommentDto> comments;
}

