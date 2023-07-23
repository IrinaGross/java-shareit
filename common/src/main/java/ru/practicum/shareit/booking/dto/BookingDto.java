package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
@Jacksonized
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookingDto {
    private final Long id;

    @NotNull
    private final Long itemId;

    @NotNull
    @FutureOrPresent
    private final LocalDateTime start;

    @NotNull
    @FutureOrPresent
    private final LocalDateTime end;

    private final String status;
    private final UserDto booker;
    private final ItemDto item;
}