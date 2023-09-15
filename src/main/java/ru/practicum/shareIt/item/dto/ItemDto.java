package ru.practicum.shareIt.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareIt.request.model.ItemRequest;
import ru.practicum.shareIt.user.model.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Builder
public class ItemDto {

    private Long id;

    @NotNull(message = "Не указано наименование")
    @NotBlank(message = "Наименование не может быть пустым")
    private String name;

    @NotNull(message = "Не указано описание")
    @NotBlank(message = "Описание не может быть пустым")
    private String description;

    @NotNull(message = "Не указан статус доступности")
    private Boolean available;

    private User owner;

    private ItemRequest request;

}

