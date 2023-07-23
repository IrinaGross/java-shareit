package ru.practicum.shareit.client;

import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import ru.practicum.shareit.booking.dto.BookingDto;

public interface BookingClient {
    @NonNull
    ResponseEntity<Object> newRequest(@NonNull Long userId, @NonNull Long itemId, @NonNull BookingDto booking);

    @NonNull
    ResponseEntity<Object> changeRequestStatus(@NonNull Long userId, @NonNull Long bookingId, boolean isApproved);

    @NonNull
    ResponseEntity<Object> getRequestById(@NonNull Long userId, @NonNull Long bookingId);

    @NonNull
    ResponseEntity<Object> getAllRequests(@NonNull Long userId, @NonNull String state, @NonNull Integer from, @NonNull Integer size);

    @NonNull
    ResponseEntity<Object> getAllRequestsForOwner(@NonNull Long userId, @NonNull String state, @NonNull Integer from, @NonNull Integer size);
}
