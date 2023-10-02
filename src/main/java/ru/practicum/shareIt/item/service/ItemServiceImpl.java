package ru.practicum.shareIt.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareIt.booking.dto.LastBookingDto;
import ru.practicum.shareIt.booking.dto.NextBookingDto;
import ru.practicum.shareIt.booking.model.Booking;
import ru.practicum.shareIt.booking.repository.BookingRepository;
import ru.practicum.shareIt.booking.status.BookingStatus;
import ru.practicum.shareIt.error.exception.BadRequestException;
import ru.practicum.shareIt.error.exception.ItemNotFoundException;
import ru.practicum.shareIt.error.exception.NoAccessException;
import ru.practicum.shareIt.error.exception.UserNotFoundException;
import ru.practicum.shareIt.item.comment.dto.CommentDto;
import ru.practicum.shareIt.item.comment.service.CommentService;
import ru.practicum.shareIt.item.dto.ItemDto;
import ru.practicum.shareIt.item.mapper.ItemMapper;
import ru.practicum.shareIt.item.model.Item;
import ru.practicum.shareIt.item.repository.ItemRepository;
import ru.practicum.shareIt.user.model.User;
import ru.practicum.shareIt.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository repository;

    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentService commentService;
    static final Sort SORT_ASC = Sort.by(Sort.Direction.ASC, "end");
    static final Sort SORT_DESC = Sort.by(Sort.Direction.DESC, "end");

    @Transactional
    public ItemDto create(Long userId, ItemDto itemDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(String.format("Пользователь " +
                        "с идентификатором %d не найден", userId)));
        Item item = repository.save(ItemMapper.toItem(itemDto, user));
        return ItemMapper.toItemDto(item);

    }

    @Transactional
    public ItemDto update(Long userId, Long itemId, ItemDto itemDto) {
        checkIdUserAndIdItemForNull(userId, itemId);

        checkUserInUserStorage(userId);

        Item itemToUpdate = repository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException(String.format("Вещь " +
                        "с идентификатором %d не найдена", itemId)));

        if (!userId.equals(itemToUpdate.getOwner().getId())) {
            throw new NoAccessException("У пользователя нет прав для редактирования данной вещи");
        }
        if (itemDto.getName() != null) itemToUpdate.setName(itemDto.getName());
        if (itemDto.getDescription() != null) itemToUpdate.setDescription(itemDto.getDescription());
        if (itemDto.getAvailable() != null) itemToUpdate.setAvailable(itemDto.getAvailable());

        repository.save(itemToUpdate);
        return ItemMapper.toItemDto(itemToUpdate);
    }

    @Transactional
    public ItemDto getById(Long userId, Long itemId) {
        checkIdUserAndIdItemForNull(userId, itemId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("user not found"));

        Item item = repository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException(String.format("Вещь " +
                        "с идентификатором %d не найдена", itemId)));

        User owner = item.getOwner();
        ItemDto itemDto = ItemMapper.toItemDto(item);
        itemDto.setOwner(owner.getId());
        List<CommentDto> commentsDtos = commentService.getCommentsByItem(itemId);
        itemDto.setComments(commentsDtos);
        if (!userId.equals(owner.getId())) {
            return itemDto;
        }

        Optional<Booking> lastBooking = bookingRepository.findTop1BookingByItemIdAndStartIsBeforeAndStatusIs(
                itemId, LocalDateTime.now(), BookingStatus.APPROVED, SORT_DESC);

        itemDto.setLastBooking(lastBooking.map(booking -> LastBookingDto.builder()
                .id(booking.getId())
                .bookerId(booking.getBooker().getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .build()).orElse(null));


        Optional<Booking> nextBooking = bookingRepository.findTop1BookingByItemIdAndStartIsAfterAndStatusIs(
                itemId, LocalDateTime.now(), BookingStatus.APPROVED, SORT_ASC);

        itemDto.setNextBooking(nextBooking.map(booking -> NextBookingDto.builder()
                .id(booking.getId())
                .bookerId(booking.getBooker().getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .build()).orElse(null));
        return itemDto;
    }

    @Transactional(readOnly = true)
    public List<ItemDto> getByOwner(Long userId) {
        if (userId == null) {
            throw new BadRequestException("Не указан идентификатор владельца");
        }

        checkUserInUserStorage(userId);

        List<Item> items = repository.findAllByOwnerId(userId);
        if (items.isEmpty()) {
            return new ArrayList<>();
        }

        List<ItemDto> itemsDto = items.stream()
                .map(ItemMapper::toItemDto)
                .peek(itemDto -> itemDto.setOwner(userId))
                .collect(Collectors.toList());

        for (ItemDto itemDto : itemsDto) {
            List<CommentDto> commentsDto = commentService.getCommentsByItem(itemDto.getId());
            itemDto.setComments(commentsDto);

            Optional<Booking> lastBooking = bookingRepository.findTop1BookingByItemIdAndEndIsBeforeAndStatusIs(
                    itemDto.getId(), LocalDateTime.now(), BookingStatus.APPROVED, SORT_DESC);

            itemDto.setLastBooking(lastBooking.isEmpty() ? LastBookingDto.builder().build() : LastBookingDto.builder()
                    .id(lastBooking.get().getId())
                    .bookerId(lastBooking.get().getBooker().getId())
                    .start(lastBooking.get().getStart())
                    .end(lastBooking.get().getEnd())
                    .build());

            Optional<Booking> nextBooking = bookingRepository.findTop1BookingByItemIdAndEndIsAfterAndStatusIs(
                    itemDto.getId(), LocalDateTime.now(), BookingStatus.APPROVED, SORT_ASC);

            itemDto.setNextBooking(nextBooking.isEmpty() ? NextBookingDto.builder().build() : NextBookingDto.builder()
                    .id(nextBooking.get().getId())
                    .bookerId(nextBooking.get().getBooker().getId())
                    .start(nextBooking.get().getStart())
                    .end(nextBooking.get().getEnd())
                    .build());
        }
        itemsDto.sort(Comparator.comparing(o -> o.getLastBooking().getStart(),
                Comparator.nullsLast(Comparator.reverseOrder())));

        for (ItemDto itemDto : itemsDto) {
            if (itemDto.getLastBooking().getBookerId() == null) {
                itemDto.setLastBooking(null);
            }
            if (itemDto.getNextBooking().getBookerId() == null) {
                itemDto.setNextBooking(null);
            }
        }
        return itemsDto;
    }

    @Transactional(readOnly = true)
    public List<ItemDto> search(Long userId, String text) {
        checkUserInUserStorage(userId);
        if (text == null || text.isBlank() || text.isEmpty()) return new ArrayList<>();
        return repository.searchAvailableByText(text).stream()
                .map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    private void checkUserInUserStorage(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(String.format("Пользователь " +
                    "с идентификатором %d не найден", userId));
        }
    }

    private void checkIdUserAndIdItemForNull(Long userId, Long itemId) {
        if (itemId == null) {
            if (userId == null) {
                throw new BadRequestException("Не указаны идентификаторы " +
                        "пользователя и вещи для обновления информации");
            } else {
                throw new BadRequestException("Не указан идентификатор вещи для обновления информации");
            }
        } else {
            if (userId == null) {
                throw new BadRequestException("Не указан идентификатор пользователя");
            }
        }
    }
}
