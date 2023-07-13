package ru.practicum.shareit.booking.repository;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface BookingRepository {
    @NonNull
    Booking create(@NonNull Booking model, @NonNull User user, @NonNull Item item);

    @NonNull
    Booking getItem(@NonNull Long bookingId);

    @NonNull
    Booking update(@NonNull Booking booking);

    @NonNull
    List<Booking> findAllByUserId(@NonNull Long userId, @NonNull BookingState state);

    @NonNull
    List<Booking> findAllByItemUserId(@NonNull Long userId, @NonNull BookingState state);

    @Nullable
    Booking findLastApproved(@NonNull Long itemId);

    @Nullable
    Booking findNextApproved(@NonNull Long itemId);

    @Nullable
    Booking findApprovedItemFor(@NonNull Long itemId, @NonNull Long userId);
}
