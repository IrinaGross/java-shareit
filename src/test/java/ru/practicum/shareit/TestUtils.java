package ru.practicum.shareit;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

public class TestUtils {
    public static final Long USER_ID_1 = 1L;
    public static final Long USER_ID_2 = 2L;
    public static final Long ITEM_ID_1 = 1L;
    public static final Long ITEM_ID_2 = 2L;
    public static final long ITEM_REQUEST_ID = 1L;
    public static final Long BOOKING_ID = 1L;
    public static final Long COMMENT_ID = 1L;
    public static final Integer SIZE = 10;
    public static final Integer FROM = 0;
    public static final LocalDateTime REQUEST_TIME = LocalDateTime.now();

    public static final User USER_1 = User.builder()
            .id(USER_ID_1)
            .name("name1")
            .email("name1@email.com")
            .build();

    public static final User USER_2 = User.builder()
            .id(USER_ID_2)
            .name("name2")
            .email("name2@email.com")
            .build();

    public static final Item ITEM_1 = Item.builder()
            .id(ITEM_ID_1)
            .owner(USER_1)
            .name("name")
            .description("desc")
            .available(true)
            .build();

    public static final Item ITEM_2 = Item.builder()
            .id(ITEM_ID_2)
            .owner(USER_2)
            .name("name")
            .description("desc")
            .available(true)
            .requestId(ITEM_REQUEST_ID)
            .build();

    public static final Booking BOOKING = Booking.builder()
            .id(BOOKING_ID)
            .item(ITEM_1)
            .booker(USER_1)
            .status(BookingStatus.WAITING)
            .build();

    public static final ItemRequest ITEM_REQUEST = ItemRequest.builder()
            .id(ITEM_REQUEST_ID)
            .creator(USER_1)
            .createAt(REQUEST_TIME)
            .description("description")
            .build();

    public static final Comment COMMENT = Comment.builder()
            .id(COMMENT_ID)
            .text("ok")
            .author(USER_1)
            .createdDate(REQUEST_TIME)
            .build();
}
