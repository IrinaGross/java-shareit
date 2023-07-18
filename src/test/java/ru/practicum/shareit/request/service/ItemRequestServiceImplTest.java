package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.Utils;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static ru.practicum.shareit.TestUtils.*;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRequestRepository itemRequestRepository;

    @InjectMocks
    private ItemRequestServiceImpl service;

    @Test
    void createWithCorrectArgumentsShouldReturnItem() {
        when(userRepository.getById(anyLong())).thenReturn(USER_1);
        when(itemRequestRepository.create(any(), any())).thenReturn(ITEM_REQUEST);

        var item = service.create(USER_ID_1, ITEM_REQUEST);

        assertNotNull(item);
        assertEquals(ITEM_REQUEST_ID, item.getId());
        assertEquals(USER_ID_1, item.getCreator().getId());
        verify(userRepository, times(1)).getById(USER_ID_1);
        verify(itemRequestRepository, times(1)).create(USER_1, ITEM_REQUEST);
    }

    @Test
    void createWithWrongUserIdShouldThrowNotFoundException() {
        when(userRepository.getById(anyLong())).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> service.create(USER_ID_1, ITEM_REQUEST));
        verify(userRepository, times(1)).getById(USER_ID_1);
        verify(itemRequestRepository, never()).create(any(), any());
    }

    @Test
    void getAllWithCorrectArgumentsShouldReturnList() {
        when(userRepository.getById(anyLong())).thenReturn(USER_1);
        when(itemRequestRepository.getAll(anyLong())).thenReturn(Collections.emptyList());

        var list = service.getAll(USER_ID_1);

        assertNotNull(list);
        assertEquals(0, list.size());
        verify(userRepository, times(1)).getById(USER_ID_1);
        verify(itemRequestRepository, times(1)).getAll(USER_ID_1);
    }

    @Test
    void getAllWithWrongUserIdShouldThrowNotFoundException() {
        when(userRepository.getById(anyLong())).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> service.getAll(USER_ID_1));
        verify(userRepository, times(1)).getById(USER_ID_1);
        verify(itemRequestRepository, never()).getAll(anyLong());
    }

    @Test
    void getByIdWithCorrectArgumentsShouldReturnItem() {
        when(userRepository.getById(anyLong())).thenReturn(USER_1);
        when(itemRequestRepository.getById(anyLong())).thenReturn(ITEM_REQUEST);

        var item = service.getById(USER_ID_1, ITEM_REQUEST_ID);

        assertNotNull(item);
        assertEquals(ITEM_REQUEST_ID, item.getId());
        verify(userRepository, times(1)).getById(USER_ID_1);
        verify(itemRequestRepository, times(1)).getById(ITEM_REQUEST_ID);
    }

    @Test
    void getByIdWithWrongUserIdShouldThrowNotFoundException() {
        when(userRepository.getById(anyLong())).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> service.getById(USER_ID_1, ITEM_REQUEST_ID));
        verify(userRepository, times(1)).getById(USER_ID_1);
        verify(itemRequestRepository, never()).getById(anyLong());
    }

    @Test
    void getAllWithPaginationWithCorrectArgumentsShouldReturnList() {
        when(userRepository.getById(anyLong())).thenReturn(USER_1);
        when(itemRequestRepository.getAll(anyLong(), any())).thenReturn(Collections.emptyList());

        var list = service.getAll(USER_ID_1, FROM, SIZE);

        assertNotNull(list);
        assertEquals(0, list.size());
        verify(userRepository, times(1)).getById(USER_ID_1);
        verify(itemRequestRepository, times(1)).getAll(ITEM_REQUEST_ID, Utils.newPage(FROM, SIZE));
    }

    @Test
    void getAllWithPaginationWithWrongUserIdShouldThrowNotFoundException() {
        when(userRepository.getById(anyLong())).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> service.getAll(USER_ID_1, FROM, SIZE));
        verify(userRepository, times(1)).getById(USER_ID_1);
        verify(itemRequestRepository, never()).getAll(anyLong(), any());
    }
}