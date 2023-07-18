package ru.practicum.shareit.booking.repository;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.Utils;
import ru.practicum.shareit.booking.db.BookingEntity;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.user.mapper.UserMapper;

import javax.persistence.EntityManager;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.practicum.shareit.TestUtils.*;

@DataJpaTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class JpaBookingRepositoryTest {
    private final JpaBookingRepository repository;
    private final EntityManager em;

    @BeforeEach
    void fill() {
        var userEntity = UserMapper.mapToEntity(USER_1, null);
        em.persist(userEntity);
        var itemEntity = ItemMapper.mapToEntity(ITEM_1, null, userEntity, null);
        em.persist(itemEntity);

        var bookingEntity = BookingMapper.mapToEntity(BOOKING, userEntity, itemEntity);
        repository.save(bookingEntity);
    }

    @Test
    @DirtiesContext
    void getItemWithCorrectIdShouldReturnItem() {
        var item = repository.getItem(BOOKING_ID);
        assertEquals(BOOKING_ID, item.getId());
    }

    @Test
    @DirtiesContext
    void getItemWithWrongIdShouldReturnItem() {
        assertThrows(NotFoundException.class, () -> repository.getItem(-1L));
    }

    @Test
    @DirtiesContext
    void updateWithCorrectArgumentsShouldReturnItem() {
        val items = getItems();
        assertEquals(1, items.size());

        var booking = BookingMapper.map(items.get(0))
                .toBuilder()
                .status(BookingStatus.APPROVED)
                .build();

        var update = repository.update(booking);

        assertEquals(booking.getStatus(), update.getStatus());
    }

    @Test
    @DirtiesContext
    void findAllByUserIdForWaitingStateShouldReturnList() {
        var list = repository.findAllByUserId(USER_ID_1, BookingState.WAITING, Utils.newPage(FROM, SIZE));
        assertEquals(1, list.size());
    }

    @Test
    @DirtiesContext
    void findAllByUserIdForCurrentStateShouldReturnEmptyList() {
        var list = repository.findAllByUserId(USER_ID_1, BookingState.CURRENT, Utils.newPage(FROM, SIZE));
        assertEquals(0, list.size());
    }

    @Test
    @DirtiesContext
    void findAllByUserIdForPastStateShouldReturnEmptyList() {
        var list = repository.findAllByUserId(USER_ID_1, BookingState.PAST, Utils.newPage(FROM, SIZE));
        assertEquals(0, list.size());
    }

    @Test
    @DirtiesContext
    void findAllByUserIdForFutureStateShouldReturnList() {
        var list = repository.findAllByUserId(USER_ID_1, BookingState.FUTURE, Utils.newPage(FROM, SIZE));
        assertEquals(1, list.size());
    }

    @Test
    @DirtiesContext
    void findAllByUserIdForRejectedStateShouldReturnEmptyList() {
        var list = repository.findAllByUserId(USER_ID_1, BookingState.REJECTED, Utils.newPage(FROM, SIZE));
        assertEquals(0, list.size());
    }

    @Test
    @DirtiesContext
    void findAllByUserIdForAllStateShouldReturnEmptyList() {
        var list = repository.findAllByUserId(USER_ID_1, BookingState.ALL, Utils.newPage(FROM, SIZE));
        assertEquals(1, list.size());
    }

    @Test
    @DirtiesContext
    void findAllByItemUserIdForWaitingStateShouldReturnList() {
        var list = repository.findAllByItemUserId(USER_ID_1, BookingState.WAITING, Utils.newPage(FROM, SIZE));
        assertEquals(1, list.size());
    }

    @Test
    @DirtiesContext
    void findAllByItemUserIdForCurrentStateShouldReturnEmptyList() {
        var list = repository.findAllByItemUserId(USER_ID_1, BookingState.CURRENT, Utils.newPage(FROM, SIZE));
        assertEquals(0, list.size());
    }

    @Test
    @DirtiesContext
    void findAllByItemUserIdForPastStateShouldReturnEmptyList() {
        var list = repository.findAllByItemUserId(USER_ID_1, BookingState.PAST, Utils.newPage(FROM, SIZE));
        assertEquals(0, list.size());
    }

    @Test
    @DirtiesContext
    void findAllByItemUserIdForFutureStateShouldReturnList() {
        var list = repository.findAllByItemUserId(USER_ID_1, BookingState.FUTURE, Utils.newPage(FROM, SIZE));
        assertEquals(1, list.size());
    }

    @Test
    @DirtiesContext
    void findAllByItemUserIdForRejectedStateShouldReturnEmptyList() {
        var list = repository.findAllByItemUserId(USER_ID_1, BookingState.REJECTED, Utils.newPage(FROM, SIZE));
        assertEquals(0, list.size());
    }

    @Test
    @DirtiesContext
    void findAllByItemUserIdForAllStateShouldReturnEmptyList() {
        var list = repository.findAllByItemUserId(USER_ID_1, BookingState.ALL, Utils.newPage(FROM, SIZE));
        assertEquals(1, list.size());
    }


    private List<BookingEntity> getItems() {
        return em.createQuery("SELECT b FROM BookingEntity b", BookingEntity.class)
                .getResultList();
    }
}