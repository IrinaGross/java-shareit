package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.user.db.UserEntity;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static ru.practicum.shareit.TestUtils.USER_1;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class UserServiceIntegrationTest {
    private final EntityManager em;
    private final UserService userService;

    @Test
    @DirtiesContext
    void updateUserTest() {
        var user = userService.addUser(USER_1);
        var newName = "new user name";
        var updatedUser = userService.updateUser(
                user.toBuilder()
                        .name(newName)
                        .build()
        );

        var entities = em.createQuery("SELECT u FROM UserEntity u", UserEntity.class)
                .getResultList();

        assertNotNull(entities);
        assertEquals(1, entities.size());
        assertEquals(updatedUser.getId(), entities.get(0).getId());
        assertEquals(updatedUser.getName(), entities.get(0).getName());
        assertNotEquals(updatedUser.getName(), user.getName());
        assertEquals(updatedUser.getEmail(), entities.get(0).getEmail());
    }
}