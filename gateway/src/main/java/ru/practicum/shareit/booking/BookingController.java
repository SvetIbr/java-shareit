package ru.practicum.shareit.booking;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.InvalidDateTimeException;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.websocket.server.PathParam;

import static ru.practicum.shareit.utils.Constants.HEADER_FOR_REQUEST;


@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {

    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> createBooking(@RequestHeader(HEADER_FOR_REQUEST) Long userId,
                                                @RequestBody @Valid BookItemRequestDto requestDto) {
        if (!requestDto.getEnd().isAfter(requestDto.getStart())) {
            throw new InvalidDateTimeException("Дата окончания бронирования " +
                    "не может быть раньше даты начала");
        }
        log.info("Creating booking {}, userId={}", requestDto, userId);
        return bookingClient.bookItem(requestDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> updateBooking(@RequestHeader(HEADER_FOR_REQUEST) Long userId,
                                                @PathVariable Long bookingId,
                                                @PathParam("approved") @NonNull Boolean approved) {
        log.info("Update Booking with ID = {}", bookingId);
        return bookingClient.updateBooking(bookingId, userId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBookingById(@RequestHeader(HEADER_FOR_REQUEST) Long userId,
                                                 @PathVariable Long bookingId) {
        log.info("Get booking {}, userId={}", bookingId, userId);
        return bookingClient.getBooking(bookingId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllBookingForBooker(@RequestHeader(HEADER_FOR_REQUEST) Long userId,
                                                         @RequestParam(name = "state", defaultValue = "ALL")
                                                         String stateParam,
                                                         @PositiveOrZero @RequestParam(name = "from",
                                                                 defaultValue = "0") Integer from,
                                                         @Positive @RequestParam(name = "size",
                                                                 defaultValue = "10") Integer size) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));

        log.info("Get booking with state {}, userId={}, from={}, size={}", state, userId, from, size);
        return bookingClient.getBookingsForBooker(userId, state, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getAllBookingForOwner(@RequestHeader(HEADER_FOR_REQUEST) Long userId,
                                                        @RequestParam(name = "state", defaultValue = "ALL")
                                                        String stateParam,
                                                        @PositiveOrZero @RequestParam(name = "from",
                                                                defaultValue = "0") Integer from,
                                                        @Positive @RequestParam(name = "size",
                                                                defaultValue = "10") Integer size) {

        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));

        log.info("Get booking with state {}, userId={}, from={}, size={}", state, userId, from, size);
        return bookingClient.getBookingsForOwner(userId, state, from, size);
    }
}
