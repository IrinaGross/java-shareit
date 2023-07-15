package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.NotFoundException;
import ru.practicum.shareit.booking.db.BookingEntity;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
interface JpaBookingRepository extends BookingRepository, CrudRepository<BookingEntity, Long> {

    @NonNull
    @Override
    default Booking create(@NonNull Booking model, @NonNull User user, @NonNull Item item, @Nullable ItemRequest request) {
        var userEntity = UserMapper.mapToEntity(user, user.getId());
        var requestEntity = ItemRequestMapper.mapToEntity(request);
        var itemEntity = ItemMapper.mapToEntity(item, item.getId(), userEntity, requestEntity);
        model = model.toBuilder().status(BookingStatus.WAITING).build();
        var bookingEntity = BookingMapper.mapToEntity(model, userEntity, itemEntity);
        return BookingMapper.map(save(bookingEntity));
    }

    @NonNull
    @Override
    default Booking getItem(@NonNull Long bookingId) {
        return findById(bookingId)
                .map(BookingMapper::map)
                .orElseThrow(() -> new NotFoundException(String.format("Бронирование с идентификатором %1$s не найдено", bookingId)));
    }

    @NonNull
    @Override
    @Transactional
    default Booking update(@NonNull Booking booking) {
        return findById(booking.getId())
                .map(it -> BookingMapper.merge(booking, it))
                .map(this::save)
                .map(BookingMapper::map)
                .orElseThrow();
    }

    @NonNull
    @Override
    default List<Booking> findAllByUserId(@NonNull Long userId, @NonNull BookingState state, @NonNull Pageable pageable) {
        Collection<BookingEntity> collection;
        switch (state) {
            case CURRENT:
                collection = findAllByUserIdAndStartBeforeAndEndAfterOrderByStartAsc(userId, LocalDateTime.now(), LocalDateTime.now(), pageable);
                break;
            case PAST:
                collection = findAllByUserIdAndEndBeforeOrderByStartDesc(userId, LocalDateTime.now(), pageable);
                break;
            case FUTURE:
                collection = findAllByUserIdAndStartAfterOrderByStartDesc(userId, LocalDateTime.now(), pageable);
                break;
            case WAITING:
                collection = findAllByUserIdAndStatusOrderByStartDesc(userId, BookingStatus.WAITING.toString(), pageable);
                break;
            case REJECTED:
                collection = findAllByUserIdAndStatusOrderByStartDesc(userId, BookingStatus.REJECTED.toString(), pageable);
                break;
            default:
                collection = findAllByUserIdOrderByStartDesc(userId, pageable);
                break;
        }
        return collection.stream()
                .map(BookingMapper::map)
                .collect(Collectors.toList());
    }

    @NonNull
    @Override
    default List<Booking> findAllByItemUserId(@NonNull Long userId, @NonNull BookingState state, @NonNull Pageable pageable) {
        Collection<BookingEntity> collection;
        switch (state) {
            case CURRENT:
                collection = findAllByItemUserIdAndStartBeforeAndEndAfterOrderByStartDesc(userId, LocalDateTime.now(), LocalDateTime.now(), pageable);
                break;
            case PAST:
                collection = findAllByItemUserIdAndEndBeforeOrderByStartDesc(userId, LocalDateTime.now(), pageable);
                break;
            case FUTURE:
                collection = findAllByItemUserIdAndStartAfterOrderByStartDesc(userId, LocalDateTime.now(), pageable);
                break;
            case WAITING:
                collection = findAllByItemUserIdAndStatusOrderByStartDesc(userId, BookingStatus.WAITING.toString(), pageable);
                break;
            case REJECTED:
                collection = findAllByItemUserIdAndStatusOrderByStartDesc(userId, BookingStatus.REJECTED.toString(), pageable);
                break;
            default:
                collection = findAllByItemUserIdOrderByStartDesc(userId, pageable);
                break;
        }

        return collection.stream()
                .map(BookingMapper::map)
                .collect(Collectors.toList());
    }

    @Nullable
    @Override
    default Booking findLastApproved(@NonNull Long itemId) {
        return findFirstByItemIdAndStatusAndStartBeforeOrderByEndDesc(itemId, BookingStatus.APPROVED.toString(), LocalDateTime.now())
                .map(BookingMapper::map)
                .orElse(null);
    }

    @Nullable
    @Override
    default Booking findNextApproved(@NonNull Long itemId) {
        return findFirstByItemIdAndStatusAndStartAfterOrderByStartAsc(itemId, BookingStatus.APPROVED.toString(), LocalDateTime.now())
                .map(BookingMapper::map)
                .orElse(null);
    }

    @Override
    default Booking findApprovedItemFor(@NonNull Long itemId, @NonNull Long userId) {
        return findFirstByItemIdAndUserIdAndStatusAndEndBefore(itemId, userId, BookingStatus.APPROVED.toString(), LocalDateTime.now())
                .map(BookingMapper::map)
                .orElse(null);
    }

    Optional<BookingEntity> findFirstByItemIdAndStatusAndStartBeforeOrderByEndDesc(Long itemId, String status, LocalDateTime date);

    Optional<BookingEntity> findFirstByItemIdAndStatusAndStartAfterOrderByStartAsc(Long itemId, String status, LocalDateTime date);

    Optional<BookingEntity> findFirstByItemIdAndUserIdAndStatusAndEndBefore(Long itemId, Long userId, String status, LocalDateTime date);

    List<BookingEntity> findAllByItemUserIdOrderByStartDesc(Long userId, Pageable pageable);

    List<BookingEntity> findAllByItemUserIdAndStatusOrderByStartDesc(Long userId, String status, Pageable pageable);

    List<BookingEntity> findAllByItemUserIdAndStartAfterOrderByStartDesc(Long userId, LocalDateTime date, Pageable pageable);

    List<BookingEntity> findAllByItemUserIdAndEndBeforeOrderByStartDesc(Long userId, LocalDateTime date, Pageable pageable);

    List<BookingEntity> findAllByItemUserIdAndStartBeforeAndEndAfterOrderByStartDesc(Long userId, LocalDateTime start, LocalDateTime end, Pageable pageable);

    List<BookingEntity> findAllByUserIdOrderByStartDesc(Long userId, Pageable pageable);

    List<BookingEntity> findAllByUserIdAndStartAfterOrderByStartDesc(Long userId, LocalDateTime date, Pageable pageable);

    List<BookingEntity> findAllByUserIdAndStartBeforeAndEndAfterOrderByStartAsc(Long userId, LocalDateTime start, LocalDateTime end, Pageable pageable);

    List<BookingEntity> findAllByUserIdAndStatusOrderByStartDesc(Long userId, String status, Pageable pageable);

    List<BookingEntity> findAllByUserIdAndEndBeforeOrderByStartDesc(Long userId, LocalDateTime date, Pageable pageable);
}