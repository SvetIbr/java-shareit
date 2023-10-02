package ru.practicum.shareIt.booking.mapper;

import ru.practicum.shareIt.booking.dto.BookingDto;
import ru.practicum.shareIt.booking.dto.BookingInfoDto;
import ru.practicum.shareIt.booking.model.Booking;
import ru.practicum.shareIt.item.dto.ItemInfoDto;
import ru.practicum.shareIt.item.model.Item;
import ru.practicum.shareIt.user.dto.UserInfoDto;
import ru.practicum.shareIt.user.model.User;

public class BookingMapper {
    public static BookingDto toBookingDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .itemId(booking.getItem().getId())
                .booker(booking.getBooker().getId())
                .status(booking.getStatus())
                .build();
    }

    public static Booking toBooking(BookingDto bookingDto, User user) {
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

    public static BookingInfoDto toBookingInfoDto(Booking booking) {
        return BookingInfoDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .item(ItemInfoDto.builder()
                        .id(booking.getItem().getId())
                        .name(booking.getItem().getName())
                        .build())
                .booker(UserInfoDto.builder()
                        .id(booking.getBooker().getId())
                        .build())
                .status(booking.getStatus())
                .build();
    }
}
