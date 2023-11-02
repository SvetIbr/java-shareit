package ru.practicum.shareit.item.mapper;


import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemOwnerDto;
import ru.practicum.shareit.item.dto.ItemShortDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

/**
 * Mapper-класс для преобразования объектов сервиса вещей
 *
 * @author Светлана Ибраева
 * @version 1.0
 */
public class ItemMapper {
    /**
     * Метод преобразования объекта Item в ItemDto
     *
     * @param item {@link Item}
     * @return ItemDto {@link ItemDto}
     */
    public static ItemDto toItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .requestId(item.getRequest() != null ? item.getRequest().getId() : null)
                .owner(item.getOwner().getId())
                .build();
    }

    /**
     * Метод преобразования объекта ItemDto в Item
     *
     * @param itemDto {@link ItemDto}
     * @param user    {@link User}
     * @return Item {@link Item}
     */
    public static Item toItem(ItemDto itemDto, User user) {
        return Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .request(itemDto.getRequestId() == null ? null : ItemRequest.builder()
                        .id(itemDto.getRequestId())
                        .build())
                .owner(user)
                .build();
    }

    /**
     * Метод преобразования объекта Item в ItemOwnerDto
     *
     * @param item {@link Item}
     * @return ItemOwnerDto {@link ItemOwnerDto}
     */
    public static ItemOwnerDto toItemOwnerDto(Item item) {
        return ItemOwnerDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .build();
    }

    /**
     * Метод преобразования объекта Item в ItemShortDto
     *
     * @param item {@link Item}
     * @return ItemShortDto {@link ItemShortDto}
     */
    public static ItemShortDto toItemShortDto(Item item) {
        return ItemShortDto.builder()
                .id(item.getId())
                .name(item.getName())
                .build();
    }
}
