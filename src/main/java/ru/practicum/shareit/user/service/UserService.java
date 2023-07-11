package ru.practicum.shareit.user.service;

import org.springframework.lang.NonNull;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {

    User addUser(@NonNull User user);

    @NonNull
    User getUser(@NonNull Long userId);

    @NonNull
    List<User> getAllUsers();

    User updateUser(@NonNull User user);

    void deleteUser(@NonNull Long userId);
}
