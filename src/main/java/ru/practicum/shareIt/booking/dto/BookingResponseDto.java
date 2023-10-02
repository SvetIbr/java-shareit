package ru.practicum.shareIt.booking.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareIt.booking.status.BookingStatus;
import ru.practicum.shareIt.item.dto.ItemShortDto;
import ru.practicum.shareIt.user.dto.UserShortDto;

import java.time.LocalDateTime;

@Data
@Builder
public class BookingResponseDto {
    private Long id;

    private LocalDateTime start;

    private LocalDateTime end;

    private ItemShortDto item;

    private UserShortDto booker;

    private BookingStatus status;
}
