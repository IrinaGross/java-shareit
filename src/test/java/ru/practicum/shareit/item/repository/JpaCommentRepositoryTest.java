package ru.practicum.shareit.item.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.db.CommentEntity;
import ru.practicum.shareit.item.db.ItemEntity;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.user.db.UserEntity;
import ru.practicum.shareit.user.mapper.UserMapper;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static ru.practicum.shareit.TestUtils.*;

@DataJpaTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class JpaCommentRepositoryTest {
    private final JpaCommentRepository repository;
    private final EntityManager em;

    @BeforeEach
    void fill() {
        UserEntity userEntity = UserMapper.mapToEntity(USER_1, null);
        em.persist(userEntity);
        ItemEntity item = ItemMapper.mapToEntity(ITEM_1, null, userEntity, null);
        em.persist(item);

        var comment = CommentEntity.builder()
                .text("text")
                .createdAt(REQUEST_TIME)
                .author(userEntity)
                .item(item)
                .build();
        repository.save(comment);
    }

    @Test
    @DirtiesContext
    void findAllByItemIdCorrectIdShouldReturnList() {
        var list = repository.findAllByItemId(USER_ID_1);
        assertNotNull(list);
        assertEquals(1, list.size());
    }

    @Test
    @DirtiesContext
    void findAllByItemIdWhenNotFoundReturnEmptyList() {
        var list = repository.findAllByItemId(USER_ID_2);
        assertNotNull(list);
        assertEquals(0, list.size());
    }
}