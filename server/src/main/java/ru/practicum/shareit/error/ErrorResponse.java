package ru.practicum.shareit.error;

import lombok.Getter;

/**
 * Класс ответов контроллеров при возникновении исключений с полем <b>error</b>
 *
 * @author Светлана Ибраева
 * @version 1.0
 */
@Getter
public class ErrorResponse {
    /**
     * Поле ошибка
     * -- GETTER --
     * Метод получения значения поля error
     */
    private final String error;

    /**
     * Конструктор - создание нового объекта - ответа контроллера
     */
    public ErrorResponse(String error) {
        this.error = error;
    }
}
