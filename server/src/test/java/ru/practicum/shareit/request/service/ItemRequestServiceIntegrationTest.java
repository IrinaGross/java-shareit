package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.request.db.ItemRequestEntity;
import ru.practicum.shareit.user.service.UserService;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static ru.practicum.shareit.TestUtils.ITEM_REQUEST;
import static ru.practicum.shareit.TestUtils.USER_1;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class ItemRequestServiceIntegrationTest {
    private final EntityManager em;
    private final ItemRequestService itemRequestService;
    private final UserService userService;

    @Test
    @DirtiesContext
    void getByIdTest() {
        var user = userService.addUser(USER_1);
        var itemRequest = itemRequestService.create(user.getId(), ITEM_REQUEST);

        var entities = em.createQuery("SELECT i FROM ItemRequestEntity i", ItemRequestEntity.class)
                .getResultList();

        assertNotNull(entities);
        assertEquals(1, entities.size());
        assertEquals(itemRequest.getId(), entities.get(0).getId());
        assertEquals(itemRequest.getDescription(), entities.get(0).getDescription());
        assertEquals(itemRequest.getCreator().getId(), entities.get(0).getCreator().getId());
        assertEquals(itemRequest.getCreateAt(), entities.get(0).getDate());
    }
}