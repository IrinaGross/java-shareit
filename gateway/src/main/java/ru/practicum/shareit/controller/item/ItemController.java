package ru.practicum.shareit.controller.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.client.ItemClient;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CreateItemGroup;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.constraints.Min;

import static ru.practicum.shareit.Const.*;

@Controller
@RequiredArgsConstructor
@RequestMapping(path = "/items")
@Validated
public class ItemController {
    private final ItemClient client;

    @GetMapping
    public ResponseEntity<Object> getItems(
            @RequestHeader(X_SHARER_USER_ID_HEADER) @NonNull Long userId,
            @RequestParam(name = FROM_REQUEST_PARAM, required = false, defaultValue = DEFAULT_FROM_VALUE) @Min(0) @NonNull Integer from,
            @RequestParam(name = SIZE_REQUEST_PARAM, required = false, defaultValue = DEFAULT_SIZE_VALUE) @Min(1) @NonNull Integer size
    ) {
        return client.getItems(userId, from, size);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(
            @RequestHeader(X_SHARER_USER_ID_HEADER) @NonNull Long userId,
            @PathVariable @NonNull Long itemId
    ) {
        return client.getItem(userId, itemId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(
            @RequestParam(name = SEARCH_REQUEST_PARAM) String text,
            @RequestParam(name = FROM_REQUEST_PARAM, required = false, defaultValue = DEFAULT_FROM_VALUE) @Min(0) @NonNull Integer from,
            @RequestParam(name = SIZE_REQUEST_PARAM, required = false, defaultValue = DEFAULT_SIZE_VALUE) @Min(1) @NonNull Integer size
    ) {
        return client.searchItems(text, from, size);
    }

    @PostMapping
    public ResponseEntity<Object> addItem(
            @RequestHeader(X_SHARER_USER_ID_HEADER) @NonNull Long userId,
            @RequestBody @NonNull @Validated(CreateItemGroup.class) ItemDto item
    ) {
        return client.addItem(userId, item);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(
            @RequestHeader(X_SHARER_USER_ID_HEADER) @NonNull Long userId,
            @PathVariable("itemId") @NonNull Long itemId,
            @RequestBody @NonNull ItemDto item
    ) {
        return client.updateItem(userId, itemId, item);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Object> deleteItem(
            @RequestHeader(X_SHARER_USER_ID_HEADER) @NonNull Long userId,
            @PathVariable @NonNull Long itemId
    ) {
        return client.deleteItem(userId, itemId);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(
            @RequestHeader(X_SHARER_USER_ID_HEADER) @NonNull Long userId,
            @PathVariable @NonNull Long itemId,
            @RequestBody @Validated CommentDto comment
    ) {
        return client.createComment(userId, itemId, comment);
    }
}
