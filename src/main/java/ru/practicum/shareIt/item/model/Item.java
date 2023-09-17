package ru.practicum.shareIt.item.model;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareIt.user.model.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Класс вещи со свойствами <b>id</b>, <b>name</b>, <b>description</b>,
 * <b>available</b>, <b>owner</b> для работы с базой данных
 *
 * @author Светлана Ибраева
 * @version 1.0
 */
@Data
@Builder
public class Item {
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

    /**
     * Поле владелец
     */
    @NotNull(message = "Не указан владелец")
    private final User owner;
}
