package ru.practicum.shareIt.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareIt.booking.dto.BookingRequestDto;
import ru.practicum.shareIt.booking.dto.BookingResponseDto;
import ru.practicum.shareIt.booking.dto.State;
import ru.practicum.shareIt.booking.mapper.BookingMapper;
import ru.practicum.shareIt.booking.model.Booking;
import ru.practicum.shareIt.booking.repository.BookingRepository;
import ru.practicum.shareIt.booking.status.BookingStatus;
import ru.practicum.shareIt.error.exception.*;
import ru.practicum.shareIt.item.model.Item;
import ru.practicum.shareIt.item.repository.ItemRepository;
import ru.practicum.shareIt.user.model.User;
import ru.practicum.shareIt.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository repository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public BookingResponseDto create(long userId, BookingRequestDto bookingDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(String.format("Пользователь " +
                        "с идентификатором %d не найден", userId)));
        Item item = itemRepository.findById(bookingDto.getItemId())
                .orElseThrow(() -> new ItemNotFoundException(String.format("Вещь " +
                        "с идентификатором %d не найдена", bookingDto.getItemId())));
        if (userId == item.getOwner().getId()) {
            throw new UserNotFoundException("Пользователь для заявки на бронирование не найден");
        }
        if (!item.getAvailable()) {
            throw new BadRequestException("Не указан статус доступности вещи");
        }
        if (!bookingDto.getEnd().isAfter(bookingDto.getStart())) {
            throw new InvalidDateTimeException("Дата окончания бронирования " +
                    "не может быть раньше даты начала");
        }
        Booking booking = BookingMapper.toBooking(bookingDto, user, item);
        booking.setStatus(BookingStatus.WAITING);
        booking = repository.save(booking);

        return BookingMapper.toBookingResponseDto(booking);
    }

    @Transactional
    public BookingResponseDto approve(Long ownerId, Long bookingId, Boolean approved) {
        checkUserInUserStorage(ownerId);
        Booking booking = checkAndReturnBookingById(bookingId);

        Item item = booking.getItem();
        if (!ownerId.equals(item.getOwner().getId())) {
            throw new NoAccessException("У пользователя нет прав " +
                    "для редактирования статуса данной вещи");
        }
        if (booking.getStatus().equals(BookingStatus.APPROVED)
                || booking.getStatus().equals(BookingStatus.REJECTED)) {
            throw new InvalidStatusException(String.format("Данная вещь " +
                    "уже имеет статус %s", booking.getStatus()));
        }
        if (approved != null) booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        booking = repository.save(booking);
        return BookingMapper.toBookingResponseDto(booking);
    }

    @Transactional
    public BookingResponseDto getById(Long userId, Long bookingId) {
        Booking booking = checkAndReturnBookingById(bookingId);
        Item item = booking.getItem();
        if (!userId.equals(item.getOwner().getId()) && !userId.equals(booking.getBooker().getId())) {
            throw new NoAccessException("У пользователя нет прав " +
                    "для просмотра данной заявки на редактирование");
        }
        return BookingMapper.toBookingResponseDto(booking);
    }

    @Transactional(readOnly = true)
    public List<BookingResponseDto> getAllBookingByUser(Long userId, String state) {
        State actualState = State.validateState(state);
        checkUserInUserStorage(userId);

        List<Booking> bookings = new ArrayList<>();

        switch (actualState) {
            case ALL:
                bookings = repository.findAllByBookerIdOrderByStartDesc(userId);
                break;
            case PAST:
                bookings = repository
                        .findAllByBookerIdAndEndIsBeforeOrderByStartDesc(userId,
                                LocalDateTime.now());
                break;
            case FUTURE:
                bookings = repository
                        .findAllByBookerIdAndStartIsAfterOrderByStartDesc(userId,
                                LocalDateTime.now());
                break;
            case CURRENT:
                bookings = repository
                        .findAllByBookerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(
                                userId, LocalDateTime.now(), LocalDateTime.now());
                break;
            case WAITING:
                bookings = repository
                        .findAllByBookerIdAndStatusOrderByStartDesc(userId,
                                BookingStatus.WAITING);
                break;
            case REJECTED:
                bookings = repository
                        .findAllByBookerIdAndStatusOrderByStartDesc(userId,
                                BookingStatus.REJECTED);
                break;
        }

        return bookings.isEmpty() ? Collections.emptyList() : bookings.stream()
                .map(BookingMapper::toBookingResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<BookingResponseDto> getAllBookingByOwner(Long userId, String state) {
        State actualState = State.validateState(state);
        checkUserInUserStorage(userId);

        List<Booking> bookings = new ArrayList<>();

        switch (actualState) {
            case ALL:
                bookings = repository.findAllByItemOwnerIdOrderByStartDesc(userId);
                break;
            case PAST:
                bookings = repository
                        .findAllByItemOwnerIdAndEndIsBeforeOrderByStartDesc(userId,
                                LocalDateTime.now());
                break;
            case FUTURE:
                bookings = repository
                        .findAllByItemOwnerIdAndStartIsAfterOrderByStartDesc(userId,
                                LocalDateTime.now());
                break;
            case CURRENT:
                bookings = repository
                        .findAllByItemOwnerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(userId,
                                LocalDateTime.now(), LocalDateTime.now());
                break;
            case WAITING:
                bookings = repository
                        .findAllByItemOwnerIdAndStatusOrderByStartDesc(userId,
                                BookingStatus.WAITING);
                break;
            case REJECTED:
                bookings = repository
                        .findAllByItemOwnerIdAndStatusOrderByStartDesc(userId,
                                BookingStatus.REJECTED);
                break;
        }

        return bookings.isEmpty() ? Collections.emptyList() : bookings.stream()
                .map(BookingMapper::toBookingResponseDto)
                .collect(Collectors.toList());
    }

    private void checkUserInUserStorage(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(String.format("Пользователь " +
                    "с идентификатором %d не найден", userId));
        }
    }

    private Booking checkAndReturnBookingById(Long bookingId) {
        return repository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException(String.format("Заявка " +
                        "на бронирование с идентификатором %d не найдена", bookingId)));
    }
}



