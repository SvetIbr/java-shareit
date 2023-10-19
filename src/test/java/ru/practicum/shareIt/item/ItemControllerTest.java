package ru.practicum.shareIt.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.practicum.shareIt.error.ErrorResponse;
import ru.practicum.shareIt.error.exception.BadRequestException;
import ru.practicum.shareIt.error.exception.ItemNotFoundException;
import ru.practicum.shareIt.error.exception.UserNotFoundException;
import ru.practicum.shareIt.item.comment.dto.CommentDto;
import ru.practicum.shareIt.item.comment.mapper.CommentMapper;
import ru.practicum.shareIt.item.comment.model.Comment;
import ru.practicum.shareIt.item.comment.service.CommentService;
import ru.practicum.shareIt.item.dto.ItemDto;
import ru.practicum.shareIt.item.mapper.ItemMapper;
import ru.practicum.shareIt.item.model.Item;
import ru.practicum.shareIt.item.service.ItemService;
import ru.practicum.shareIt.user.dto.UserDto;
import ru.practicum.shareIt.user.mapper.UserMapper;
import ru.practicum.shareIt.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
public class ItemControllerTest {
    @MockBean
    private ItemService itemService;
    @MockBean
    private CommentService commentService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    private final User user = new User(1L, "user1", "user1@user1.ru");
    private final UserDto userDto = UserMapper.toUserDto(user);
    private final Item item = new Item(1L, "item1", "description1", true, user, null);
    private final ItemDto itemDto = ItemMapper.toItemDto(item);
    private final Comment comment = new Comment(1L, "comment1", item, user, LocalDateTime.now());
    private final CommentDto commentDto = CommentMapper.toCommentDto(comment);

    @Test
    void createTest() throws Exception {
        when(itemService.create(anyLong(), any()))
                .thenReturn(itemDto);
        mockMvc.perform(post("/items")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(itemDto))
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userDto.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName()), String.class))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription()), String.class))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable()), Boolean.class));

        verify(itemService, times(1))
                .create(anyLong(), any());
    }

    @Test
    void updateTest() throws Exception {
        when(itemService.update(anyLong(), anyLong(), any()))
                .thenReturn(itemDto);
        mockMvc.perform(patch("/items/" + itemDto.getId())
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userDto.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName()), String.class))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription()), String.class))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable()), Boolean.class));

        verify(itemService, times(1))
                .update(anyLong(), anyLong(), any());
    }

    @Test
    void getByIdTest() throws Exception {
        when(itemService.getById(anyLong(), anyLong()))
                .thenReturn(itemDto);
        mockMvc.perform(get("/items/" + itemDto.getId())
                        .header("X-Sharer-User-Id", userDto.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName()), String.class))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription()), String.class))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable()), Boolean.class));

        verify(itemService, times(1))
                .getById(itemDto.getId(), userDto.getId());
    }

    @Test
    void getByIdWithFailItemIdTest() throws Exception {
        when(itemService.getById(anyLong(), anyLong()))
                .thenThrow(new ItemNotFoundException("Вещь с идентификатором 25 не найдена"));
        MvcResult mvcResult = mockMvc.perform(get("/items/25")
                        .header("X-Sharer-User-Id", userDto.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
        String actualResponseBody = mvcResult.getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(mapper
                .writeValueAsString(new ErrorResponse("Вещь с идентификатором 25 не найдена")));
    }

    @Test
    void getByIdWithNullUserIdTest() throws Exception {
        mockMvc.perform(get("/items/" + itemDto.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllTest() throws Exception {
        when(itemService.getByOwner(anyLong(), anyInt(), anyInt()))
                .thenReturn(Collections.emptyList());
        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", userDto.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(itemService, times(1))
                .getByOwner(anyLong(), anyInt(), anyInt());
    }

    @Test
    void searchTest() throws Exception {
        when(itemService.search(anyLong(), anyString(), anyInt(), anyInt()))
                .thenThrow(new BadRequestException("Параметры для отображения данных " +
                        "заданы не верно (начало не может быть меньше 0, а размер - меньше 1)"));
        MvcResult mvcResult = mockMvc.perform(get("/items/search")
                        .param("text", "item1")
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userDto.getId()))
                .andExpect(status().isBadRequest()).andReturn();
        String actualResponseBody = mvcResult.getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(mapper
                .writeValueAsString(new ErrorResponse("Параметры для отображения данных " +
                        "заданы не верно (начало не может быть меньше 0, а размер - меньше 1)")));
    }

    @Test
    void searchWithFailFromTest() throws Exception {
        when(itemService.search(anyLong(), anyString(), anyInt(), anyInt()))
                .thenReturn(List.of(itemDto));
        mockMvc.perform(get("/items/search").param("text", "item1")
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userDto.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(itemDto.getName()), String.class));

        verify(itemService, times(1))
                .search(anyLong(), anyString(), anyInt(), anyInt());
    }

    @Test
    void addCommentTest() throws Exception {
        when(commentService.addComment(anyLong(), anyLong(), any()))
                .thenReturn(commentDto);
        mockMvc.perform(post("/items/{id}/comment", itemDto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(commentDto))
                        .header("X-Sharer-User-Id", userDto.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(commentDto.getId()), Long.class))
                .andExpect(jsonPath("$.text", is(commentDto.getText())))
                .andExpect(jsonPath("$.authorName", is(userDto.getName())));
    }
}

