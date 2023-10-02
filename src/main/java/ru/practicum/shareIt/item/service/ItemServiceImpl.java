package ru.practicum.shareIt.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareIt.booking.dto.BookingRequestDto;
import ru.practicum.shareIt.booking.mapper.BookingMapper;
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
import ru.practicum.shareIt.item.dto.ItemOwnerDto;
import ru.practicum.shareIt.item.mapper.ItemMapper;
import ru.practicum.shareIt.item.model.Item;
import ru.practicum.shareIt.item.repository.ItemRepository;
import ru.practicum.shareIt.user.model.User;
import ru.practicum.shareIt.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
        checkUserInUserStorage(userId);

        Item item = repository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException(String.format("Вещь " +
                        "с идентификатором %d не найдена", itemId)));

        User owner = item.getOwner();
        ItemDto itemDto = ItemMapper.toItemDto(item);
        itemDto.setOwner(owner.getId());
        List<CommentDto> commentsDto = commentService.getCommentsByItem(itemId);
        itemDto.setComments(commentsDto);
        if (!userId.equals(owner.getId())) {
            return itemDto;
        }

        Optional<Booking> lastBooking = bookingRepository.findTop1BookingByItemIdAndStartIsBeforeAndStatusIs(
                itemDto.getId(), LocalDateTime.now(), BookingStatus.APPROVED, SORT_DESC);
        lastBooking.ifPresent(booking -> itemDto.setLastBooking(BookingMapper.toBookingShortDto(booking)));


        Optional<Booking> nextBooking = bookingRepository.findTop1BookingByItemIdAndStartIsAfterAndStatusIs(
                itemDto.getId(), LocalDateTime.now(), BookingStatus.APPROVED, SORT_ASC);
        nextBooking .ifPresent(booking -> itemDto.setNextBooking(BookingMapper.toBookingShortDto(booking)));
        return itemDto;
    }

    @Transactional(readOnly = true)
    public List<ItemOwnerDto> getByOwner(Long userId) {
        if (userId == null) {
            throw new BadRequestException("Не указан идентификатор владельца");
        }

        checkUserInUserStorage(userId);

        List<Item> items = repository.findAllByOwnerId(userId);
        if (items.isEmpty()) {
            return new ArrayList<>();
        }

        List<ItemOwnerDto> itemsOwnerDto = items.stream()
                .map(ItemMapper::toItemOwnerDto)
                .collect(Collectors.toList());

        for (ItemOwnerDto itemOwnerDto : itemsOwnerDto) {
            List<CommentDto> commentsDto = commentService.getCommentsByItem(itemOwnerDto.getId());
            itemOwnerDto.setComments(commentsDto);

            Optional<Booking> lastBooking = bookingRepository.findTop1BookingByItemIdAndStartIsBeforeAndStatusIs(
                    itemOwnerDto.getId(), LocalDateTime.now(), BookingStatus.APPROVED, SORT_DESC);
            lastBooking.ifPresent(booking -> itemOwnerDto.setLastBooking(BookingMapper.toBookingShortDto(booking)));


            Optional<Booking> nextBooking = bookingRepository.findTop1BookingByItemIdAndStartIsAfterAndStatusIs(
                    itemOwnerDto.getId(), LocalDateTime.now(), BookingStatus.APPROVED, SORT_ASC);
            nextBooking .ifPresent(booking -> itemOwnerDto.setNextBooking(BookingMapper.toBookingShortDto(booking)));
        }
        return itemsOwnerDto;
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
