package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.db.BookingEntity;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.service.ItemService;
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
class BookingServiceIntegrationTest {
    private final EntityManager em;
    private final BookingService bookingService;
    private final ItemService itemService;
    private final UserService userService;
    private final ItemRequestService itemRequestService;

    @Test
    @DirtiesContext
    void newRequestTest() {
        var user1 = userService.addUser(USER_1);
        var user2 = userService.addUser(USER_2);
        itemRequestService.create(user1.getId(), ITEM_REQUEST);
        var item = itemService.addNewItem(user1.getId(), ITEM_1);

        var booking = Booking.builder()
                .start(REQUEST_TIME.plusDays(1))
                .end(REQUEST_TIME.plusDays(2))
                .build();
        booking = bookingService.newRequest(user2.getId(), item.getId(), booking);

        var entity = em.createQuery("SELECT b FROM BookingEntity b WHERE b.id = :id", BookingEntity.class)
                .setParameter("id", booking.getId())
                .getSingleResult();

        assertNotNull(entity);
        assertNotNull(entity.getItem());
        assertEquals(item.getId(), entity.getItem().getId());
        assertNotNull(entity.getUser());
        assertEquals(user2.getId(), entity.getUser().getId());
        assertEquals(BookingStatus.WAITING, BookingStatus.valueOf(entity.getStatus()));
        assertEquals(REQUEST_TIME.plusDays(1), entity.getStart());
        assertEquals(REQUEST_TIME.plusDays(2), entity.getEnd());
    }
}