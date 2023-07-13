package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Data
@Builder(toBuilder = true)
public class Item {
    private final Long id;
    private final String name;
    private final String description;
    private final User owner;
    private final Boolean available;
    private final Booking last;
    private final Booking next;
    private final List<Comment> comments;
}
