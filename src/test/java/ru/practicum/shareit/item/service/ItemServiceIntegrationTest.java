package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.Utils;
import ru.practicum.shareit.item.db.ItemEntity;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.service.UserService;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static ru.practicum.shareit.TestUtils.*;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class ItemServiceIntegrationTest {
    private final EntityManager em;
    private final ItemService itemService;
    private final UserService userService;
    private final ItemRequestService itemRequestService;

    @Test
    @DirtiesContext
    void getItemsTest() {
        var user = userService.addUser(USER_1);
        itemRequestService.create(user.getId(), ITEM_REQUEST);
        itemService.addNewItem(user.getId(), ITEM_1);
        itemService.addNewItem(user.getId(), ITEM_2);

        var items = itemService.getItems(user.getId(), Utils.newPage(FROM, SIZE));

        var entities = em.createQuery("SELECT i FROM ItemEntity i", ItemEntity.class)
                .getResultList();

        assertNotNull(items);
        assertEquals(2, items.size());
        assertNotNull(items.get(0).getId());
        assertNotNull(items.get(1).getId());
        assertNotNull(entities);
        assertEquals(2, entities.size());
        assertEquals(items.get(0).getId(), entities.get(0).getId());
        assertEquals(items.get(1).getId(), entities.get(1).getId());
    }
}