package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import static ru.practicum.shareit.utils.Constants.HEADER_FOR_REQUEST;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Validated
@Slf4j
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> createItemRequest(@RequestHeader(HEADER_FOR_REQUEST) Long userId,
                                                    @RequestBody @Valid ItemRequestDto itemRequestDto) {

        log.info("Create ItemRequest={}", itemRequestDto);
        return itemRequestClient.createItemRequest(userId, itemRequestDto);
    }

    @GetMapping()
    public ResponseEntity<Object> getAllItemRequestByUser(@RequestHeader(HEADER_FOR_REQUEST) Long userId) {
        log.info("Get all own requests for user={}", userId);
        return itemRequestClient.getAllItemRequestsByUser(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllItemRequestsForUser(@RequestHeader(HEADER_FOR_REQUEST) Long userId,
                                                            @PositiveOrZero @RequestParam(required = false,
                                                                    defaultValue = "0") Integer from,
                                                            @Positive @RequestParam(required = false,
                                                                    defaultValue = "10") Integer size) {
        log.info("Get all user's requests for user={}", userId);
        return itemRequestClient.getAllItemRequestForUser(userId, from, size);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getItemRequestById(@RequestHeader(HEADER_FOR_REQUEST) Long userId,
                                                     @PathVariable Long id) {
        log.info("Get request with id={}", id);
        return itemRequestClient.getItemRequestById(userId, id);
    }
}
