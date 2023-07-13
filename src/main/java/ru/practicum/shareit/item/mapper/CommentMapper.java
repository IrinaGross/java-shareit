package ru.practicum.shareit.item.mapper;

import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.db.CommentEntity;
import ru.practicum.shareit.item.db.ItemEntity;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.user.db.UserEntity;
import ru.practicum.shareit.user.mapper.UserMapper;

import java.time.LocalDateTime;

@NoArgsConstructor
public class CommentMapper {

    public static Comment map(CommentDto dto) {
        return Comment.builder()
                .id(dto.getId())
                .text(dto.getText())
                .build();
    }

    public static CommentDto mapToDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorName(comment.getAuthor().getName())
                .created(comment.getCreatedDate())
                .build();
    }

    public static Comment map(CommentEntity entity) {
        return Comment.builder()
                .text(entity.getText())
                .id(entity.getId())
                .createdDate(entity.getCreatedAt())
                .author(UserMapper.map(entity.getAuthor()))
                .build();
    }

    public static CommentEntity mapToEntity(Comment comment, ItemEntity itemEntity, UserEntity userEntity) {
        return CommentEntity.builder()
                .item(itemEntity)
                .author(userEntity)
                .createdAt(LocalDateTime.now())
                .text(comment.getText())
                .build();
    }
}
