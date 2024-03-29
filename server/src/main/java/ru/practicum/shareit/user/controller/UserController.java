package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserService service;

    @PostMapping
    public UserDto create(@RequestBody UserDto userDto) {
        User user = UserMapper.map(userDto, null);
        return UserMapper.mapToDto(service.addUser(user));
    }

    @GetMapping("/{userId}")
    public UserDto getUser(@PathVariable("userId") Long userId) {
        return UserMapper.mapToDto(service.getUser(userId));
    }

    @GetMapping
    public List<UserDto> findAll() {
        return service.getAllUsers().stream()
                .map(UserMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @PatchMapping("/{userId}")
    public UserDto update(
            @PathVariable("userId") Long userId,
            @RequestBody UserDto userDto
    ) {
        User user = UserMapper.map(userDto, userId);
        return UserMapper.mapToDto(service.updateUser(user));
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable("userId") Long userId) {
        service.deleteUser(userId);
    }
}
