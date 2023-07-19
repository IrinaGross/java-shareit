package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ItemRequestRepository itemRequestRepository;

    @NonNull
    @Override
    public Booking newRequest(@NonNull Long userId, @NonNull Long itemId, @NonNull Booking model) {
        if (model.getStart().isEqual(model.getEnd())) {
            throw new BadRequestException("Нельзя забронировать вещь. Даты начала и окончания брони одинаковы");
        }
        if (model.getEnd().isBefore(model.getStart())) {
            throw new BadRequestException("Нельзя забронировать вещь. Дата окончания брони меньше даты начала брони");
        }
        var item = itemRepository.getItem(itemId);
        if (Objects.equals(item.getOwner().getId(), userId)) {
            throw new NotFoundException("Нет доступа");
        }
        if (!item.getAvailable()) {
            throw new BadRequestException(String.format("Вещь c идентификаторм %1$s не доступна для бронирования", itemId));
        }
        ItemRequest itemRequest = null;
        var requestId = item.getRequestId();
        if (requestId != null) {
            itemRequest = itemRequestRepository.getById(requestId);
        }
        var user = userRepository.getById(userId);
        return bookingRepository.create(model, user, item, itemRequest);
    }

    @NonNull
    @Override
    public Booking changeRequestStatus(@NonNull Long userId, @NonNull Long bookingId, @NonNull BookingStatus status) {
        userRepository.getById(userId); // check exist
        var booking = bookingRepository.getItem(bookingId);
        var item = itemRepository.getItem(booking.getItem().getId());
        if (!Objects.equals(item.getOwner().getId(), userId)) {
            throw new NotFoundException("Нет доступа");
        }
        if (booking.getStatus() != BookingStatus.WAITING) {
            throw new BadRequestException("Статус уже установлен");
        }
        return bookingRepository.update(
                booking.toBuilder().status(status).build()
        );
    }

    @NonNull
    @Override
    public Booking getRequestById(@NonNull Long userId, @NonNull Long bookingId) {
        userRepository.getById(userId); // check exist
        var booking = bookingRepository.getItem(bookingId);
        var item = itemRepository.getItem(booking.getItem().getId());
        if (Objects.equals(booking.getBooker().getId(), userId) || Objects.equals(item.getOwner().getId(), userId)) {
            return booking;
        }
        throw new NotFoundException("Нет доступа");
    }

    @NonNull
    @Override
    public List<Booking> getAllRequests(@NonNull Long userId, @NonNull BookingState state, @NonNull Pageable pageable) {
        var user = userRepository.getById(userId);
        return bookingRepository.findAllByUserId(user.getId(), state, pageable);
    }

    @NonNull
    @Override
    public List<Booking> getAllRequestsForOwner(@NonNull Long userId, @NonNull BookingState state, @NonNull Pageable pageable) {
        var user = userRepository.getById(userId);
        return bookingRepository.findAllByItemUserId(user.getId(), state, pageable);
    }
}