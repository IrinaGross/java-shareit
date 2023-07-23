package ru.practicum.shareit.booking.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.db.BookingEntity;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.db.ItemEntity;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.user.db.UserEntity;
import ru.practicum.shareit.user.mapper.UserMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookingMapper {

    public static BookingDto mapToDto(Booking model) {
        var booker = model.getBooker();
        var item = model.getItem();
        var status = model.getStatus();
        return BookingDto.builder()
                .id(model.getId())
                .start(model.getStart())
                .end(model.getEnd())
                .status(status == null ? null : status.name())
                .booker(booker == null ? null : UserMapper.mapToDto(booker))
                .item(item == null ? null : ItemMapper.mapToDto(item))
                .build();
    }

    public static Booking map(BookingDto dto) {
        var status = dto.getStatus();
        return Booking.builder()
                .id(dto.getId())
                .start(dto.getStart())
                .end(dto.getEnd())
                .status(status == null ? null : BookingStatus.valueOf(status))
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
