package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NoValidItemDto;
import ru.practicum.shareit.item.dto.ValidItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/items")
public class ItemController {
    public static final String X_SHARER_USER_ID_HEADER = "X-Sharer-User-Id";
    private final ItemService itemService;

    @GetMapping
    public List<ItemDto> getItems(@RequestHeader(X_SHARER_USER_ID_HEADER) @NonNull Long userId) {
        return itemService.getItems(userId)
                .stream()
                .map(ItemMapper::map)
                .collect(Collectors.toList());
    }

    @GetMapping("/{itemId}")
    public ItemDto getItem(@PathVariable @NonNull Long itemId) {
        return ItemMapper.map(itemService.getItem(itemId));
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam(value = "text") String text) {
        return itemService.searchBy(text)
                .stream()
                .map(ItemMapper::map)
                .collect(Collectors.toList());
    }

    @PostMapping
    public ItemDto addItem(
            @RequestHeader(X_SHARER_USER_ID_HEADER) @NonNull Long userId,
            @RequestBody @NonNull @Valid ValidItemDto itemDto
    ) {
        Item item = ItemMapper.map(itemDto, null);
        return ItemMapper.map(itemService.addNewItem(userId, item));
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(
            @RequestHeader(X_SHARER_USER_ID_HEADER) @NonNull Long userId,
            @PathVariable("itemId") @NonNull Long itemId,
            @RequestBody @NonNull NoValidItemDto itemDto
    ) {
        Item item = ItemMapper.map(itemDto, itemId);
        return ItemMapper.map(itemService.updateItem(userId, item));
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(
            @RequestHeader(X_SHARER_USER_ID_HEADER) @NonNull Long userId,
            @PathVariable @NonNull Long itemId
    ) {
        itemService.deleteItem(userId, itemId);
    }
}
