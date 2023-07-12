package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.NotFoundException;
import ru.practicum.shareit.item.db.ItemEntity;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
interface JpaItemRepository extends ItemRepository, CrudRepository<ItemEntity, Long> {

    @NonNull
    @Override
    default List<Item> getItems(@NonNull Long userId) {
        return findAllByUserId(userId).stream()
                .map(ItemMapper::map)
                .collect(Collectors.toList());
    }

    @NonNull
    @Override
    default Item addNewItem(@NonNull Long userId, @NonNull Item item) {
        return ItemMapper.map(
                save(ItemMapper.map(item, null, userId))
        );
    }

    @Override
    @Transactional
    default void deleteItem(@NonNull Long userId, @NonNull Long itemId) {
        findByItemIdAndUserId(itemId, userId)
                .ifPresentOrElse(this::delete, () -> {
                    throw new NotFoundException(String.format("Вещь с идентефикатором %1$s не найдена", itemId));
                });
    }

    @NonNull
    @Override
    @Transactional
    default Item update(@NonNull Long userId, @NonNull Item item) {
        return findByItemIdAndUserId(item.getId(), userId)
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
    default List<Item> searchBy(@NonNull String text) {
        if (text.isEmpty()) {
            return Collections.emptyList();
        }
        return findAllByQuery(text).stream()
                .map(ItemMapper::map)
                .collect(Collectors.toList());
    }

    @Query(value = "SELECT * FROM item_table WHERE item_owner_id = ?1", nativeQuery = true)
    Collection<ItemEntity> findAllByUserId(Long userId);

    @Query(value = "SELECT * FROM item_table WHERE item_id = ?1 and item_owner_id = ?2", nativeQuery = true)
    Optional<ItemEntity> findByItemIdAndUserId(Long itemId, Long userId);

    @Query("SELECT i FROM ItemEntity i WHERE LOWER(i.name) LIKE LOWER(CONCAT('%', ?1, '%')) OR LOWER(i.description) LIKE LOWER(CONCAT('%', ?1, '%')) AND i.available = TRUE")
    Collection<ItemEntity> findAllByQuery(String query);
}