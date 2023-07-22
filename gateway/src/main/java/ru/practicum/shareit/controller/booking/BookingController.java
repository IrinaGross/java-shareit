package ru.practicum.shareit.controller.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.client.BookingClient;

import javax.validation.constraints.Min;

import static ru.practicum.shareit.Const.*;

@Controller
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
@Validated
public class BookingController {
    private final BookingClient client;

    @PostMapping
    public ResponseEntity<Object> addRequest(
            @RequestHeader(X_SHARER_USER_ID_HEADER) @NonNull Long userId,
            @RequestBody @NonNull @Validated BookingDto booking
    ) {
        return client.newRequest(userId, booking.getItemId(), booking);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> changeStatus(
            @RequestHeader(X_SHARER_USER_ID_HEADER) @NonNull Long userId,
            @PathVariable(BOOKING_ID_PATH_NAME) @NonNull Long bookingId,
            @RequestParam("approved") boolean isApproved
    ) {
        return client.changeRequestStatus(userId, bookingId, isApproved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getById(
            @RequestHeader(X_SHARER_USER_ID_HEADER) @NonNull Long userId,
            @PathVariable(BOOKING_ID_PATH_NAME) @NonNull Long bookingId
    ) {
        return client.getRequestById(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllRequests(
            @RequestHeader(X_SHARER_USER_ID_HEADER) @NonNull Long userId,
            @RequestParam(name = STATE_REQUEST_PARAM, required = false, defaultValue = DEFAULT_STATE_VALUE) @NonNull String state,
            @RequestParam(name = FROM_REQUEST_PARAM, required = false, defaultValue = DEFAULT_FROM_VALUE) @Min(0) @NonNull Integer from,
            @RequestParam(name = SIZE_REQUEST_PARAM, required = false, defaultValue = DEFAULT_SIZE_VALUE) @Min(1) @NonNull Integer size
    ) {
        return client.getAllRequests(userId, state, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getAllRequestsForOwner(
            @RequestHeader(X_SHARER_USER_ID_HEADER) @NonNull Long userId,
            @RequestParam(name = STATE_REQUEST_PARAM, required = false, defaultValue = DEFAULT_STATE_VALUE) @NonNull String state,
            @RequestParam(name = FROM_REQUEST_PARAM, required = false, defaultValue = DEFAULT_FROM_VALUE) @Min(0) @NonNull Integer from,
            @RequestParam(name = SIZE_REQUEST_PARAM, required = false, defaultValue = DEFAULT_SIZE_VALUE) @Min(1) @NonNull Integer size
    ) {
        return client.getAllRequestsForOwner(userId, state, from, size);
    }
}
