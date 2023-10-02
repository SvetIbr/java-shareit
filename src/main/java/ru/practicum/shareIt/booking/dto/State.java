package ru.practicum.shareIt.booking.dto;

import ru.practicum.shareIt.error.exception.InvalidStatusException;

public enum State {
    ALL,
    CURRENT,
    PAST,
    FUTURE,
    WAITING,
    REJECTED;

    public static State validateState(String value) throws InvalidStatusException {
        try {
            return State.valueOf(value);
        } catch (Exception exception) {
            throw new InvalidStatusException("Unknown state: " + value);
        }
    }
}
