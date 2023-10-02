package ru.practicum.shareIt.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareIt.booking.model.Booking;
import org.springframework.data.domain.Sort;
import ru.practicum.shareIt.booking.status.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByBookerIdOrderByStartDesc(Long userId);
    List<Booking> findAllByBookerIdAndEndIsBefore(Long userId, LocalDateTime time, Sort sort);

    List<Booking> findAllByBookerIdAndStartIsAfter(Long userId, LocalDateTime now, Sort sort);

    List<Booking> findAllByBookerIdAndStartIsBeforeAndEndIsAfter(Long userId, LocalDateTime now, LocalDateTime now1, Sort sort);

    List<Booking> findAllByBookerIdAndStatus(Long userId, BookingStatus bookingStatus);

    List<Booking> findAllByItemOwnerIdOrderByStartDesc(Long userId);

    List<Booking> findAllByItemOwnerIdAndEndIsBefore(Long userId, LocalDateTime now, Sort sort);

    List<Booking> findAllByItemOwnerIdAndStartIsAfter(Long userId, LocalDateTime now, Sort sort);

    List<Booking> findAllByItemOwnerIdAndStartIsBeforeAndEndIsAfter(Long userId, LocalDateTime now, LocalDateTime now1, Sort sort);

    List<Booking> findAllByItemOwnerIdAndStatus(Long userId, BookingStatus bookingStatus);

    Optional<Booking> findTop1BookingByItemIdAndEndIsBeforeAndStatusIs(Long itemId, LocalDateTime now, BookingStatus bookingStatus, Sort sortDesc);

    Optional<Booking> findTop1BookingByItemIdAndEndIsAfterAndStatusIs(Long itemId, LocalDateTime now, BookingStatus bookingStatus, Sort sortAsc);

    Optional<Booking> findTop1BookingByItemIdAndBookerIdAndEndIsBeforeAndStatusIs(Long itemId, Long userId, LocalDateTime now, BookingStatus bookingStatus, Sort sortDesc);

    Optional<Booking> findTop1BookingByItemIdAndStartIsBeforeAndStatusIs(Long itemId, LocalDateTime now, BookingStatus bookingStatus, Sort sortDesc);

    Optional<Booking> findTop1BookingByItemIdAndStartIsAfterAndStatusIs(Long itemId, LocalDateTime now, BookingStatus bookingStatus, Sort sortAsc);
}
