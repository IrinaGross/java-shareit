package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.Utils;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
class ItemRequestServiceImpl implements ItemRequestService {
    private final UserRepository userRepository;
    private final ItemRequestRepository itemRequestRepository;

    @NonNull
    @Override
    public ItemRequest create(@NonNull Long userId, @NonNull ItemRequest request) {
        var creator = userRepository.getById(userId);
        return itemRequestRepository.create(creator, request);
    }

    @NonNull
    @Override
    public List<ItemRequest> getAll(@NonNull Long userId) {
        userRepository.getById(userId);
        return itemRequestRepository.getAll(userId);
    }

    @NonNull
    @Override
    public ItemRequest getById(@NonNull Long userId, @NonNull Long requestId) {
        userRepository.getById(userId);
        return itemRequestRepository.getById(requestId);
    }

    @NonNull
    @Override
    public List<ItemRequest> getAll(@NonNull Long userId, @NonNull Integer from, @NonNull Integer size) {
        userRepository.getById(userId);
        return itemRequestRepository.getAll(userId, Utils.newPage(from, size));
    }
}
