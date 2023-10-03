package ru.practicum.shareIt.booking.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.shareIt.booking.status.BookingStatus;
import ru.practicum.shareIt.item.model.Item;
import ru.practicum.shareIt.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;
        return Objects.equals(id, booking.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

