package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Utils;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.UnsupportedStatusException;

import javax.validation.constraints.Min;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.Const.*;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDto addRequest(
            @RequestHeader(X_SHARER_USER_ID_HEADER) long userId,
            @RequestBody @NonNull @Validated BookingDto dto
    ) {
        var model = BookingMapper.map(dto);
        return BookingMapper.mapToDto(bookingService.newRequest(userId, dto.getItemId(), model));
    }

    @PatchMapping("/{bookingId}")
    public BookingDto changeStatus(
            @RequestHeader(X_SHARER_USER_ID_HEADER) long userId,
            @PathVariable(BOOKING_ID_PATH_NAME) @NonNull Long bookingId,
            @RequestParam("approved") boolean isApproved
    ) {
        var status = isApproved ? BookingStatus.APPROVED : BookingStatus.REJECTED;
        return BookingMapper.mapToDto(bookingService.changeRequestStatus(userId, bookingId, status));
    }

    @GetMapping("/{bookingId}")
    public BookingDto getById(
            @RequestHeader(X_SHARER_USER_ID_HEADER) long userId,
            @PathVariable(BOOKING_ID_PATH_NAME) @NonNull Long bookingId
    ) {
        return BookingMapper.mapToDto(bookingService.getRequestById(userId, bookingId));
    }

    @GetMapping
    public List<BookingDto> getAllRequests(
            @RequestHeader(X_SHARER_USER_ID_HEADER) long userId,
            @RequestParam(name = STATE_REQUEST_PARAM, required = false) String state,
            @RequestParam(name = FROM_REQUEST_PARAM, required = false, defaultValue = DEFAULT_FROM_VALUE) @Min(0) @NonNull Integer from,
            @RequestParam(name = SIZE_REQUEST_PARAM, required = false, defaultValue = DEFAULT_SIZE_VALUE) @Min(1) @NonNull Integer size
    ) {
        return bookingService.getAllRequests(userId, asBookingState(state), Utils.newPage(from, size))
                .stream()
                .map(BookingMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllRequestsForOwner(
            @RequestHeader(X_SHARER_USER_ID_HEADER) long userId,
            @RequestParam(name = STATE_REQUEST_PARAM, required = false) String state,
            @RequestParam(name = FROM_REQUEST_PARAM, required = false, defaultValue = DEFAULT_FROM_VALUE) @Min(0) @NonNull Integer from,
            @RequestParam(name = SIZE_REQUEST_PARAM, required = false, defaultValue = DEFAULT_SIZE_VALUE) @Min(1) @NonNull Integer size
    ) {
        return bookingService.getAllRequestsForOwner(userId, asBookingState(state), Utils.newPage(from, size))
                .stream()
                .map(BookingMapper::mapToDto)
                .collect(Collectors.toList());
    }

    private static BookingState asBookingState(@Nullable String state) {
        if (state == null || state.isEmpty()) {
            return BookingState.ALL;
        } else {
            try {
                return BookingState.valueOf(state);
            } catch (IllegalArgumentException e) {
                throw new UnsupportedStatusException(String.format("Unknown state: %1$s", state));
            }
        }
    }
}