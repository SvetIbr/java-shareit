package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

import static ru.practicum.shareit.utils.Constants.HEADER_WITH_USER_ID;

/**
 * Класс контроллера для работы с запросами к сервису запроса вещей
 *
 * @author Светлана Ибраева
 * @version 1.0
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {
    /**
     * Поле сервис для работы с хранилищем запросов вещей
     */
    private final ItemRequestService service;

    /**
     * Метод добавления запроса на вещь в хранилище сервиса через запрос
     *
     * @param userId         - идентификатор пользователя, создающего запрос
     * @param itemRequestDto {@link ItemRequestDto}
     * @return {@link ItemRequestDto} с добавленным id и код ответа API 201
     */
    @PostMapping
    public ItemRequestDto create(@RequestHeader(HEADER_WITH_USER_ID) long userId,
                                 @RequestBody ItemRequestDto itemRequestDto) {
        return new ResponseEntity<>(service.create(userId, itemRequestDto), HttpStatus.CREATED).getBody();
    }

    /**
     * Метод получения списка своих запросов вместе с данными
     * об ответах на них из хранилища сервиса через запрос
     *
     * @param userId - идентификатор пользователя, запрашивающего информацию
     * @return список объектов {@link ItemRequestDto}
     */
    @GetMapping
    public List<ItemRequestDto> getAllItemRequestByUser(@RequestHeader(HEADER_WITH_USER_ID) long userId) {
        return service.getAllItemRequestByUser(userId);
    }

    /**
     * Метод получения списка запросов на вещи, созданных другими пользователями
     * из хранилища сервиса через запрос. Запросы на вещи сортируются от более новых к более старым.
     * Результаты возвращаются постранично.
     *
     * @param userId - идентификатор пользователя, запрашивающего информацию
     * @param from   - индекс первого элемента, начиная с 0
     * @param size   - количество элементов для отображения
     * @return список объектов {@link ItemRequestDto}
     */
    @GetMapping("/all")
    public List<ItemRequestDto> getAllRequestsForUser(@RequestHeader(HEADER_WITH_USER_ID) long userId,
                                                      @RequestParam(required = false,
                                                              defaultValue = "0") Integer from,
                                                      @RequestParam(required = false,
                                                              defaultValue = "10") Integer size) {
        return service.getAllItemRequests(userId, from, size);
    }

    /**
     * Метод получения данных об одном конкретном запросе вместе с данными об ответах на него
     * из хранилища сервиса через запрос.
     * Посмотреть данные об отдельном запросе может любой пользователь.
     *
     * @param userId    - идентификатор пользователя, запрашивающего информацию
     * @param requestId - индекс первого элемента, начиная с 0
     * @return {@link ItemRequestDto}
     */
    @GetMapping("/{requestId}")
    public ItemRequestDto getItemRequestById(@RequestHeader(HEADER_WITH_USER_ID) long userId,
                                             @PathVariable long requestId) {
        return service.getItemRequestById(userId, requestId);
    }
}
