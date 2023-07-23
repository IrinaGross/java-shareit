package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.Utils;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static ru.practicum.shareit.TestUtils.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private ItemRequestRepository itemRequestRepository;

    @InjectMocks
    private BookingServiceImpl service;

    @Test
    void newRequestWithCorrectArgumentsShouldReturnItem() {
        var booking = Booking.builder()
                .start(REQUEST_TIME.plusDays(1))
                .end(REQUEST_TIME.plusDays(2))
                .build();
        when(itemRepository.getItem(anyLong())).thenReturn(ITEM_2);
        when(userRepository.getById(anyLong())).thenReturn(USER_1);
        when(itemRequestRepository.getById(anyLong())).thenReturn(ITEM_REQUEST);
        when(bookingRepository.create(any(), any(), any(), any())).thenReturn(BOOKING);

        var result = service.newRequest(USER_ID_1, ITEM_ID_1, booking);

        assertNotNull(result);
        assertEquals(BOOKING.getId(), result.getId());
        verify(itemRepository, times(1)).getItem(ITEM_ID_1);
        verify(itemRequestRepository, times(1)).getById(ITEM_REQUEST_ID);
        verify(userRepository, times(1)).getById(USER_ID_1);
        verify(bookingRepository, times(1)).create(booking, USER_1, ITEM_2, ITEM_REQUEST);
    }

    @Test
    void newRequestWithWrongItemIdShouldThrowNotFoundException() {
        var booking = Booking.builder()
                .start(REQUEST_TIME.plusDays(1))
                .end(REQUEST_TIME.plusDays(2))
                .build();

        when(itemRepository.getItem(anyLong())).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> service.newRequest(USER_ID_1, ITEM_ID_1, booking));
        verify(itemRepository, times(1)).getItem(ITEM_ID_1);
        verify(itemRequestRepository, never()).getById(anyLong());
        verify(userRepository, never()).getById(anyLong());
        verify(bookingRepository, never()).create(any(), any(), any(), any());
    }

    @Test
    void newRequestWithWrongItemRequestIdShouldThrowNotFoundException() {
        var booking = Booking.builder()
                .start(REQUEST_TIME.plusDays(1))
                .end(REQUEST_TIME.plusDays(2))
                .build();

        when(itemRepository.getItem(anyLong())).thenReturn(ITEM_2);
        when(itemRequestRepository.getById(anyLong())).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> service.newRequest(USER_ID_1, ITEM_ID_1, booking));
        verify(itemRepository, times(1)).getItem(ITEM_ID_1);
        verify(itemRequestRepository, times(1)).getById(ITEM_REQUEST_ID);
        verify(userRepository, never()).getById(anyLong());
        verify(bookingRepository, never()).create(any(), any(), any(), any());
    }

    @Test
    void newRequestWithWrongUserIdShouldThrowNotFoundException() {
        var booking = Booking.builder()
                .start(REQUEST_TIME.plusDays(1))
                .end(REQUEST_TIME.plusDays(2))
                .build();

        when(itemRepository.getItem(anyLong())).thenReturn(ITEM_2);
        when(itemRequestRepository.getById(anyLong())).thenReturn(ITEM_REQUEST);
        when(userRepository.getById(anyLong())).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> service.newRequest(USER_ID_1, ITEM_ID_1, booking));
        verify(itemRepository, times(1)).getItem(ITEM_ID_1);
        verify(itemRequestRepository, times(1)).getById(anyLong());
        verify(userRepository, times(1)).getById(USER_ID_1);
        verify(bookingRepository, never()).create(any(), any(), any(), any());
    }

    @Test
    void newRequestWithStartEqualsEndThenThrowBadRequestException() {
        var model = Booking.builder()
                .start(REQUEST_TIME)
                .end(REQUEST_TIME)
                .build();

        assertThrows(BadRequestException.class, () -> service.newRequest(USER_ID_1, ITEM_ID_1, model));
        verify(itemRepository, never()).getItem(anyLong());
        verify(itemRequestRepository, never()).getById(anyLong());
        verify(userRepository, never()).getById(anyLong());
        verify(bookingRepository, never()).create(any(), any(), any(), any());
    }

    @Test
    void newRequestWithStartLessThatEndThenThrowBadRequestException() {
        var model = Booking.builder()
                .start(REQUEST_TIME)
                .end(REQUEST_TIME.minusHours(5))
                .build();

        assertThrows(BadRequestException.class, () -> service.newRequest(USER_ID_1, ITEM_ID_1, model));
        verify(itemRepository, never()).getItem(anyLong());
        verify(itemRequestRepository, never()).getById(anyLong());
        verify(userRepository, never()).getById(anyLong());
        verify(bookingRepository, never()).create(any(), any(), any(), any());
    }

    @Test
    void newRequestWithOwnerIdEqualsUserIdThenThrowNotFoundException() {
        var model = Booking.builder()
                .start(REQUEST_TIME.plusDays(1))
                .end(REQUEST_TIME.plusDays(2))
                .build();

        when(itemRepository.getItem(anyLong())).thenReturn(ITEM_1);

        assertThrows(NotFoundException.class, () -> service.newRequest(USER_ID_1, ITEM_ID_1, model));
        verify(itemRepository, times(1)).getItem(ITEM_ID_1);
        verify(itemRequestRepository, never()).getById(anyLong());
        verify(userRepository, never()).getById(anyLong());
        verify(bookingRepository, never()).create(any(), any(), any(), any());
    }

    @Test
    void newRequestWithItemIsNotAvailableThenThrowBadRequestException() {
        var model = Booking.builder()
                .start(REQUEST_TIME.plusDays(1))
                .end(REQUEST_TIME.plusDays(2))
                .build();
        var item = ITEM_2.toBuilder()
                .available(false)
                .build();

        when(itemRepository.getItem(anyLong())).thenReturn(item);

        assertThrows(BadRequestException.class, () -> service.newRequest(USER_ID_1, ITEM_ID_1, model));
        verify(itemRepository, times(1)).getItem(ITEM_ID_1);
        verify(itemRequestRepository, never()).getById(anyLong());
        verify(userRepository, never()).getById(anyLong());
        verify(bookingRepository, never()).create(any(), any(), any(), any());
    }

    @Test
    void changeRequestStatusWithCorrectArgumentsShouldReturnItem() {
        var approvedBooking = BOOKING.toBuilder()
                .status(BookingStatus.APPROVED)
                .build();

        when(userRepository.getById(anyLong())).thenReturn(USER_1);
        when(bookingRepository.getItem(anyLong())).thenReturn(BOOKING);
        when(itemRepository.getItem(anyLong())).thenReturn(ITEM_1);
        when(bookingRepository.update(any())).thenReturn(approvedBooking);

        var result = service.changeRequestStatus(USER_ID_1, BOOKING_ID, BookingStatus.APPROVED);

        assertNotNull(result);
        assertEquals(BookingStatus.APPROVED, result.getStatus());
        verify(userRepository, times(1)).getById(USER_ID_1);
        verify(bookingRepository, times(1)).getItem(BOOKING_ID);
        verify(itemRepository, times(1)).getItem(ITEM_ID_1);
        verify(bookingRepository, times(1)).update(approvedBooking);
    }

    @Test
    void changeRequestStatusWithWrongUserIdShouldThrowNotFoundException() {
        when(userRepository.getById(anyLong())).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> service.changeRequestStatus(USER_ID_1, BOOKING_ID, BookingStatus.APPROVED));
        verify(userRepository, times(1)).getById(USER_ID_1);
        verify(bookingRepository, never()).getItem(anyLong());
        verify(itemRepository, never()).getItem(anyLong());
        verify(bookingRepository, never()).update(any());
    }

    @Test
    void changeRequestStatusWithWrongBookingIdShouldThrowNotFoundException() {
        when(userRepository.getById(anyLong())).thenReturn(USER_1);
        when(bookingRepository.getItem(anyLong())).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> service.changeRequestStatus(USER_ID_1, BOOKING_ID, BookingStatus.APPROVED));
        verify(userRepository, times(1)).getById(USER_ID_1);
        verify(bookingRepository, times(1)).getItem(BOOKING_ID);
        verify(itemRepository, never()).getItem(anyLong());
        verify(bookingRepository, never()).update(any());
    }

    @Test
    void changeRequestStatusWhenOwnerIdEqualsUserIdThenThrowNotFoundException() {
        var item = ITEM_1.toBuilder()
                .owner(USER_2)
                .build();
        var booking = BOOKING.toBuilder()
                .item(item)
                .build();

        when(itemRepository.getItem(anyLong())).thenReturn(item);
        when(userRepository.getById(anyLong())).thenReturn(USER_2);
        when(bookingRepository.getItem(anyLong())).thenReturn(booking);

        assertThrows(NotFoundException.class, () -> service.changeRequestStatus(USER_ID_1, BOOKING_ID, BookingStatus.APPROVED));
        verify(userRepository, times(1)).getById(USER_ID_1);
        verify(bookingRepository, times(1)).getItem(BOOKING_ID);
        verify(itemRepository, times(1)).getItem(ITEM_ID_1);
        verify(bookingRepository, never()).update(any());
    }

    @Test
    void changeRequestStatusWhenCurrentStatusIsNotWaitingThenThrowBadRequestException() {
        var approvedBooking = BOOKING.toBuilder()
                .status(BookingStatus.APPROVED)
                .build();

        when(itemRepository.getItem(anyLong())).thenReturn(ITEM_1);
        when(userRepository.getById(anyLong())).thenReturn(USER_1);
        when(bookingRepository.getItem(anyLong())).thenReturn(approvedBooking);

        assertThrows(BadRequestException.class, () -> service.changeRequestStatus(USER_ID_1, BOOKING_ID, BookingStatus.APPROVED));
        verify(userRepository, times(1)).getById(USER_ID_1);
        verify(bookingRepository, times(1)).getItem(BOOKING_ID);
        verify(itemRepository, times(1)).getItem(ITEM_ID_1);
        verify(bookingRepository, never()).update(any());
    }

    @Test
    void getRequestByIdWithCorrectArgumentsShouldReturnItem() {
        when(userRepository.getById(anyLong())).thenReturn(USER_1);
        when(bookingRepository.getItem(anyLong())).thenReturn(BOOKING);
        when(itemRepository.getItem(anyLong())).thenReturn(ITEM_1);

        var booking = service.getRequestById(USER_ID_1, BOOKING_ID);

        assertNotNull(booking);
        assertEquals(BOOKING_ID, booking.getId());
        verify(userRepository, times(1)).getById(USER_ID_1);
        verify(bookingRepository, times(1)).getItem(BOOKING_ID);
        verify(itemRepository, times(1)).getItem(ITEM_ID_1);
    }

    @Test
    void getRequestByIdWithWrongUserIdShouldThrowNotFoundException() {
        when(userRepository.getById(anyLong())).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> service.getRequestById(USER_ID_1, BOOKING_ID));
        verify(userRepository, times(1)).getById(USER_ID_1);
        verify(bookingRepository, never()).getItem(anyLong());
        verify(itemRepository, never()).getItem(anyLong());
    }

    @Test
    void getRequestByIdWithWrongBookingIdShouldThrowNotFoundException() {
        when(userRepository.getById(anyLong())).thenReturn(USER_1);
        when(bookingRepository.getItem(anyLong())).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> service.getRequestById(USER_ID_1, BOOKING_ID));
        verify(userRepository, times(1)).getById(USER_ID_1);
        verify(bookingRepository, times(1)).getItem(anyLong());
        verify(itemRepository, never()).getItem(anyLong());
    }

    @Test
    void getRequestByIdForDifferentUserShouldThrowNotFoundException() {
        var booking = BOOKING.toBuilder()
                .item(ITEM_2)
                .booker(USER_2)
                .build();
        when(userRepository.getById(anyLong())).thenReturn(USER_1);
        when(bookingRepository.getItem(anyLong())).thenReturn(booking);
        when(itemRepository.getItem(anyLong())).thenReturn(ITEM_2);

        assertThrows(NotFoundException.class, () -> service.getRequestById(USER_ID_1, BOOKING_ID));
        verify(userRepository, times(1)).getById(USER_ID_1);
        verify(bookingRepository, times(1)).getItem(BOOKING_ID);
        verify(itemRepository, times(1)).getItem(ITEM_ID_2);
    }

    @Test
    void getAllRequestsWithCorrectArgumentsShouldReturnItems() {
        when(userRepository.getById(anyLong())).thenReturn(USER_1);
        when(bookingRepository.findAllByUserId(anyLong(), any(), any())).thenReturn(Collections.emptyList());

        var list = service.getAllRequests(USER_ID_1, BookingState.ALL, Utils.newPage(FROM, SIZE));
        assertNotNull(list);
        assertEquals(0, list.size());
        verify(userRepository, times(1)).getById(USER_ID_1);
        verify(bookingRepository, times(1)).findAllByUserId(USER_ID_1, BookingState.ALL, Utils.newPage(FROM, SIZE));
    }

    @Test
    void getAllRequestsWithWrongUserIdShouldThrowNotFoundException() {
        when(userRepository.getById(anyLong())).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> service.getAllRequests(USER_ID_2, BookingState.ALL, Utils.newPage(FROM, SIZE)));
        verify(userRepository, times(1)).getById(USER_ID_2);
        verify(bookingRepository, never()).findAllByUserId(anyLong(), any(), any());
    }

    @Test
    void getAllRequestsForOwnerWithCorrectArgumentsShouldReturnItems() {
        when(userRepository.getById(anyLong())).thenReturn(USER_1);
        when(bookingRepository.findAllByItemUserId(anyLong(), any(), any())).thenReturn(Collections.emptyList());

        var list = service.getAllRequestsForOwner(USER_ID_1, BookingState.ALL, Utils.newPage(FROM, SIZE));
        assertNotNull(list);
        assertEquals(0, list.size());
        verify(userRepository, times(1)).getById(USER_ID_1);
        verify(bookingRepository, times(1)).findAllByItemUserId(USER_ID_1, BookingState.ALL, Utils.newPage(FROM, SIZE));
    }

    @Test
    void getAllRequestsForOwnerWithWrongUserIdShouldThrowNotFoundException() {
        when(userRepository.getById(anyLong())).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> service.getAllRequestsForOwner(USER_ID_2, BookingState.ALL, Utils.newPage(FROM, SIZE)));
        verify(userRepository, times(1)).getById(USER_ID_2);
        verify(bookingRepository, never()).findAllByItemUserId(anyLong(), any(), any());
    }
}