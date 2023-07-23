package ru.practicum.shareit.item.repository;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface CommentRepository {
    @NonNull
    Comment create(@NonNull Item item, @NonNull User user, @NonNull Comment comment, @Nullable ItemRequest request);

    @NonNull
    List<Comment> find(@NonNull Long itemId);
}
