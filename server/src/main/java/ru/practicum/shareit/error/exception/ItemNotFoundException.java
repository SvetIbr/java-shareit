package ru.practicum.shareit.error.exception;

public class ItemNotFoundException extends RuntimeException {
    public ItemNotFoundException(String s) {
        super(s);
    }
}
