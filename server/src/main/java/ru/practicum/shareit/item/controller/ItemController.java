package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.service.CommentService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemOwnerDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

import static ru.practicum.shareit.utils.Constants.HEADER_WITH_USER_ID;

/**
 * Класс контроллера для работы с запросами к сервису вещей
 *
 * @author Светлана Ибраева
 * @version 1.0
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    /**
     * Поле сервис для работы с хранилищем вещей
     */
    private final ItemService service;

    /**
     * Поле сервис для работы с комментариями
     */
    private final CommentService commentService;

    /**
     * Метод добавления вещи в хранилище сервиса через запрос
     *
     * @param userId  - идентификатор владельца
     * @param itemDto {@link ItemDto}
     * @return копию объекта itemDto с добавленным id и код ответа API 201
     */
    @PostMapping
    public ItemDto create(@RequestHeader(HEADER_WITH_USER_ID) long userId,
                          @RequestBody ItemDto itemDto) {
        return new ResponseEntity<>(service.create(userId, itemDto), HttpStatus.CREATED).getBody();
    }

    /**
     * Метод обновления информации о вещи в хранилище сервиса через запрос
     *
     * @param userId  - идентификатор пользователя, обновляющего вещь
     * @param itemDto {@link ItemDto} - данные для обновления
     * @param itemId  - идентификатор вещи, которую обновляют
     * @return копию объекта itemDto с обновленными полями
     */
    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader(HEADER_WITH_USER_ID) long userId,
                          @RequestBody ItemDto itemDto,
                          @PathVariable Long itemId) {
        return service.update(userId, itemId, itemDto);
    }

    /**
     * Метод получения информации о вещи из хранилища сервиса по идентификатору через запрос
     *
     * @param userId - идентификатор пользователя, который хочет посмотреть информацию
     * @param itemId - идентификатор вещи, которую хотят посмотреть
     * @return копию объекта itemDto с указанным идентификатором
     */
    @GetMapping("/{itemId}")
    public ItemDto findById(@RequestHeader(HEADER_WITH_USER_ID) long userId,
                            @PathVariable Long itemId) {
        return service.getById(userId, itemId);
    }

    /**
     * Метод получения владельцем списка всех его вещей из хранилища сервиса через запрос
     *
     * @param userId - идентификатор пользователя, который является владельцем вещей
     * @param from   - индекс первого элемента, начиная с 0
     * @param size   - количество элементов для отображения
     * @return список объектов ItemOwnerDto
     */
    @GetMapping
    public List<ItemOwnerDto> findByOwner(@RequestHeader(HEADER_WITH_USER_ID) long userId,
                                          @RequestParam(required = false,
                                                  defaultValue = "0") Integer from,
                                          @RequestParam(required = false,
                                                  defaultValue = "10") Integer size) {
        return service.getByOwner(userId, from, size);
    }

    /**
     * Метод получения через запрос списка вещей из хранилища сервиса,
     * в названии или описании которых содержится text
     *
     * @param userId - идентификатор текущего пользователя
     * @param text   - текст
     * @param from   - индекс первого элемента, начиная с 0
     * @param size   - количество элементов для отображения
     * @return список вещей, доступных для аренды и содержащх в описании или названии text
     */
    @GetMapping("/search")
    public List<ItemDto> search(@RequestHeader(HEADER_WITH_USER_ID) long userId,
                                @RequestParam(value = "text") String text,
                                @RequestParam(required = false, defaultValue = "0") Integer from,
                                @RequestParam(required = false, defaultValue = "10") Integer size) {
        return service.search(userId, text, from, size);
    }

    /**
     * Метод добавления комментария к вещи через запрос
     *
     * @param userId     - идентификатор пользователя, добавлющего комментарий
     * @param itemId     - идентификатор вещи, которой добавляют комментарий
     * @param commentDto - {@link CommentDto}
     * @return CommentDto {@link CommentDto}
     */
    @PostMapping("/{itemId}/comment")
    public CommentDto comment(@RequestHeader(HEADER_WITH_USER_ID) Long userId,
                              @PathVariable Long itemId,
                              @RequestBody CommentDto commentDto) {
        return commentService.addComment(userId, itemId, commentDto);
    }
}

