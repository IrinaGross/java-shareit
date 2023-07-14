package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.BadRequestException;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @NonNull
    @Override
    public List<Item> getItems(@NonNull Long userId) {
        User user = userRepository.getById(userId);
        return itemRepository.getItems(user.getId())
                .stream()
                .map(it -> inflateMore(it, true))
                .collect(Collectors.toList());
    }

    @NonNull
    @Override
    public Item addNewItem(@NonNull Long userId, @NonNull Item item) {
        User user = userRepository.getById(userId);
        return itemRepository.addNewItem(user, item);
    }

    @Override
    public void deleteItem(@NonNull Long userId, @NonNull Long itemId) {
        User user = userRepository.getById(userId);
        itemRepository.deleteItem(user.getId(), itemId);
    }

    @NonNull
    @Override
    public Item updateItem(@NonNull Long userId, @NonNull Item item) {
        User user = userRepository.getById(userId);
        return itemRepository.update(user.getId(), item);
    }

    @NonNull
    @Override
    public Item getItem(@NonNull Long userId, @NonNull Long itemId) {
        userRepository.getById(userId);
        Item item = itemRepository.getItem(itemId);
        return inflateMore(item, item.getOwner().getId().equals(userId));
    }

    @NonNull
    @Override
    public List<Item> searchBy(@NonNull String text) {
        return itemRepository.searchBy(text);
    }

    @NonNull
    @Override
    public Comment createComment(@NonNull Long userId, @NonNull Long itemId, @NonNull Comment comment) {
        var booking = bookingRepository.findApprovedItemFor(itemId, userId);
        if (booking == null) {
            throw new BadRequestException("Не найдена запись о подтвержденном бронировании");
        }
        var item = itemRepository.getItem(itemId);
        var user = userRepository.getById(userId);
        return commentRepository.create(item, user, comment);
    }

    private Item inflateMore(Item item, boolean loadLastAndNext) {
        var builder = item.toBuilder()
                .comments(commentRepository.find(item.getId()));
        if (loadLastAndNext) {
            builder.last(bookingRepository.findLastApproved(item.getId()))
                    .next(bookingRepository.findNextApproved(item.getId()));
        }
        return builder.build();
    }
}
