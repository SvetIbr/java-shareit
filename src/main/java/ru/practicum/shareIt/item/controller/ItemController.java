package ru.practicum.shareIt.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareIt.item.dto.ItemDto;
import ru.practicum.shareIt.item.service.ItemServiceImpl;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemServiceImpl service;

    @PostMapping
    public ItemDto create(@RequestHeader("X-Sharer-User-Id") long userId,
                          @Valid @RequestBody ItemDto itemDto) {
        return service.create(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader("X-Sharer-User-Id") long userId,
                          @RequestBody ItemDto itemDto,
                          @PathVariable Long itemId) {
        return service.update(userId, itemId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ItemDto findById(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable Long itemId) {
        return service.getById(userId, itemId);
    }

    @GetMapping
    public List<ItemDto> findByOwner(@RequestHeader("X-Sharer-User-Id") long userId) {
        return service.findByOwner(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam(value = "text") String text) {
        return service.search(text);
    }
}

