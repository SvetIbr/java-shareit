package ru.practicum.shareIt.booking.mapper;

import ru.practicum.shareIt.booking.dto.BookingRequestDto;
import ru.practicum.shareIt.booking.dto.BookingResponseDto;
import ru.practicum.shareIt.booking.dto.BookingShortDto;
import ru.practicum.shareIt.booking.model.Booking;
import ru.practicum.shareIt.item.dto.ItemShortDto;
import ru.practicum.shareIt.item.model.Item;
import ru.practicum.shareIt.user.dto.UserShortDto;
import ru.practicum.shareIt.user.model.User;

public class BookingMapper {
    public static Booking toBooking(BookingRequestDto bookingDto, User user) {
        return Booking.builder()
                .id(bookingDto.getId())
                .booker(user)
                .end(bookingDto.getEnd())
                .item(Item.builder()
                        .id(bookingDto.getItemId())
                        .build())
                .start(bookingDto.getStart())
                .status(bookingDto.getStatus())
                .build();
    }

    public static BookingResponseDto toBookingResponseDto(Booking booking) {
        return BookingResponseDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .item(ItemShortDto.builder()
                        .id(booking.getItem().getId())
                        .name(booking.getItem().getName())
                        .build())
                .booker(UserShortDto.builder()
                        .id(booking.getBooker().getId())
                        .build())
                .status(booking.getStatus())
                .build();
    }

    public static BookingShortDto toBookingShortDto(Booking booking) {
        return BookingShortDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                //.itemId(booking.getItem().getId())
                .bookerId(booking.getBooker().getId())
                .build();
    }
}
