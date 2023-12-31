package ru.practicum.shareit.booking.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.shareit.booking.status.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Класс бронирования со свойствами <b>id</b>, <b>start</b>, <b>end</b>,
 * <b>item</b>, <b>booker</b> и <b>status</b> для работы с базой данных
 *
 * @author Светлана Ибраева
 * @version 1.0
 */
@Entity
@Table(name = "bookings")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {
    /**
     * Поле идентификатор
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Поле дата начала бронирования
     */
    @Column(name = "start_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd, hh:mm:ss")
    private LocalDateTime start;

    /**
     * Поле дата окончания бронирования
     */
    @Column(name = "end_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd, hh:mm:ss")
    private LocalDateTime end;

    /**
     * Поле арендуемая вещь
     */
    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    /**
     * Поле арендатор
     */
    @ManyToOne
    @JoinColumn(name = "booker_id")
    private User booker;

    /**
     * Поле статус бронирования
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private BookingStatus status;
}

