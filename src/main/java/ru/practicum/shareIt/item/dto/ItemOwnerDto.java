package ru.practicum.shareIt.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareIt.booking.dto.BookingShortDto;
import ru.practicum.shareIt.item.comment.dto.CommentDto;

import java.util.List;

@Data
@Builder
public class ItemOwnerDto {
    private Long id;

    private String name;

    private String description;

    private Boolean available;

    private BookingShortDto lastBooking;

    private BookingShortDto nextBooking;

    private List<CommentDto> comments;
}
