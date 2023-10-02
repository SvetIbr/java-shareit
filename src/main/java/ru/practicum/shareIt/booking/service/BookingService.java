package ru.practicum.shareIt.booking.service;

import ru.practicum.shareIt.booking.dto.BookingDto;
import ru.practicum.shareIt.booking.dto.BookingInfoDto;

import java.util.List;

public interface BookingService {
    BookingInfoDto create(long userId, BookingDto bookingDto);

    BookingInfoDto approve(Long userId, Long bookingId, Boolean approved);

    BookingInfoDto getById(Long userId, Long bookingId);

    List<BookingInfoDto> getAllBookingByUser(Long userId, String state);

    List<BookingInfoDto> getAllBookingByOwner(Long userId, String state);
}
