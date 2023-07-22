package ru.practicum.shareit.user.repository;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.db.UserEntity;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Repository
interface JpaUserRepository extends UserRepository, CrudRepository<UserEntity, Long> {

    @NonNull
    @Override
    default User getById(@NonNull Long userId) {
        return findById(userId)
                .map(UserMapper::map)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с идентефикатором %1$s не найден", userId)));
    }

    @NonNull
    @Override
    default User addUser(@NonNull User user) {
        try {
            return UserMapper.map(
                    save(UserMapper.mapToEntity(user, null))
            );
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException(String.format("Пользователь с email %1$s уже существует", user.getEmail()));
        }
    }

    @NonNull
    @Override
    default List<User> getAllUsers() {
        return StreamSupport.stream(findAll().spliterator(), false)
                .map(UserMapper::map)
                .collect(Collectors.toList());
    }

    @NonNull
    @Override
    @Transactional
    default User updateUser(@NonNull User user) {
        return findById(user.getId())
                .map(it -> save(UserMapper.merge(user, it)))
                .map(UserMapper::map)
                .orElseThrow();
    }

    @Override
    default void deleteUser(@NonNull Long userId) {
        deleteById(userId);
    }
}