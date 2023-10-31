package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Класс вещи со свойствами <b>id</b>, <b>name</b>, <b>description</b>,
 * <b>available</b> и <b>requestId</b> для работы через REST-интерфейс
 *
 * @author Светлана Ибраева
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemForRequestDto {
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
     * Поле идентификатора запроса на вещь
     */
    private Long requestId;
}
