package ru.practicum.shareIt.item.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemShortDto {
    private final Long id;
    private final String name;
}