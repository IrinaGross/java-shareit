package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
public class Comment {
    private final Long id;
    private final String text;
    private final User author;
    private final LocalDateTime createdDate;
}
