package ru.practicum.shareit.booking.mapper;

import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.db.BookingEntity;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.db.ItemEntity;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.user.db.UserEntity;
import ru.practicum.shareit.user.mapper.UserMapper;

@NoArgsConstructor
public class BookingMapper {

    public static BookingDto mapToDto(Booking model) {
        return BookingDto.builder()
                .id(model.getId())
                .start(model.getStart())
                .end(model.getEnd())
                .status(model.getStatus())
                .booker(UserMapper.mapToDto(model.getBooker()))
                .item(ItemMapper.mapToDto(model.getItem()))
                .build();
    }

    public static Booking map(BookingDto dto) {
        return Booking.builder()
                .start(dto.getStart())
                .end(dto.getEnd())
                .status(dto.getStatus())
                .build();
    }

    public static BookingEntity mapToEntity(Booking model, UserEntity user, ItemEntity item) {
        return BookingEntity.builder()
                .id(model.getId())
                .start(model.getStart())
                .end(model.getEnd())
                .status(model.getStatus().toString())
                .user(user)
                .item(item)
                .build();
    }

    public static Booking map(BookingEntity entity) {
        return Booking.builder()
                .id(entity.getId())
                .item(ItemMapper.map(entity.getItem()))
                .booker(UserMapper.map(entity.getUser()))
                .start(entity.getStart())
                .end(entity.getEnd())
                .status(BookingStatus.valueOf(entity.getStatus()))
                .build();
    }

    public static BookingEntity merge(Booking model, BookingEntity entity) {
        return BookingEntity.builder()
                .id(entity.getId())
                .start(model.getStart() == null ? entity.getStart() : model.getStart())
                .end(model.getEnd() == null ? entity.getEnd() : model.getEnd())
                .status(model.getStatus() == null ? entity.getStatus() : model.getStatus().toString())
                .item(entity.getItem())
                .user(entity.getUser())
                .build();
    }
}
