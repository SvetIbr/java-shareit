package ru.practicum.shareIt.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareIt.booking.model.Booking;
import ru.practicum.shareIt.booking.status.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Интерфейс хранилища всех бронирований
 *
 * @author Светлана Ибраева
 * @version 1.0
 */
public interface BookingRepository extends JpaRepository<Booking, Long> {
    /**
     * Метод получения списка всех бронирований текущего пользователя,
     * отсортированных по дате от более новых к более старым на текущий момент
     *
     * @param userId - идентификатор пользователя
     * @return список объектов Booking
     */
    List<Booking> findAllByBookerIdOrderByStartDesc(Long userId);

    /**
     * Метод получения списка всех завершенных бронирований текущего пользователя,
     * отсортированных по дате от более новых к более старым на текущий момент
     *
     * @param userId - идентификатор пользователя
     * @param time   - текущее время
     * @return список объектов Booking
     */
    List<Booking> findAllByBookerIdAndEndIsBeforeOrderByStartDesc(Long userId, LocalDateTime time);

    /**
     * Метод получения списка всех предстоящих бронирований текущего пользователя,
     * отсортированных по дате от более новых к более старым на текущий момент
     *
     * @param userId - идентификатор пользователя
     * @param now    - текущее время
     * @return список объектов Booking
     */
    List<Booking> findAllByBookerIdAndStartIsAfterOrderByStartDesc(Long userId, LocalDateTime now);

    /**
     * Метод получения списка всех текущих бронирований текущего пользователя,
     * отсортированных по дате от более новых к более старым на текущий момент
     *
     * @param userId - идентификатор пользователя
     * @param now    - начало текущего времени
     * @param now1   - окончание текущего времени
     * @return список объектов Booking
     */
    List<Booking> findAllByBookerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(Long userId, LocalDateTime now, LocalDateTime now1);

    /**
     * Метод получения списка всех бронирований с определенным статусом текущего пользователя,
     * отсортированных по дате от более новых к более старым на текущий момент
     *
     * @param userId        - идентификатор пользователя
     * @param bookingStatus - статус бронирования
     * @return список объектов Booking
     */
    List<Booking> findAllByBookerIdAndStatusOrderByStartDesc(Long userId, BookingStatus bookingStatus);

    /**
     * Метод получения списка бронирований для всех вещей текущего пользователя,
     * отсортированных по дате от более новых к более старым на текущий момент
     *
     * @param userId - идентификатор пользователя
     * @return список объектов Booking
     */
    List<Booking> findAllByItemOwnerIdOrderByStartDesc(Long userId);

    /**
     * Метод получения списка завершенных бронирований для всех вещей текущего пользователя,
     * отсортированных по дате от более новых к более старым на текущий момент
     *
     * @param userId - идентификатор пользователя
     * @param now    - текущее время
     * @return список объектов Booking
     */
    List<Booking> findAllByItemOwnerIdAndEndIsBeforeOrderByStartDesc(Long userId, LocalDateTime now);

    /**
     * Метод получения списка предстоящих бронирований для всех вещей текущего пользователя,
     * отсортированных по дате от более новых к более старым на текущий момент
     *
     * @param userId - идентификатор пользователя
     * @param now    - текущее время
     * @return список объектов Booking
     */
    List<Booking> findAllByItemOwnerIdAndStartIsAfterOrderByStartDesc(Long userId, LocalDateTime now);

    /**
     * Метод получения списка текущих бронирований для всех вещей текущего пользователя,
     * отсортированных по дате от более новых к более старым на текущий момент
     *
     * @param userId - идентификатор пользователя
     * @param now    - начало текущего времени
     * @param now1   - окончание текущего времени
     * @return список объектов Booking
     */
    List<Booking> findAllByItemOwnerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(Long userId, LocalDateTime now, LocalDateTime now1);

    /**
     * Метод получения списка бронирований с определенным статусом для всех вещей текущего пользователя,
     * отсортированных по дате от более новых к более старым на текущий момент
     *
     * @param userId        - идентификатор пользователя
     * @param bookingStatus - статус бронирования
     * @return список объектов Booking
     */
    List<Booking> findAllByItemOwnerIdAndStatusOrderByStartDesc(Long userId, BookingStatus bookingStatus);

    /**
     * Метод получения Optional бронирования для подтверждения, что пользователь
     * по идентификатору брал в аренду вещь
     *
     * @param itemId        - идентификатор вещи, которую брали в аренду
     * @param userId        - идентификатор пользователя, который брал в аренду
     * @param now           - текущее время
     * @param bookingStatus - статус бронирования
     * @return Optional<Booking>
     */
    Optional<Booking> findTop1BookingByItemIdAndBookerIdAndEndIsBeforeAndStatusIsOrderByEndDesc(Long itemId, Long userId, LocalDateTime now, BookingStatus bookingStatus);

    /**
     * Метод получения Optional последнего бронирования вещи
     *
     * @param id            - идентификатор вещи, которую брали в аренду
     * @param now           - текущее время
     * @param bookingStatus - статус бронирования
     * @return Optional<Booking>
     */
    Optional<Booking> findTop1BookingByItemIdAndStartIsBeforeAndStatusIsOrderByEndDesc(Long id, LocalDateTime now, BookingStatus bookingStatus);

    /**
     * Метод получения Optional следующего бронирования вещи
     *
     * @param id            - идентификатор вещи, которую брали в аренду
     * @param now           - текущее время
     * @param bookingStatus - статус бронирования
     * @return Optional<Booking>
     */
    Optional<Booking> findTop1BookingByItemIdAndStartIsAfterAndStatusIsOrderByStartAsc(Long id, LocalDateTime now, BookingStatus bookingStatus);
}
