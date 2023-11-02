package ru.practicum.shareit.error.exception;

public class ItemRequestNotFoundException extends RuntimeException {
    public ItemRequestNotFoundException(String s) {
        super(s);
    }
}
