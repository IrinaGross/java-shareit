package ru.practicum.shareit.booking.service;

import org.springframework.lang.NonNull;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.util.List;

public interface BookingService {
    @NonNull
    Booking newRequest(@NonNull Long userId, @NonNull Long itemId, @NonNull Booking model);

    @NonNull
    Booking changeRequestStatus(@NonNull Long userId, @NonNull Long bookingId, @NonNull BookingStatus status);

    @NonNull
    Booking getRequestById(@NonNull Long userId, @NonNull Long bookingId);

    @NonNull
    List<Booking> getAllRequests(@NonNull Long userId, @NonNull BookingState state);

    @NonNull
    List<Booking> getAllRequestsForOwner(@NonNull Long userId, @NonNull BookingState state);
}
