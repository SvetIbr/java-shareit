package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private Long id;

    @NotNull(message = "Не указано описание")
    @NotBlank(message = "Не заполнено поле \"описание\"")
    @NotEmpty(message = "Не заполнено поле \"описание\"")
    private String text;

    private String authorName;

    private LocalDateTime created;
}
