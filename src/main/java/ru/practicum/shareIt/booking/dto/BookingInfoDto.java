package ru.practicum.shareIt.booking.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareIt.booking.status.BookingStatus;
import ru.practicum.shareIt.item.dto.ItemInfoDto;
import ru.practicum.shareIt.user.dto.UserInfoDto;

import java.time.LocalDateTime;

@Data
@Builder
public class BookingInfoDto {
    private Long id;

    private LocalDateTime start;

    private LocalDateTime end;

    private ItemInfoDto item;

    private UserInfoDto booker;

    private BookingStatus status;
}
