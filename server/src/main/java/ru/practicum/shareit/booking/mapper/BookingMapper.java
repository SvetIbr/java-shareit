package ru.practicum.shareit.booking.mapper;


import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

/**
 * Mapper-класс для преобразования объектов сервиса бронирования
 *
 * @author Светлана Ибраева
 * @version 1.0
 */
public class BookingMapper {
    /**
     * Метод преобразования объекта BookingRequestDto в Booking
     *
     * @param bookingDto {@link BookingRequestDto}
     * @return {@link Booking}
     */
    public static Booking toBooking(BookingRequestDto bookingDto, User user, Item item) {
        return Booking.builder()
                .id(bookingDto.getId())
                .start(bookingDto.getStart())
                .end(bookingDto.getEnd())
                .item(item)
                .booker(user)
                .status(bookingDto.getStatus())
                .build();
    }

    /**
     * Метод преобразования объекта Booking в BookingResponseDto
     *
     * @param booking {@link Booking}
     * @return {@link BookingResponseDto}
     */
    public static BookingResponseDto toBookingResponseDto(Booking booking) {
        return BookingResponseDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .item(ItemMapper.toItemShortDto(booking.getItem()))
                .booker(UserMapper.toUserShortDto(booking.getBooker()))
                .status(booking.getStatus())
                .build();
    }

    /**
     * Метод преобразования объекта Booking в BookingShortDto
     *
     * @param booking {@link Booking}
     * @return {@link BookingShortDto}
     */
    public static BookingShortDto toBookingShortDto(Booking booking) {
        return BookingShortDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .bookerId(booking.getBooker().getId())
                .build();
    }
}
