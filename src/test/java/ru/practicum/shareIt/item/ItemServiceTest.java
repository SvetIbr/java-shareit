package ru.practicum.shareIt.item;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.TransactionSystemException;
import ru.practicum.shareIt.booking.dto.BookingRequestDto;
import ru.practicum.shareIt.booking.dto.BookingResponseDto;
import ru.practicum.shareIt.booking.repository.BookingRepository;
import ru.practicum.shareIt.booking.service.BookingService;
import ru.practicum.shareIt.error.exception.BadRequestException;
import ru.practicum.shareIt.error.exception.ItemNotFoundException;
import ru.practicum.shareIt.error.exception.NoAccessException;
import ru.practicum.shareIt.error.exception.UserNotFoundException;
import ru.practicum.shareIt.item.comment.dto.CommentDto;
import ru.practicum.shareIt.item.comment.repository.CommentRepository;
import ru.practicum.shareIt.item.comment.service.CommentService;
import ru.practicum.shareIt.item.dto.ItemDto;
import ru.practicum.shareIt.item.dto.ItemOwnerDto;
import ru.practicum.shareIt.item.mapper.ItemMapper;
import ru.practicum.shareIt.item.model.Item;
import ru.practicum.shareIt.item.repository.ItemRepository;
import ru.practicum.shareIt.item.service.ItemService;
import ru.practicum.shareIt.user.model.User;
import ru.practicum.shareIt.user.repository.UserRepository;
import ru.practicum.shareIt.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ItemServiceTest {
    @Autowired
    private final CommentRepository commentRepository;
    @Autowired
    private final CommentService commentService;
    @Autowired
    private final ItemRepository itemRepository;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final BookingRepository bookingRepository;
    @Autowired
    private final BookingService bookingService;
    @Autowired
    private final UserService userService = Mockito.mock(UserService.class);
    @Autowired
    private final ItemService itemService;
    private final User user1 = new User(1L, "user1", "user1@mail.ru");
    private final User user2 = new User(2L, "user2", "user2@mail.ru");
    private final Item item = new Item(1L, "Дрель", "Простая дрель",
            true, user1, null);
    private final CommentDto commentDto = CommentDto.builder()
            .id(1L)
            .authorName(user2.getName())
            .text("comment")
            .created(LocalDateTime.now()).build();
    private final BookingRequestDto bookingRequestDto = BookingRequestDto.builder()
            .start(LocalDateTime.now().plusSeconds(2))
            .end(LocalDateTime.now().plusSeconds(4))
            .itemId(item.getId())
            .build();

    @Autowired
    public ItemServiceTest(CommentRepository commentRepository, CommentService commentService, UserRepository userRepository, ItemRepository itemRepository,
                           BookingService bookingService, BookingRepository bookingRepository, ItemService itemService) {
        this.commentRepository = commentRepository;
        this.commentService = commentService;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
        this.itemService = itemService;
        this.bookingService = bookingService;
        this.bookingRepository = bookingRepository;
        userRepository.save(user1);
        userRepository.save(user2);
    }

    @Test
    void createTest() {
        ItemDto itemDto = itemService.create(item.getOwner().getId(), ItemMapper.toItemDto(item));
        Item itemFromStorage = itemRepository.findById(item.getId()).get();
        assertEquals(itemFromStorage.getId(), itemDto.getId());
        assertEquals(itemFromStorage.getName(), itemDto.getName());
        assertEquals(itemFromStorage.getDescription(), itemDto.getDescription());
        assertEquals(itemFromStorage.getAvailable(), itemDto.getAvailable());
    }

    @Test
    void createWithFailUserIdTest() {
        final UserNotFoundException exception = assertThrows(UserNotFoundException.class, ()
                -> itemService.create(23L, ItemMapper.toItemDto(item)));
        assertEquals("Пользователь с идентификатором 23 не найден",
                exception.getMessage());
    }

    @Test
    void createWithEmptyNameTest() {
        Item itemWithoutName = new Item(1L, "", "Простая дрель",
                true, user1, null);
        assertThrows(TransactionSystemException.class, () ->
                itemService.create(itemWithoutName.getOwner().getId(),
                        ItemMapper.toItemDto(itemWithoutName)));
    }

    @Test
    void createWithNullNameTest() {
        Item itemWithoutName = new Item(1L, null, "Простая дрель",
                true, user1, null);
        assertThrows(TransactionSystemException.class, () ->
                itemService.create(itemWithoutName.getOwner().getId(),
                        ItemMapper.toItemDto(itemWithoutName)));
    }

    @Test
    void createWithEmptyDescriptionTest() {
        Item itemWithoutDescription = new Item(1L, "дрель", "",
                true, user1, null);
        assertThrows(TransactionSystemException.class, () ->
                itemService.create(itemWithoutDescription.getOwner().getId(),
                        ItemMapper.toItemDto(itemWithoutDescription)));
    }

    @Test
    void createWithNullDescriptionTest() {
        Item itemWithoutDescription = new Item(1L, "дрель", null,
                true, user1, null);
        assertThrows(TransactionSystemException.class, () ->
                itemService.create(itemWithoutDescription.getOwner().getId(),
                        ItemMapper.toItemDto(itemWithoutDescription)));
    }

    @Test
    void createWithNullAvailableTest() {
        Item itemWithoutAvailable = new Item(1L, "дрель", "Простая дрель",
                null, user1, null);
        assertThrows(TransactionSystemException.class, () ->
                itemService.create(itemWithoutAvailable.getOwner().getId(),
                        ItemMapper.toItemDto(itemWithoutAvailable)));
    }

    @Test
    void updateTest() {
        Item item = new Item(1L, "Дрель", "Простая дрель",
                true, user1, null);
        itemService.create(item.getOwner().getId(), ItemMapper.toItemDto(item));

        Item updatedItem = new Item(1L, "Молоток", "Молоток простой",
                false, user1, null);
        itemService.update(1L, 1L, ItemMapper.toItemDto(updatedItem));

        Item itemFromStorage = itemRepository.findById(item.getId()).get();
        assertEquals(itemFromStorage.getName(), updatedItem.getName());
        assertEquals(itemFromStorage.getDescription(), updatedItem.getDescription());
        assertEquals(itemFromStorage.getAvailable(), updatedItem.getAvailable());
    }

    @Test
    void updateWithNullUserIdTest() {
        final BadRequestException exception = assertThrows(BadRequestException.class, ()
                -> itemService.update(null, 1L, ItemMapper.toItemDto(item)));
        assertEquals("Не указан идентификатор пользователя",
                exception.getMessage());
    }

    @Test
    void updateWithNullItemIdTest() {
        final BadRequestException exception = assertThrows(BadRequestException.class, ()
                -> itemService.update(1L, null, ItemMapper.toItemDto(item)));
        assertEquals("Не указан идентификатор вещи для обновления информации",
                exception.getMessage());
    }

    @Test
    void updateWithNullItemIdAndNullUserIdTest() {
        final BadRequestException exception = assertThrows(BadRequestException.class, ()
                -> itemService.update(null, null, ItemMapper.toItemDto(item)));
        assertEquals("Не указаны идентификаторы пользователя и вещи для обновления информации",
                exception.getMessage());
    }

    @Test
    void updateWithFailItemIdTest() {
        final ItemNotFoundException exception = assertThrows(ItemNotFoundException.class, ()
                -> itemService.update(1L, 13L, ItemMapper.toItemDto(item)));
        assertEquals("Вещь с идентификатором 13 не найдена",
                exception.getMessage());
    }

    @Test
    void updateWithFailUserIdTest() {
        final UserNotFoundException exception = assertThrows(UserNotFoundException.class, ()
                -> itemService.update(12L, 1L, ItemMapper.toItemDto(item)));
        assertEquals("Пользователь с идентификатором 12 не найден",
                exception.getMessage());
    }

    @Test
    void updateNoOwnerTest() {
        final NoAccessException exception = assertThrows(NoAccessException.class, ()
                -> itemService.update(2L, 1L, ItemMapper.toItemDto(item)));
        assertEquals("У пользователя нет прав для редактирования данной вещи",
                exception.getMessage());
    }

    @Test
    void getByIdNoOwnerTest() {
        ItemDto itemDto = itemService.create(item.getOwner().getId(), ItemMapper.toItemDto(item));
        ItemDto itemDtoNoOwner = itemService.getById(user2.getId(), itemDto.getId());
        assertEquals(itemDtoNoOwner.getName(), item.getName());
        assertEquals(itemDtoNoOwner.getDescription(), item.getDescription());
        assertEquals(itemDtoNoOwner.getId(), item.getId());
        assertEquals(itemDtoNoOwner.getAvailable(), item.getAvailable());
        assertNull(itemDtoNoOwner.getLastBooking());
        assertNull(itemDtoNoOwner.getNextBooking());
    }

    @Test
    void getByIdByOwnerTest() {
        ItemDto itemDto = itemService.create(item.getOwner().getId(), ItemMapper.toItemDto(item));
        ItemDto itemDtoOwner = itemService.getById(itemDto.getId(), user1.getId());
        ItemOwnerDto itemOwnerDto = ItemMapper.toItemOwnerDto(item);
        assertEquals(itemDtoOwner.getName(), itemOwnerDto.getName());
        assertEquals(itemDtoOwner.getDescription(), itemOwnerDto.getDescription());
        assertEquals(itemDtoOwner.getId(), itemOwnerDto.getId());
        assertEquals(itemDtoOwner.getAvailable(), itemOwnerDto.getAvailable());
        assertNotNull(itemDtoOwner.getLastBooking());
        assertNotNull(itemDtoOwner.getComments());
    }

    @Test
    void getByIdWithFailIdTest() {
        final ItemNotFoundException exception = assertThrows(ItemNotFoundException.class, ()
                -> itemService.getById(1L, 13L));
        assertEquals("Вещь с идентификатором 13 не найдена",
                exception.getMessage());
    }

    @Test
    void getByOwnerTest() {
        itemService.create(item.getOwner().getId(), ItemMapper.toItemDto(item));
        ItemOwnerDto itemOwnerDto = itemService.getByOwner(item.getOwner().getId(), 0, 10).get(0);
        assertEquals(itemOwnerDto.getName(), item.getName());
        assertEquals(itemOwnerDto.getDescription(), item.getDescription());
        assertEquals(itemOwnerDto.getId(), item.getId());
        assertEquals(itemOwnerDto.getAvailable(), item.getAvailable());
        assertNotNull(itemOwnerDto.getLastBooking());
        assertNotNull(itemOwnerDto.getComments());
    }

    @Test
    void getByOwnerWithFailOwnerIdTest() {
        final BadRequestException exception = assertThrows(BadRequestException.class, ()
                -> itemService.getByOwner(null, 0, 10));
        assertEquals("Не указан идентификатор владельца",
                exception.getMessage());
    }

    @Test
    void getByOwnerWithFailFromTest() {
        final BadRequestException exception = assertThrows(BadRequestException.class, ()
                -> itemService.getByOwner(1L, -1, 10));
        assertEquals("Параметры для отображения данных " +
                        "заданы не верно (начало не может быть меньше 0, а размер - меньше 1)",
                exception.getMessage());
    }

    @Test
    void getByOwnerWithFailSizeTest() {
        final BadRequestException exception = assertThrows(BadRequestException.class, ()
                -> itemService.getByOwner(1L, 0, 0));
        assertEquals("Параметры для отображения данных " +
                        "заданы не верно (начало не может быть меньше 0, а размер - меньше 1)",
                exception.getMessage());
    }

    @Test
    void searchByBlankTextTest() {
        assertEquals(new ArrayList<>(),
                itemService.search(1L, " ", 0, 10));
    }

    @Test
    void searchByNullTextTest() {
        assertEquals(new ArrayList<>(),
                itemService.search(1L, null, 0, 10));
    }

    @Test
    void searchWithFailFromTest() {
        final BadRequestException exception = assertThrows(BadRequestException.class, ()
                -> itemService.search(1L, "дрель", -1, 12));
        assertEquals("Параметры для отображения данных " +
                        "заданы не верно (начало не может быть меньше 0, а размер - меньше 1)",
                exception.getMessage());
    }

    @Test
    void searchTest() {
        itemService.create(item.getOwner().getId(), ItemMapper.toItemDto(item));
        assertEquals(List.of(ItemMapper.toItemDto(item)),
                itemService.search(1L, "дрель", 0, 10));
    }

    @Test
    void createCommentTest() throws InterruptedException {
        itemService.create(item.getOwner().getId(), ItemMapper.toItemDto(item));
        BookingResponseDto bookingResponseDto = bookingService.create(user2.getId(), bookingRequestDto);
        bookingService.approve(user1.getId(), bookingResponseDto.getId(), true);
        Thread.sleep(10000);
        CommentDto comment = commentService.addComment(user2.getId(), item.getId(), commentDto);
        assertEquals(commentRepository.findById(comment.getId()).orElse(null).getText(), comment.getText());
    }
}
