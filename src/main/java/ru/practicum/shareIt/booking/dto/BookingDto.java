package ru.practicum.shareIt.booking.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareIt.booking.status.BookingStatus;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Data
@Builder
public class BookingDto {

    private Long id;

    @NotNull
    @FutureOrPresent(message = "Дата старта не может быть в прошлом")
    private LocalDateTime start;

    @NotNull
    @Future(message = "Дата окончания не может быть в прошлом или настоящем")
    private LocalDateTime end;

    @NotNull
    private Long itemId;

    private Long booker;

    private BookingStatus status;
}

