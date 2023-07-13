package ru.practicum.shareit.booking.model;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
public class Booking {
    private final Long id;
//    private final Long itemId;
    private final Item item;
//    private final String itemName;
    private final User booker;
    private final LocalDateTime start;
    private final LocalDateTime end;
    private final BookingStatus status;
}
