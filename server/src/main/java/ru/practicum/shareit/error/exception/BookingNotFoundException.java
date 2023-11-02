package ru.practicum.shareit.error.exception;

public class BookingNotFoundException extends RuntimeException {
    public BookingNotFoundException(String s) {
        super(s);
    }
}

