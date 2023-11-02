package ru.practicum.shareit.request;

import lombok.*;
import ru.practicum.shareit.item.ItemDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequestDto {

    private Long id;

    @NotNull(message = "Не указано описание")
    @NotBlank(message = "Не заполнено поле \"описание\"")
    private String description;

    private LocalDateTime created;

    private List<ItemDto> items;
}
