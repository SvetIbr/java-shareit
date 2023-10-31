package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.status.BookingStatus;
import ru.practicum.shareit.error.ErrorResponse;
import ru.practicum.shareit.error.exception.BookingNotFoundException;
import ru.practicum.shareit.error.exception.InvalidDateTimeException;
import ru.practicum.shareit.error.exception.InvalidStatusException;
import ru.practicum.shareit.error.exception.NoAccessException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = BookingController.class)
@AutoConfigureMockMvc
public class BookingControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private BookingService bookingService;
    private final User user = new User(1L, "User1", "user1@mail.ru");
    private final User user2 = new User(2L, "User2", "user2@mail.ru");
    private final Item item = new Item(1L, "item1", "description1", true, user, null);
    private final LocalDateTime start = LocalDateTime.now().plusDays(5);
    private final LocalDateTime end = LocalDateTime.now().plusDays(9);
    private final Booking booking = new Booking(1L, start, end, item, user2, BookingStatus.WAITING);
    private final BookingRequestDto bookingRequestDto = BookingRequestDto.builder()
            .id(1L).start(start).end(end)
            .itemId(item.getId()).bookerId(user2.getId()).status(BookingStatus.WAITING).build();
    private final BookingResponseDto bookingResponseDto = BookingMapper.toBookingResponseDto(booking);

    @Test
    void createTest() throws Exception {
        when(bookingService.create(anyLong(), any())).thenReturn(BookingMapper.toBookingResponseDto(booking));
        mockMvc.perform(post("/bookings")
                        .content(objectMapper.writeValueAsString(bookingRequestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", user.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(booking.getId()), Long.class))
                .andExpect(jsonPath("$.item.name", is(booking.getItem().getName())));
    }

    @Test
    void createWithEndBeforeStartTest() throws Exception {
        when(bookingService.create(anyLong(), any()))
                .thenThrow(new InvalidDateTimeException("Дата окончания бронирования " +
                        "не может быть раньше даты начала"));
        MvcResult mvcResult = mockMvc.perform(post("/bookings")
                        .content(objectMapper.writeValueAsString(bookingRequestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", user.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest())
                .andReturn();
        String actualResponseBody = mvcResult.getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(objectMapper
                .writeValueAsString(new ErrorResponse("Дата окончания бронирования " +
                        "не может быть раньше даты начала")));
    }

    @Test
    void approveTest() throws Exception {
        booking.setStatus(BookingStatus.APPROVED);
        when(bookingService.approve(any(), any(), anyBoolean()))
                .thenReturn(BookingMapper.toBookingResponseDto(booking));

        mockMvc.perform(patch("/bookings/{id}", booking.getId())
                        .param("approved", "true")
                        .content(objectMapper.writeValueAsString(bookingRequestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(notNullValue())))
                .andExpect(jsonPath("$.status", is(BookingStatus.APPROVED.toString())));
    }

    @Test
    void getByIdTest() throws Exception {
        when(bookingService.getById(any(), any())).thenReturn(bookingResponseDto);
        mockMvc.perform(get("/bookings/{id}", booking.getId())
                        .header("X-Sharer-User-Id", user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(booking.getId()), Long.class))
                .andExpect(jsonPath("$.item.name", is(booking.getItem().getName())))
                .andExpect(jsonPath("$.item.id", is(booking.getItem().getId()), Long.class));
    }

    @Test
    void getByIdWithFailBookingIdTest() throws Exception {
        when(bookingService.getById(any(), any()))
                .thenThrow(new BookingNotFoundException("Заявка на бронирование с идентификатором 15 не найдена"));
        MvcResult mvcResult = mockMvc.perform(get("/bookings/15")
                        .header("X-Sharer-User-Id", user.getId()))
                .andExpect(status().isNotFound())
                .andReturn();
        String actualResponseBody = mvcResult.getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(objectMapper
                .writeValueAsString(new ErrorResponse("Заявка на бронирование с идентификатором 15 не найдена")));
    }

    @Test
    void getByIdWithFailOwnerIdTest() throws Exception {
        when(bookingService.getById(any(), any()))
                .thenThrow(new NoAccessException("У пользователя нет прав " +
                        "для просмотра данной заявки на бронирование"));
        MvcResult mvcResult = mockMvc.perform(get("/bookings/{id}", booking.getId())
                        .header("X-Sharer-User-Id", user.getId()))
                .andExpect(status().isNotFound())
                .andReturn();
        String actualResponseBody = mvcResult.getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(objectMapper
                .writeValueAsString(new ErrorResponse("У пользователя нет прав " +
                        "для просмотра данной заявки на бронирование")));
    }

    @Test
    void getAllBookingByUserTest() throws Exception {
        when(bookingService.getAllBookingByUser(anyLong(), anyString(), anyInt(), anyInt()))
                .thenReturn(Collections.singletonList(bookingResponseDto));
        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", user2.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk());
    }

    @Test
    void getAllBookingByUserWithFailStateTest() throws Exception {
        when(bookingService.getAllBookingByUser(anyLong(), anyString(), anyInt(), anyInt()))
                .thenThrow(new InvalidStatusException("Unknown state: value"));
        MvcResult mvcResult = mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", user2.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest())
                .andReturn();
        String actualResponseBody = mvcResult.getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(objectMapper
                .writeValueAsString(new ErrorResponse("Unknown state: value")));
    }

    @Test
    void getAllBookingByOwnerTest() throws Exception {
        when(bookingService.getAllBookingByOwner(anyLong(), anyString(), anyInt(), anyInt()))
                .thenReturn(Collections.singletonList(bookingResponseDto));
        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk());
    }
}

