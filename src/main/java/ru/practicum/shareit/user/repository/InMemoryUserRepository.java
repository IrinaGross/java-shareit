package ru.practicum.shareit.user.repository;

import org.springframework.lang.NonNull;
import ru.practicum.shareit.ConflictException;
import ru.practicum.shareit.NotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

//@Repository
class InMemoryUserRepository implements UserRepository {
    private Long lastId = 0L;
    private final HashMap<Long, User> users = new HashMap<>();

    @NonNull
    @Override
    public User getById(@NonNull Long userId) {
        return getOrThrow(userId);
    }

    @NonNull
    @Override
    public User addUser(@NonNull User user) {
        checkEmail(user.getId(), user.getEmail());
        Long id = ++lastId;
        User newUser = user.toBuilder()
                .id(id)
                .build();
        users.put(id, newUser);
        return newUser;
    }

    @NonNull
    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @NonNull
    @Override
    public User updateUser(@NonNull User user) {
        Long userId = user.getId();
        User saved = getOrThrow(userId);
        String newName = user.getName();
        String newEmail = user.getEmail();
        User updated = saved.toBuilder()
                .name(newName == null ? saved.getName() : newName)
                .email(newEmail == null ? saved.getEmail() : checkEmail(userId, newEmail))
                .build();
        users.put(userId, updated);
        return updated;
    }

    @Override
    public void deleteUser(@NonNull Long userId) {
        getOrThrow(userId);
        users.remove(userId);
    }

    @NonNull
    private User getOrThrow(Long userId) {
        User user = users.get(userId);
        if (user == null) {
            throw new NotFoundException(String.format("Пользователь с идентефикатором %1$s не найден", userId));
        }
        return user;
    }

    private String checkEmail(Long userId, String email) {
        boolean exist = users.values()
                .stream()
                .anyMatch(it -> Objects.equals(it.getEmail(), email) && !Objects.equals(it.getId(), userId));
        if (exist) {
            throw new ConflictException(String.format("Пользователь с email %1$s уже существует", email));
        }
        return email;
    }
}