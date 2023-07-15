package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.UnsupportedStatusException;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.constraints.Min;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.ExceptionMapper.getCustomBody;
import static ru.practicum.shareit.Utils.X_SHARER_USER_ID_HEADER;

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
            @PathVariable("bookingId") @NonNull Long bookingId,
            @RequestParam("approved") boolean isApproved
    ) {
        var status = isApproved ? BookingStatus.APPROVED : BookingStatus.REJECTED;
        return BookingMapper.mapToDto(bookingService.changeRequestStatus(userId, bookingId, status));
    }

    @GetMapping("/{bookingId}")
    public BookingDto getById(
            @RequestHeader(X_SHARER_USER_ID_HEADER) long userId,
            @PathVariable("bookingId") @NonNull Long bookingId
    ) {
        return BookingMapper.mapToDto(bookingService.getRequestById(userId, bookingId));
    }

    @GetMapping
    public List<BookingDto> getAllRequests(
            @RequestHeader(X_SHARER_USER_ID_HEADER) long userId,
            @RequestParam(name = "state", required = false) String state,
            @RequestParam(name = "from", required = false, defaultValue = "0") @Min(0) @NonNull Integer from,
            @RequestParam(name = "size", required = false, defaultValue = "10") @Min(1) @NonNull Integer size
    ) {
        return bookingService.getAllRequests(userId, asBookingState(state), from, size)
                .stream()
                .map(BookingMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllRequestsForOwner(
            @RequestHeader(X_SHARER_USER_ID_HEADER) long userId,
            @RequestParam(name = "state", required = false) String state,
            @RequestParam(name = "from", required = false, defaultValue = "0") @Min(0) @NonNull Integer from,
            @RequestParam(name = "size", required = false, defaultValue = "10") @Min(1) @NonNull Integer size
    ) {
        return bookingService.getAllRequestsForOwner(userId, asBookingState(state), from, size)
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

    @SuppressWarnings("unused")
    @ExceptionHandler(UnsupportedStatusException.class)
    public ResponseEntity<Object> handleConflict(UnsupportedStatusException ex) {
        return new ResponseEntity<>(getCustomBody(ex), HttpStatus.BAD_REQUEST);
    }
}