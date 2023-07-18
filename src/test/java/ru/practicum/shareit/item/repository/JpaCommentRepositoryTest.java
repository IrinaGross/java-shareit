package ru.practicum.shareit.item.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.db.CommentEntity;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
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
        var userEntity = UserMapper.mapToEntity(USER_1, null);
        em.persist(userEntity);
        var itemEntity = ItemMapper.mapToEntity(ITEM_1, null, userEntity, null);
        em.persist(itemEntity);

        var comment = CommentEntity.builder()
                .text("text")
                .createdAt(REQUEST_TIME)
                .author(userEntity)
                .item(itemEntity)
                .build();
        repository.save(comment);
    }

    @Test
    @DirtiesContext
    void createWithCorrectArgumentsShouldReturnItem() {
        var userEntity = UserMapper.mapToEntity(USER_2, null);
        em.persist(userEntity);
        var itemEntity = ItemMapper.mapToEntity(ITEM_2, null, userEntity, null);
        em.persist(itemEntity);
        var commentEntity = CommentMapper.mapToEntity(COMMENT, itemEntity, userEntity);
        em.persist(commentEntity);

        var comment = repository.create(ITEM_1, USER_1, COMMENT, ITEM_REQUEST);
        assertNotNull(comment);
        assertEquals(3L, comment.getId());
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