package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User addUser(@NonNull User user) {
        return userRepository.addUser(user);
    }

    @NonNull
    @Override
    public User getUser(@NonNull Long userId) {
        return userRepository.getById(userId);
    }

    @NonNull
    @Override
    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    @Override
    public User updateUser(@NonNull User user) {
        return userRepository.updateUser(user);
    }

    @Override
    public void deleteUser(@NonNull Long userId) {
        userRepository.deleteUser(userId);
    }
}
