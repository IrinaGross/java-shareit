package ru.practicum.shareit.item.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.db.CommentEntity;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Repository
interface JpaCommentRepository extends CommentRepository, CrudRepository<CommentEntity, Long> {
    @NonNull
    @Override
    default Comment create(@NonNull Item item, @NonNull User user, @NonNull Comment comment) {
        var userEntity = UserMapper.mapToEntity(user, user.getId());
        var itemEntity = ItemMapper.mapToEntity(item, item.getId(), userEntity);
        var entity = save(CommentMapper.mapToEntity(comment, itemEntity, userEntity));
        return CommentMapper.map(entity);
    }

    @NonNull
    @Override
    default List<Comment> find(@NonNull Long itemId) {
        return findAllByItemId(itemId)
                .stream()
                .map(CommentMapper::map)
                .collect(Collectors.toList());
    }

    Collection<CommentEntity> findAllByItemId(Long itemId);
}