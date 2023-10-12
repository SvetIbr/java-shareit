package ru.practicum.shareIt.user.dto;

import lombok.Builder;
import lombok.Data;

/**
 * Класс пользователя со свойством <b>id</b> для работы через REST-интерфейс
 *
 * @author Светлана Ибраева
 * @version 1.0
 */
@Data
@Builder
public class UserShortDto {
    /**
     * Поле идентификатор
     */
    private final Long id;
}
