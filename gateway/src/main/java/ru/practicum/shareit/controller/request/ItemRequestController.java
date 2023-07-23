package ru.practicum.shareit.controller.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.client.ItemRequestClient;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.constraints.Min;

import static ru.practicum.shareit.Const.*;

@Controller
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
@Validated
public class ItemRequestController {
    private final ItemRequestClient client;

    @PostMapping
    public ResponseEntity<Object> addNew(
            @RequestHeader(X_SHARER_USER_ID_HEADER) @NonNull Long userId,
            @RequestBody @NonNull @Validated ItemRequestDto request
    ) {
        return client.addNew(userId, request);
    }

    @GetMapping
    public ResponseEntity<Object> getAllFor(@RequestHeader(X_SHARER_USER_ID_HEADER) @NonNull Long userId) {
        return client.getAllFor(userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getById(
            @RequestHeader(X_SHARER_USER_ID_HEADER) @NonNull Long userId,
            @PathVariable @NonNull Long requestId
    ) {
        return client.getById(userId, requestId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAll(
            @RequestHeader(X_SHARER_USER_ID_HEADER) @NonNull Long userId,
            @RequestParam(name = FROM_REQUEST_PARAM, required = false, defaultValue = DEFAULT_FROM_VALUE) @Min(0) @NonNull Integer from,
            @RequestParam(name = SIZE_REQUEST_PARAM, required = false, defaultValue = DEFAULT_SIZE_VALUE) @Min(1) @NonNull Integer size
    ) {
        return client.getAll(userId, from, size);
    }
}
