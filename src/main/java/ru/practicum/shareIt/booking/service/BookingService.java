package ru.practicum.shareIt.booking.service;

import ru.practicum.shareIt.booking.dto.BookingRequestDto;
import ru.practicum.shareIt.booking.dto.BookingResponseDto;

import java.util.List;

public interface BookingService {
    BookingResponseDto create(long userId, BookingRequestDto bookingDto);

    BookingResponseDto approve(Long userId, Long bookingId, Boolean approved);

    BookingResponseDto getById(Long userId, Long bookingId);

    List<BookingResponseDto> getAllBookingByUser(Long userId, String state);

    List<BookingResponseDto> getAllBookingByOwner(Long userId, String state);
}
