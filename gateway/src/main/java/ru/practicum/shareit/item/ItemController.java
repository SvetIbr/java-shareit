package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import static ru.practicum.shareit.utils.Constants.HEADER_FOR_REQUEST;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Validated
class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> createItem(@RequestHeader(HEADER_FOR_REQUEST) Long userId,
                                             @Valid @RequestBody ItemDto itemDto) {
        log.info("Create Item={}", itemDto);
        return itemClient.createItem(itemDto, userId);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateItem(@RequestHeader(HEADER_FOR_REQUEST) Long userId,
                                             @RequestBody ItemDto itemDto, @PathVariable Long id) {
        log.info("Update Item with ID = {}", id);
        return itemClient.updateItem(itemDto, id, userId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getItemById(@RequestHeader(HEADER_FOR_REQUEST) Long userId,
                                              @PathVariable Long id) {
        log.info("Get Item with ID = {}", id);
        return itemClient.getItemById(id, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getItemsByOwner(@RequestHeader(HEADER_FOR_REQUEST) Long userId,
                                                  @PositiveOrZero @RequestParam(name = "from",
                                                          defaultValue = "0") Integer from,
                                                  @Positive @RequestParam(name = "size",
                                                          defaultValue = "10") Integer size) {
        log.info("Get all items for user with ID = {}", userId);
        return itemClient.getItemsByOwner(userId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItemByText(@RequestParam(name = "text", defaultValue = " ")
                                                   String text,
                                                   @RequestHeader(HEADER_FOR_REQUEST) Long userId,
                                                   @PositiveOrZero @RequestParam(name = "from",
                                                           defaultValue = "0") Integer from,
                                                   @Positive @RequestParam(name = "size",
                                                           defaultValue = "10") Integer size) {

        log.info("Search item by keyword={}", text);
        return itemClient.searchItemByText(text, userId, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader(HEADER_FOR_REQUEST) Long userId,
                                             @PathVariable Long itemId,
                                             @Valid @RequestBody CommentDto commentDto) {
        log.info("Add comment for Item's ID = {} by User with ID = {}", itemId, userId);
        return itemClient.addComment(itemId, userId, commentDto);
    }
}
