package ru.practicum.shareit.error.exception;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String s) {
        super(s);
    }
}
