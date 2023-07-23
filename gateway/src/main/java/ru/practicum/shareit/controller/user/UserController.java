package ru.practicum.shareit.controller.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.client.UserClient;
import ru.practicum.shareit.user.dto.CreateUserGroup;
import ru.practicum.shareit.user.dto.UserDto;

@Controller
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserClient client;

    @GetMapping
    public ResponseEntity<Object> findAll() {
        return client.findAll();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUser(
            @PathVariable("userId") @NonNull Long userId
    ) {
        return client.getUser(userId);
    }

    @PostMapping
    public ResponseEntity<Object> create(
            @RequestBody @Validated(CreateUserGroup.class) @NonNull UserDto user
    ) {
        return client.create(user);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> delete(
            @PathVariable("userId") @NonNull Long userId
    ) {
        return client.deleteUser(userId);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> update(
            @PathVariable("userId") @NonNull Long userId,
            @RequestBody @NonNull UserDto user
    ) {
        return client.updateUser(userId, user);
    }
}
