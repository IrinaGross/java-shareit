package ru.practicum.shareit.item.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.Utils;
import ru.practicum.shareit.item.db.ItemEntity;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.user.mapper.UserMapper;

import javax.persistence.EntityManager;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static ru.practicum.shareit.TestUtils.*;

@DataJpaTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class JpaItemRepositoryTest {
    private final JpaItemRepository repository;
    private final EntityManager em;

    @BeforeEach
    void fill() {
        var userEntity = UserMapper.mapToEntity(USER_1, null);
        em.persist(userEntity);

        repository.save(ItemMapper.mapToEntity(ITEM_1, null, userEntity, null));
        repository.save(ItemMapper.mapToEntity(ITEM_1, null, userEntity, null));
        repository.save(ItemMapper.mapToEntity(ITEM_1, null, userEntity, null));
    }

    @Test
    @DirtiesContext
    void findAllByUserIdWithExistIdShouldReturnNonEmptyList() {
        var list = repository.findAllByUserId(USER_ID_1, Utils.newPage(FROM, SIZE));
        assertNotNull(list);
        assertEquals(3, list.size());
    }

    @Test
    @DirtiesContext
    void findAllByUserIdWithNonExistIdShouldReturnEmptyList() {
        var list = repository.findAllByUserId(USER_ID_2, Utils.newPage(FROM, SIZE));
        assertNotNull(list);
        assertEquals(0, list.size());
    }

    @Test
    @DirtiesContext
    void findByIdAndUserIdWithCorrectArgumentsShouldReturnNonEmptyOptional() {
        Optional<ItemEntity> entity = repository.findByIdAndUserId(ITEM_ID_1, USER_ID_1);
        assertFalse(entity.isEmpty());

        ItemEntity itemEntity = entity.get();

        assertEquals(itemEntity.getId(), ITEM_ID_1);
        assertEquals(itemEntity.getUser().getId(), USER_ID_1);
    }

    @Test
    @DirtiesContext
    void findByIdAndUserIdWithWrongItemIdShouldReturnEmptyOptional() {
        Optional<ItemEntity> entity = repository.findByIdAndUserId(999L, USER_ID_1);
        assertTrue(entity.isEmpty());
    }

    @Test
    @DirtiesContext
    void findByIdAndUserIdWithWrongUserIdShouldReturnEmptyOptional() {
        Optional<ItemEntity> entity = repository.findByIdAndUserId(ITEM_ID_1, USER_ID_2);
        assertTrue(entity.isEmpty());
    }

    @Test
    @DirtiesContext
    void findByIdAndUserIdWithWrongUserIdAndUserIdShouldReturnEmptyOptional() {
        Optional<ItemEntity> entity = repository.findByIdAndUserId(ITEM_ID_2, USER_ID_2);
        assertTrue(entity.isEmpty());
    }

    @Test
    @DirtiesContext
    void findAllByQueryWithEmptyQueryReturnEmptyList() {
        var list = repository.searchBy("", Utils.newPage(FROM, SIZE));
        assertNotNull(list);
        assertEquals(0, list.size());
    }

    @Test
    @DirtiesContext
    void findAllByQueryWithQueryEqualsNameReturnNonEmptyList() {
        var list = repository.searchBy("name", Utils.newPage(FROM, SIZE));
        assertNotNull(list);
        assertEquals(3, list.size());
    }

    @Test
    @DirtiesContext
    void findAllByQueryWithQueryEqualsNaMEReturnNonEmptyList() {
        var list = repository.searchBy("naME", Utils.newPage(FROM, SIZE));
        assertNotNull(list);
        assertEquals(3, list.size());
    }
}