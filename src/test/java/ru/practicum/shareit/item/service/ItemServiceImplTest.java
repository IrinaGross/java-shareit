package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.Utils;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.repository.CommentRepository;
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
class ItemServiceImplTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private ItemRequestRepository itemRequestRepository;

    @InjectMocks
    private ItemServiceImpl service;

    @Test
    void getItemsWithCorrectArgumentsShouldReturnItems() {
        when(userRepository.getById(anyLong())).thenReturn(USER_1);
        when(itemRepository.getItems(anyLong(), any())).thenReturn(Collections.emptyList());

        var list = service.getItems(USER_ID_1, FROM, SIZE);

        assertNotNull(list);
        assertEquals(0, list.size());
        verify(userRepository, times(1)).getById(USER_ID_1);
        verify(itemRepository, times(1)).getItems(USER_ID_1, Utils.newPage(FROM, SIZE));
    }

    @Test
    void getItemsWithWrongUserIdShouldThrowNotFoundException() {
        when(userRepository.getById(anyLong())).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> service.getItems(USER_ID_1, FROM, SIZE));
        verify(userRepository, times(1)).getById(USER_ID_1);
        verify(itemRepository, never()).getItems(anyLong(), any());
    }

    @Test
    void addNewItemWithCorrectArgumentsShouldReturnItem() {
        when(userRepository.getById(anyLong())).thenReturn(USER_1);
        when(itemRepository.addNewItem(any(), any(), any())).thenReturn(ITEM_1);

        var result = service.addNewItem(USER_ID_1, ITEM_1);

        assertNotNull(result);
        assertEquals(ITEM_1, result);
        verify(userRepository, times(1)).getById(USER_ID_1);
        verify(itemRepository, times(1)).addNewItem(USER_1, ITEM_1, null);
        verify(itemRequestRepository, never()).getById(anyLong());
    }

    @Test
    void addNewItemWithWrongUserIdShouldThrowNotFoundException() {
        when(userRepository.getById(anyLong())).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> service.addNewItem(USER_ID_1, ITEM_1));
        verify(userRepository, times(1)).getById(USER_ID_1);
        verify(itemRepository, never()).addNewItem(any(), any(), any());
        verify(itemRequestRepository, never()).getById(anyLong());
    }

    @Test
    void addNewItemWithItemRequestShouldReturnItem() {
        when(userRepository.getById(anyLong())).thenReturn(USER_2);
        when(itemRepository.addNewItem(any(), any(), any())).thenReturn(ITEM_2);
        when(itemRequestRepository.getById(anyLong())).thenReturn(ITEM_REQUEST);

        var result = service.addNewItem(USER_ID_2, ITEM_2);

        assertNotNull(result);
        assertEquals(ITEM_2, result);
        verify(userRepository, times(1)).getById(USER_ID_2);
        verify(itemRepository, times(1)).addNewItem(USER_2, ITEM_2, ITEM_REQUEST);
        verify(itemRequestRepository, times(1)).getById(ITEM_REQUEST_ID);
    }

    @Test
    void deleteItemWithCorrectArgumentsShouldWorkCorrectly() {
        when(userRepository.getById(anyLong())).thenReturn(USER_1);
        doNothing().when(itemRepository).deleteItem(anyLong(), anyLong());

        service.deleteItem(USER_ID_1, ITEM_ID_1);

        verify(userRepository, times(1)).getById(USER_ID_1);
        verify(itemRepository, times(1)).deleteItem(USER_ID_1, ITEM_ID_1);
    }

    @Test
    void deleteItemWithWrongUserIdShouldThrowNotFoundException() {
        when(userRepository.getById(anyLong())).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> service.deleteItem(USER_ID_1, ITEM_ID_1));
        verify(userRepository, times(1)).getById(USER_ID_1);
        verify(itemRepository, never()).deleteItem(anyLong(), anyLong());
    }

    @Test
    void deleteItemWithWrongItemIdShouldThrowNotFoundException() {
        when(userRepository.getById(anyLong())).thenReturn(USER_1);
        doThrow(NotFoundException.class).when(itemRepository).deleteItem(anyLong(), anyLong());

        assertThrows(NotFoundException.class, () -> service.deleteItem(USER_ID_1, ITEM_ID_1));
        verify(userRepository, times(1)).getById(USER_ID_1);
        verify(itemRepository, times(1)).deleteItem(USER_ID_1, ITEM_ID_1);
    }

    @Test
    void updateItemWithCorrectArgumentsShouldReturnItem() {
        var item = ITEM_1.toBuilder()
                .name("Дрель")
                .build();
        when(userRepository.getById(anyLong())).thenReturn(USER_1);
        when(itemRepository.update(anyLong(), any())).thenReturn(item);

        var result = service.updateItem(USER_ID_1, item);

        assertNotNull(result);
        assertEquals(item.getName(), result.getName());
        verify(userRepository, times(1)).getById(USER_ID_1);
        verify(itemRepository, times(1)).update(USER_ID_1, item);
    }

    @Test
    void updateItemWithWrongUserIdShouldThrowNotFoundException() {
        var item = ITEM_2.toBuilder()
                .name("Дрель")
                .build();

        when(userRepository.getById(anyLong())).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> service.updateItem(USER_ID_1, item));
        verify(userRepository, times(1)).getById(USER_ID_1);
        verify(itemRepository, never()).update(anyLong(), any());
    }

    @Test
    void updateItemWhenUserDoesNotOwnItemThenThrowNotFoundException() {
        var item = ITEM_2.toBuilder()
                .name("Дрель")
                .build();

        when(userRepository.getById(anyLong())).thenReturn(USER_1);
        when(itemRepository.update(anyLong(), any())).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> service.updateItem(USER_ID_1, item));
        verify(userRepository, times(1)).getById(USER_ID_1);
        verify(itemRepository, times(1)).update(USER_ID_1, item);
    }

    @Test
    void getItemWithCorrectArgumentsShouldReturnItem() {
        when(userRepository.getById(anyLong())).thenReturn(USER_1);
        when(itemRepository.getItem(anyLong())).thenReturn(ITEM_1);
        when(commentRepository.find(anyLong())).thenReturn(Collections.emptyList());
        when(bookingRepository.findLastApproved(anyLong())).thenReturn(null);
        when(bookingRepository.findNextApproved(anyLong())).thenReturn(null);

        var item = service.getItem(USER_ID_1, ITEM_ID_1);

        assertNotNull(item);
        assertEquals(ITEM_ID_1, item.getId());
        assertNull(item.getLast());
        assertNull(item.getNext());
        assertNotNull(item.getComments());
        assertEquals(0, item.getComments().size());
        verify(userRepository, times(1)).getById(USER_ID_1);
        verify(itemRepository, times(1)).getItem(ITEM_ID_1);
        verify(commentRepository, times(1)).find(ITEM_ID_1);
        verify(bookingRepository, times(1)).findLastApproved(ITEM_ID_1);
        verify(bookingRepository, times(1)).findNextApproved(ITEM_ID_1);
    }

    @Test
    void getItemWithWrongUserIdShouldThrowNotFoundException() {
        when(userRepository.getById(anyLong())).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> service.getItem(USER_ID_2, ITEM_ID_1));
        verify(userRepository, times(1)).getById(USER_ID_2);
        verify(itemRepository, never()).getItem(anyLong());
        verify(commentRepository, never()).find(anyLong());
        verify(bookingRepository, never()).findLastApproved(anyLong());
        verify(bookingRepository, never()).findNextApproved(anyLong());
    }

    @Test
    void getItemWithWrongItemIdShouldThrowNotFoundException() {
        when(userRepository.getById(anyLong())).thenReturn(USER_1);
        when(itemRepository.getItem(anyLong())).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> service.getItem(USER_ID_1, ITEM_ID_2));
        verify(userRepository, times(1)).getById(USER_ID_1);
        verify(itemRepository, times(1)).getItem(ITEM_ID_2);
        verify(commentRepository, never()).find(anyLong());
        verify(bookingRepository, never()).findLastApproved(anyLong());
        verify(bookingRepository, never()).findNextApproved(anyLong());
    }

    @Test
    void getItemWhenUserDoesNotOwnItemThenReturnItemWithoutLastAndNext() {
        when(userRepository.getById(anyLong())).thenReturn(USER_2);
        when(itemRepository.getItem(anyLong())).thenReturn(ITEM_1);
        when(commentRepository.find(anyLong())).thenReturn(Collections.emptyList());

        var item = service.getItem(USER_ID_2, ITEM_ID_1);

        assertNotNull(item);
        assertEquals(ITEM_ID_1, item.getId());
        assertNull(item.getLast());
        assertNull(item.getNext());
        assertNotNull(item.getComments());
        assertEquals(0, item.getComments().size());
        verify(userRepository, times(1)).getById(USER_ID_2);
        verify(itemRepository, times(1)).getItem(ITEM_ID_1);
        verify(commentRepository, times(1)).find(ITEM_ID_1);
        verify(bookingRepository, never()).findLastApproved(anyLong());
        verify(bookingRepository, never()).findNextApproved(anyLong());
    }

    @Test
    void searchWithCorrectArgumentsShouldReturnList() {
        var query = "nAme";

        when(itemRepository.searchBy(anyString(), any())).thenReturn(Collections.singletonList(ITEM_1));

        var items = service.searchBy(query, FROM, SIZE);

        assertNotNull(items);
        assertEquals(1, items.size());
        verify(itemRepository, times(1)).searchBy(query, Utils.newPage(FROM, SIZE));
    }

    @Test
    void searchWithWithEmptyQueryShouldReturnEmptyList() {
        var query = "";

        when(itemRepository.searchBy(anyString(), any())).thenReturn(Collections.emptyList());

        var items = service.searchBy(query, FROM, SIZE);

        assertNotNull(items);
        assertEquals(0, items.size());
        verify(itemRepository, times(1)).searchBy(query, Utils.newPage(FROM, SIZE));
    }

    @Test
    void createCommentWithCorrectArgumentsShouldReturnItem() {
        when(bookingRepository.findApprovedItemFor(anyLong(), anyLong())).thenReturn(BOOKING);
        when(itemRepository.getItem(anyLong())).thenReturn(ITEM_1);
        when(userRepository.getById(anyLong())).thenReturn(USER_1);
        when(commentRepository.create(any(), any(), any(), any())).thenReturn(COMMENT);

        var comment = Comment.builder().text("ok").build();
        var created = service.createComment(USER_ID_1, ITEM_ID_1, comment);

        assertNotNull(created);
        assertNotNull(created.getAuthor());
        assertEquals(USER_ID_1, created.getAuthor().getId());
        verify(bookingRepository, times(1)).findApprovedItemFor(ITEM_ID_1, USER_ID_1);
        verify(itemRepository, times(1)).getItem(ITEM_ID_1);
        verify(userRepository, times(1)).getById(USER_ID_1);
        verify(itemRequestRepository, never()).getById(anyLong());
        verify(commentRepository, times(1)).create(ITEM_1, USER_1, comment, null);
    }

    @Test
    void createCommentWhenUserDoNotCreateBookingThenThrowBadRequestException() {
        when(bookingRepository.findApprovedItemFor(anyLong(), anyLong())).thenReturn(null);

        var comment = Comment.builder().text("ok").build();

        assertThrows(BadRequestException.class, () -> service.createComment(USER_ID_1, ITEM_ID_1, comment));
        verify(bookingRepository, times(1)).findApprovedItemFor(ITEM_ID_1, USER_ID_1);
        verify(itemRepository, never()).getItem(anyLong());
        verify(userRepository, never()).getById(anyLong());
        verify(itemRequestRepository, never()).getById(anyLong());
        verify(commentRepository, never()).create(any(), any(), any(), any());
    }
}