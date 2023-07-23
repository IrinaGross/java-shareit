package ru.practicum.shareit.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.configuration.GatewayConfig;
import ru.practicum.shareit.exception.BadRequestException;

import java.util.Map;

import static ru.practicum.shareit.Const.*;

@Service
class BookingServiceClient extends BaseClient implements BookingClient {
    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingServiceClient(GatewayConfig config, RestTemplateBuilder builder) {
        super(builder, config.buildServerRoute(API_PREFIX));
    }

    @NonNull
    @Override
    public ResponseEntity<Object> newRequest(@NonNull Long userId, @NonNull Long itemId, @NonNull BookingDto booking) {
        if (booking.getStart().isEqual(booking.getEnd())) {
            throw new BadRequestException("Нельзя забронировать вещь. Даты начала и окончания брони одинаковы");
        }
        if (booking.getEnd().isBefore(booking.getStart())) {
            throw new BadRequestException("Нельзя забронировать вещь. Дата окончания брони меньше даты начала брони");
        }

        return post("", userId, booking);
    }

    @NonNull
    @Override
    public ResponseEntity<Object> changeRequestStatus(@NonNull Long userId, @NonNull Long bookingId, boolean isApproved) {
        return patch(
                String.format("/%d?approved={approved}", bookingId),
                userId,
                Map.of("approved", isApproved)
        );
    }

    @NonNull
    @Override
    public ResponseEntity<Object> getRequestById(@NonNull Long userId, @NonNull Long bookingId) {
        return get("/" + bookingId, userId);
    }

    @NonNull
    @Override
    public ResponseEntity<Object> getAllRequests(@NonNull Long userId, @NonNull String state, @NonNull Integer from, @NonNull Integer size) {
        return get(
                "?state={state}&from={from}&size={size}",
                userId,
                Map.of(STATE_REQUEST_PARAM, state,
                        FROM_REQUEST_PARAM, from,
                        SIZE_REQUEST_PARAM, size)
        );
    }

    @NonNull
    @Override
    public ResponseEntity<Object> getAllRequestsForOwner(@NonNull Long userId, @NonNull String state, @NonNull Integer from, @NonNull Integer size) {
        return get(
                "/owner?state={state}&from={from}&size={size}",
                userId,
                Map.of(STATE_REQUEST_PARAM, state,
                        FROM_REQUEST_PARAM, from,
                        SIZE_REQUEST_PARAM, size)
        );
    }
}
