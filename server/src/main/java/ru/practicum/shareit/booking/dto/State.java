package ru.practicum.shareit.booking.dto;


import ru.practicum.shareit.error.exception.InvalidStatusException;

public enum State {
    ALL,
    CURRENT,
    PAST,
    FUTURE,
    WAITING,
    REJECTED;
}

