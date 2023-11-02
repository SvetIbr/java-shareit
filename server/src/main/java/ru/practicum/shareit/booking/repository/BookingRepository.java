package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.status.BookingStatus;

import java.time.LocalDateTime;
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
    Page<Booking> findAllByBookerIdOrderByStartDesc(Long userId, Pageable pageable);

    /**
     * Метод получения списка всех завершенных бронирований текущего пользователя,
     * отсортированных по дате от более новых к более старым на текущий момент
     *
     * @param userId - идентификатор пользователя
     * @param time   - текущее время
     * @return список объектов Booking
     */
    Page<Booking> findAllByBookerIdAndEndIsBeforeOrderByStartDesc(Long userId, LocalDateTime time,
                                                                  Pageable pageable);

    /**
     * Метод получения списка всех предстоящих бронирований текущего пользователя,
     * отсортированных по дате от более новых к более старым на текущий момент
     *
     * @param userId - идентификатор пользователя
     * @param now    - текущее время
     * @return список объектов Booking
     */
    Page<Booking> findAllByBookerIdAndStartIsAfterOrderByStartDesc(Long userId, LocalDateTime now,
                                                                   Pageable pageable);

    /**
     * Метод получения списка всех текущих бронирований текущего пользователя,
     * отсортированных по дате от более новых к более старым на текущий момент
     *
     * @param userId - идентификатор пользователя
     * @param now    - начало текущего времени
     * @param now1   - окончание текущего времени
     * @return список объектов Booking
     */
    Page<Booking> findAllByBookerIdAndStartIsBeforeAndEndIsAfterOrderByStartAsc(Long userId,
                                                                                LocalDateTime now,
                                                                                LocalDateTime now1,
                                                                                Pageable pageable);

    /**
     * Метод получения списка всех бронирований с определенным статусом текущего пользователя,
     * отсортированных по дате от более новых к более старым на текущий момент
     *
     * @param userId        - идентификатор пользователя
     * @param bookingStatus - статус бронирования
     * @return список объектов Booking
     */
    Page<Booking> findAllByBookerIdAndStatusOrderByStartDesc(Long userId, BookingStatus bookingStatus,
                                                             Pageable pageable);

    /**
     * Метод получения списка бронирований для всех вещей текущего пользователя,
     * отсортированных по дате от более новых к более старым на текущий момент
     *
     * @param userId - идентификатор пользователя
     * @return список объектов Booking
     */
    Page<Booking> findAllByItemOwnerIdOrderByStartDesc(Long userId, Pageable pageable);

    /**
     * Метод получения списка завершенных бронирований для всех вещей текущего пользователя,
     * отсортированных по дате от более новых к более старым на текущий момент
     *
     * @param userId - идентификатор пользователя
     * @param now    - текущее время
     * @return список объектов Booking
     */
    Page<Booking> findAllByItemOwnerIdAndEndIsBeforeOrderByStartDesc(Long userId,
                                                                     LocalDateTime now, Pageable pageable);

    /**
     * Метод получения списка предстоящих бронирований для всех вещей текущего пользователя,
     * отсортированных по дате от более новых к более старым на текущий момент
     *
     * @param userId - идентификатор пользователя
     * @param now    - текущее время
     * @return список объектов Booking
     */
    Page<Booking> findAllByItemOwnerIdAndStartIsAfterOrderByStartDesc(Long userId,
                                                                      LocalDateTime now, Pageable pageable);

    /**
     * Метод получения списка текущих бронирований для всех вещей текущего пользователя,
     * отсортированных по дате от более новых к более старым на текущий момент
     *
     * @param userId - идентификатор пользователя
     * @param now    - начало текущего времени
     * @param now1   - окончание текущего времени
     * @return список объектов Booking
     */
    Page<Booking> findAllByItemOwnerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(Long userId,
                                                                                    LocalDateTime now,
                                                                                    LocalDateTime now1,
                                                                                    Pageable pageable);

    /**
     * Метод получения списка бронирований с определенным статусом для всех вещей текущего пользователя,
     * отсортированных по дате от более новых к более старым на текущий момент
     *
     * @param userId        - идентификатор пользователя
     * @param bookingStatus - статус бронирования
     * @return список объектов Booking
     */
    Page<Booking> findAllByItemOwnerIdAndStatusOrderByStartDesc(Long userId, BookingStatus bookingStatus,
                                                                Pageable pageable);

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
    Optional<Booking> findTop1BookingByItemIdAndBookerIdAndEndIsBeforeAndStatusIsOrderByEndDesc(Long itemId,
                                                                                                Long userId,
                                                                                                LocalDateTime now,
                                                                                                BookingStatus
                                                                                                        bookingStatus);

    /**
     * Метод получения Optional последнего бронирования вещи
     *
     * @param id            - идентификатор вещи, которую брали в аренду
     * @param now           - текущее время
     * @param bookingStatus - статус бронирования
     * @return Optional<Booking>
     */
    Optional<Booking> findTop1BookingByItemIdAndStartIsBeforeAndStatusIsOrderByEndDesc(Long id,
                                                                                       LocalDateTime now,
                                                                                       BookingStatus bookingStatus);

    /**
     * Метод получения Optional следующего бронирования вещи
     *
     * @param id            - идентификатор вещи, которую брали в аренду
     * @param now           - текущее время
     * @param bookingStatus - статус бронирования
     * @return Optional<Booking>
     */
    Optional<Booking> findTop1BookingByItemIdAndStartIsAfterAndStatusIsOrderByStartAsc(Long id,
                                                                                       LocalDateTime now,
                                                                                       BookingStatus bookingStatus);

}
