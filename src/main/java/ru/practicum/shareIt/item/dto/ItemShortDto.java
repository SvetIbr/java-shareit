package ru.practicum.shareIt.item.dto;

import lombok.Builder;
import lombok.Data;

/**
 * Класс вещи со свойствами <b>id</b>, <b>name</b>  для работы через REST-интерфейс
 *
 * @author Светлана Ибраева
 * @version 1.0
 */
@Data
@Builder
public class ItemShortDto {
    /**
     * Поле идентификатор
     */
    private final Long id;

    /**
     * Поле наименование
     */
    private final String name;
}
