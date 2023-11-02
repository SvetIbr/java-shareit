package ru.practicum.shareit.item;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {

    private Long id;

    @NotNull(message = "Не указано наименование")
    @NotBlank(message = "Не заполнено поле \"наименование\"")
    private String name;

    @NotNull(message = "Не указано описание")
    @NotBlank(message = "Не заполнено поле \"описание\"")
    private String description;

    @NotNull(message = "Не указан статус доступности")
    private Boolean available;

    private Long requestId;
}
