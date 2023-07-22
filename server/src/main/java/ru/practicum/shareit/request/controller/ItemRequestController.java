package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Utils;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.constraints.Min;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.Const.*;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService service;

    @PostMapping
    public ItemRequestDto addNew(
            @RequestHeader(X_SHARER_USER_ID_HEADER) @NonNull Long userId,
            @RequestBody @NonNull @Validated ItemRequestDto request
    ) {
        return ItemRequestMapper.mapToDto(
                service.create(userId, ItemRequestMapper.map(request))
        );
    }

    @GetMapping
    public List<ItemRequestDto> getAllFor(@RequestHeader(X_SHARER_USER_ID_HEADER) @NonNull Long userId) {
        return service.getAll(userId)
                .stream()
                .map(ItemRequestMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getById(
            @RequestHeader(X_SHARER_USER_ID_HEADER) @NonNull Long userId,
            @PathVariable @NonNull Long requestId
    ) {
        return ItemRequestMapper.mapToDto(
                service.getById(userId, requestId)
        );
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAll(
            @RequestHeader(X_SHARER_USER_ID_HEADER) @NonNull Long userId,
            @RequestParam(name = FROM_REQUEST_PARAM, required = false, defaultValue = DEFAULT_FROM_VALUE) @Min(0) @NonNull Integer from,
            @RequestParam(name = SIZE_REQUEST_PARAM, required = false, defaultValue = DEFAULT_SIZE_VALUE) @Min(1) @NonNull Integer size
    ) {
        return service.getAll(userId, Utils.newPage(from, size))
                .stream()
                .map(ItemRequestMapper::mapToDto)
                .collect(Collectors.toList());
    }
}
