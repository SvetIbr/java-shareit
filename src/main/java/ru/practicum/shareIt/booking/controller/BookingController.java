package ru.practicum.shareIt.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareIt.booking.dto.BookingRequestDto;
import ru.practicum.shareIt.booking.dto.BookingResponseDto;
import ru.practicum.shareIt.booking.service.BookingService;

import javax.validation.Valid;

import java.util.List;

import static ru.practicum.shareIt.item.controller.ItemController.HEADER_WITH_OWNER_ID;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService service;
    @PostMapping
    public BookingResponseDto create(@RequestHeader(HEADER_WITH_OWNER_ID) long userId,
                                     @Valid @RequestBody BookingRequestDto bookingDto) {
        return new ResponseEntity<>(service.create(userId, bookingDto), HttpStatus.CREATED).getBody();
    }

    @PatchMapping("/{bookingId}")
    public BookingResponseDto approve(@RequestHeader(HEADER_WITH_OWNER_ID) Long userId,
                                      @PathVariable Long bookingId,
                                      @RequestParam Boolean approved) {
        return service.approve(userId, bookingId, approved);
    }

    //Получение данных о конкретном бронировании (включая его статус).
    // Может быть выполнено либо автором бронирования, либо владельцем вещи,
    // к которой относится бронирование.
    @GetMapping("/{bookingId}")
    public BookingResponseDto getById(@RequestHeader(HEADER_WITH_OWNER_ID) Long userId,
                                      @PathVariable Long bookingId) {
        return service.getById(userId, bookingId);
    }

    //Получение списка всех бронирований текущего пользователя.
    @GetMapping
    public List<BookingResponseDto> get(@RequestHeader(HEADER_WITH_OWNER_ID) Long userId,
                                        @RequestParam(defaultValue = "ALL", required = false) String state) {
        return service.getAllBookingByUser(userId, state);
    }

    //Получение списка бронирований для всех вещей текущего пользователя.
    @GetMapping("/owner")
    public List<BookingResponseDto> getByOwner(@RequestHeader(HEADER_WITH_OWNER_ID) Long userId,
                                               @RequestParam(defaultValue = "ALL", required = false) String state) {
        return service.getAllBookingByOwner(userId, state);
    }
}
