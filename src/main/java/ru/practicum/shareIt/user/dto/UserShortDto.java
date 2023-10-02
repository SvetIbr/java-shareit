package ru.practicum.shareIt.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserShortDto {
    private final Long id;
}
