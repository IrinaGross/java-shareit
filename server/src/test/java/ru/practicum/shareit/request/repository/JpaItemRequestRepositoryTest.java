package ru.practicum.shareit.request.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.user.mapper.UserMapper;

import javax.persistence.EntityManager;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static ru.practicum.shareit.TestUtils.*;

@DataJpaTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class JpaItemRequestRepositoryTest {
    private final JpaItemRequestRepository repository;
    private final EntityManager em;

    @BeforeEach
    void fill() {
        var userEntity = UserMapper.mapToEntity(USER_1, null);
        em.persist(userEntity);

        var requestEntity = ItemRequestMapper.mapToEntity(ITEM_REQUEST);
        repository.save(Objects.requireNonNull(requestEntity));
    }

    @Test
    @DirtiesContext
    void findByCreatorIdOrderByDateAscWithExistIdShouldReturnNonEmptyList() {
        var list = repository.findByCreatorIdOrderByDateAsc(USER_ID_1);
        assertNotNull(list);
        assertEquals(1, list.size());
    }

    @Test
    @DirtiesContext
    void findByCreatorIdOrderByDateAscWithNonExistIdShouldReturnEmptyList() {
        var list = repository.findByCreatorIdOrderByDateAsc(USER_ID_2);
        assertNotNull(list);
        assertEquals(0, list.size());
    }

    @Test
    @DirtiesContext
    void findByCreatorIdNotOrderByDateAscWithExistIdShouldReturnNonEmptyList() {
        var list = repository.findByCreatorIdOrderByDateAsc(USER_ID_1);
        assertNotNull(list);
        assertEquals(1, list.size());
    }

    @Test
    @DirtiesContext
    void findByCreatorIdNotOrderByDateAscWithNonExistIdShouldReturnEmptyList() {
        var list = repository.findByCreatorIdOrderByDateAsc(USER_ID_2);
        assertNotNull(list);
        assertEquals(0, list.size());
    }
}