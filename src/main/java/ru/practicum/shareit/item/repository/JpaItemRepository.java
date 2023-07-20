package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.db.ItemEntity;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
interface JpaItemRepository extends ItemRepository, CrudRepository<ItemEntity, Long> {

    @NonNull
    @Override
    default List<Item> getItems(@NonNull Long userId, @NonNull Pageable page) {
        return findAllByUserId(userId, page).stream()
                .map(ItemMapper::map)
                .collect(Collectors.toList());
    }

    @NonNull
    @Override
    default Item addNewItem(@NonNull User user, @NonNull Item item, @Nullable ItemRequest request) {
        var userEntity = UserMapper.mapToEntity(user, user.getId());
        var requestEntity = ItemRequestMapper.mapToEntity(request);
        return ItemMapper.map(
                save(ItemMapper.mapToEntity(item, null, userEntity, requestEntity))
        );
    }

    @Override
    @Transactional
    default void deleteItem(@NonNull Long userId, @NonNull Long itemId) {
        findByIdAndUserId(itemId, userId)
                .ifPresentOrElse(this::delete, () -> {
                    throw new NotFoundException(String.format("Вещь с идентефикатором %1$s не найдена", itemId));
                });
    }

    @NonNull
    @Override
    @Transactional
    default Item update(@NonNull Long userId, @NonNull Item item) {
        return findByIdAndUserId(item.getId(), userId)
                .map(it -> save(ItemMapper.merge(item, it)))
                .map(ItemMapper::map)
                .orElseThrow(() -> new NotFoundException(String.format("Вещь с идентефикатором %1$s не найдена", item.getId())));
    }

    @NonNull
    @Override
    default Item getItem(@NonNull Long itemId) {
        return findById(itemId)
                .map(ItemMapper::map)
                .orElseThrow(() -> new NotFoundException(String.format("Вещь с идентефикатором %1$s не найдена", itemId)));
    }

    @NonNull
    @Override
    default List<Item> searchBy(@NonNull String text, @NonNull Pageable page) {
        if (text.isEmpty()) {
            return Collections.emptyList();
        }
        return findAllByQuery(text, page).stream()
                .map(ItemMapper::map)
                .collect(Collectors.toList());
    }

    List<ItemEntity> findAllByUserId(Long userId, Pageable page);

    Optional<ItemEntity> findByIdAndUserId(Long itemId, Long userId);

    @Query("SELECT i FROM ItemEntity i WHERE LOWER(i.name) LIKE LOWER(CONCAT('%', ?1, '%')) OR LOWER(i.description) LIKE LOWER(CONCAT('%', ?1, '%')) AND i.available = TRUE")
    List<ItemEntity> findAllByQuery(String query, Pageable page);
}