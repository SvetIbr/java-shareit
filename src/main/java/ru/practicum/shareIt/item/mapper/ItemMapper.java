package ru.practicum.shareIt.item.mapper;

import ru.practicum.shareIt.item.dto.ItemDto;
import ru.practicum.shareIt.item.model.Item;
import ru.practicum.shareIt.request.model.ItemRequest;
import ru.practicum.shareIt.user.model.User;

/**
 * Mapper-класс для преобразования объектов Item в ItemDto и наоборот
 *
 * @author Светлана Ибраева
 * @version 1.0
 */
public class ItemMapper {
    /**
     * Метод преобразования объекта Item в ItemDto
     *
     * @param item {@link Item}
     * @return ItemDto {@link ItemDto} c идентичной информацией полей <b>id</b>,
     * <b>name</b>, <b>description</b>, <b>available</b>
     */
    public static ItemDto toItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .request(item.getRequest() != null ? item.getRequest().getId() : null)
                .lastBooking(null)
                .nextBooking(null)
                .build();
    }

    /**
     * Метод преобразования объекта ItemDto в Item
     *
     * @param itemDto {@link ItemDto}
     * @return Item {@link Item} с идентичной информацией всех полей класса Item
     */
    public static Item toItem(ItemDto itemDto, User user) {
        return Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .request(itemDto.getRequest() == null ? null : ItemRequest.builder()
                        .id(itemDto.getRequest())
                        .build())
                .owner(user)
                .build();
    }

    public static Item matchItem(ItemDto itemDto, Item item) {
        return Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName() == null ? item.getName() : itemDto.getName())
                .description(itemDto.getDescription() == null ? item.getDescription() : itemDto.getDescription())
                .available(itemDto.getAvailable() == null ? item.getAvailable() : itemDto.getAvailable())
                .request(itemDto.getRequest() == null ?
                        item.getRequest() : ItemRequest.builder()
                        .id(itemDto.getRequest())
                        .build())
                .build();
    }
}
