package ru.practicum.shareit.request.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.NotFoundException;
import ru.practicum.shareit.request.db.ItemRequestEntity;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
interface JpaItemRequestRepository extends ItemRequestRepository, CrudRepository<ItemRequestEntity, Long> {
    @NonNull
    @Override
    default ItemRequest create(@NonNull User creator, @NonNull ItemRequest request) {
        var requestEntity = ItemRequestMapper.mapToEntity(
                request.toBuilder()
                        .createAt(LocalDateTime.now())
                        .creator(creator)
                        .build()
        );
        return ItemRequestMapper.map(save(Objects.requireNonNull(requestEntity)));
    }

    @NonNull
    @Override
    default List<ItemRequest> getAll(@NonNull Long userId) {
        return findByCreatorIdOrderByDateAsc(userId).stream()
                .map(ItemRequestMapper::map)
                .collect(Collectors.toList());
    }

    @NonNull
    @Override
    default ItemRequest getById(@NonNull Long requestId) {
        return findById(requestId)
                .map(ItemRequestMapper::map)
                .orElseThrow(() -> new NotFoundException(String.format("Запрос вещи с идентификатором %1$s не найден", requestId)));
    }

    @NonNull
    @Override
    default List<ItemRequest> getAll(@NonNull Long userId, @NonNull Pageable page) {
        return findByCreatorIdNotOrderByDateAsc(userId, page).stream()
                .map(ItemRequestMapper::map)
                .collect(Collectors.toList());
    }

    List<ItemRequestEntity> findByCreatorIdOrderByDateAsc(Long userId);

    List<ItemRequestEntity> findByCreatorIdNotOrderByDateAsc(Long userId, Pageable page);
}