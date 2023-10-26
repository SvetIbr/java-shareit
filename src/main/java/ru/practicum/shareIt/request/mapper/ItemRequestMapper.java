package ru.practicum.shareIt.request.mapper;

import ru.practicum.shareIt.item.dto.ItemForRequestDto;
import ru.practicum.shareIt.item.model.Item;
import ru.practicum.shareIt.request.dto.ItemRequestDto;
import ru.practicum.shareIt.request.model.ItemRequest;
import ru.practicum.shareIt.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper-класс для преобразования объектов сервиса запросов на вещи
 *
 * @author Светлана Ибраева
 * @version 1.0
 */
public class ItemRequestMapper {
    /**
     * Метод преобразования объекта ItemRequestDto в ItemRequest
     *
     * @param itemRequestDto {@link ItemRequestDto}
     * @param user           - создатель запроса {@link User}
     * @return ItemRequest {@link ItemRequest}
     */
    public static ItemRequest toItemRequest(ItemRequestDto itemRequestDto, User user) {
        return ItemRequest.builder()
                .id(itemRequestDto.getId())
                .description(itemRequestDto.getDescription())
                .requestor(user)
                .created(itemRequestDto.getCreated())
                .build();
    }

    /**
     * Метод преобразования объекта itemRequest в ItemRequestDto
     *
     * @param itemRequest {@link ItemRequest}
     * @param items       - вещи, созданные в ответ на запрос {@link Item}
     * @return ItemRequestDto {@link ItemRequestDto}
     */
    public static ItemRequestDto toItemRequestDto(ItemRequest itemRequest, List<Item> items) {
        List<ItemForRequestDto> itemsForRequest = null;
        if (items != null) {
            itemsForRequest = items
                    .stream()
                    .map(item -> ItemForRequestDto.builder()
                            .id(item.getId())
                            .name(item.getName())
                            .description(item.getDescription())
                            .available(item.getAvailable())
                            .requestId(item.getRequest().getId())
                            .build())
                    .collect(Collectors.toList());
        }
        return ItemRequestDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .items(itemsForRequest)
                .build();
    }
}


