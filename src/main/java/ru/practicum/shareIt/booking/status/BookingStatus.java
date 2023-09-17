package ru.practicum.shareIt.booking.status;

public enum BookingStatus {
    WAITING, // Новое бронирование, ожидает одобрения
    APPROVED, // Бронирование подтверждено владельцем
    REJECTED, // Бронирование отклонено владельцем
    CANCELED // Бронирование отменено создателем
}
