package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Utils;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CreateItemGroup;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.constraints.Min;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.Const.*;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/items")
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    public List<ItemDto> getItems(
            @RequestHeader(X_SHARER_USER_ID_HEADER) long userId,
            @RequestParam(name = FROM_REQUEST_PARAM, required = false, defaultValue = "0") @Min(0) @NonNull Integer from,
            @RequestParam(name = SIZE_REQUEST_PARAM, required = false, defaultValue = "10") @Min(1) @NonNull Integer size
    ) {
        return itemService.getItems(userId, Utils.newPage(from, size))
                .stream()
                .map(ItemMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{itemId}")
    public ItemDto getItem(
            @RequestHeader(X_SHARER_USER_ID_HEADER) long userId,
            @PathVariable @NonNull Long itemId
    ) {
        return ItemMapper.mapToDto(itemService.getItem(userId, itemId));
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(
            @RequestParam(name = SEARCH_REQUEST_PARAM) String text,
            @RequestParam(name = FROM_REQUEST_PARAM, required = false, defaultValue = "0") @Min(0) @NonNull Integer from,
            @RequestParam(name = SIZE_REQUEST_PARAM, required = false, defaultValue = "10") @Min(1) @NonNull Integer size
    ) {
        return itemService.searchBy(text, Utils.newPage(from, size))
                .stream()
                .map(ItemMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @PostMapping
    public ItemDto addItem(
            @RequestHeader(X_SHARER_USER_ID_HEADER) long userId,
            @RequestBody @NonNull @Validated(CreateItemGroup.class) ItemDto itemDto
    ) {
        Item item = ItemMapper.map(itemDto, null);
        return ItemMapper.mapToDto(itemService.addNewItem(userId, item));
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(
            @RequestHeader(X_SHARER_USER_ID_HEADER) long userId,
            @PathVariable("itemId") @NonNull Long itemId,
            @RequestBody @NonNull ItemDto itemDto
    ) {
        Item item = ItemMapper.map(itemDto, itemId);
        return ItemMapper.mapToDto(itemService.updateItem(userId, item));
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(
            @RequestHeader(X_SHARER_USER_ID_HEADER) long userId,
            @PathVariable @NonNull Long itemId
    ) {
        itemService.deleteItem(userId, itemId);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(
            @RequestHeader(X_SHARER_USER_ID_HEADER) long userId,
            @PathVariable @NonNull Long itemId,
            @RequestBody @Validated CommentDto commentDto
    ) {
        var comment = CommentMapper.map(commentDto);
        return CommentMapper.mapToDto(itemService.createComment(userId, itemId, comment));
    }
}
