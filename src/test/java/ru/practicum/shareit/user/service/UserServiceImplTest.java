package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static ru.practicum.shareit.TestUtils.USER_1;
import static ru.practicum.shareit.TestUtils.USER_ID_1;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl service;

    @Test
    void addUserWithCorrectRequestShouldReturnItem() {
        when(userRepository.addUser(any())).thenReturn(USER_1);

        var build = USER_1.toBuilder().build();
        var user = service.addUser(build);

        assertNotNull(user);
        assertEquals(USER_1.getId(), user.getId());
        verify(userRepository, times(1)).addUser(build);
    }

    @Test
    void getUserWithCorrectRequestShouldReturnItem() {
        when(userRepository.getById(anyLong())).thenReturn(USER_1);

        var user = service.getUser(USER_ID_1);

        assertNotNull(user);
        assertEquals(USER_1.getId(), user.getId());
        verify(userRepository, times(1)).getById(USER_ID_1);
    }

    @Test
    void getUserWithWrongUserIdShouldThrowNotFoundException() {
        when(userRepository.getById(anyLong())).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> service.getUser(USER_ID_1));
        verify(userRepository, times(1)).getById(USER_ID_1);
    }

    @Test
    void getAllUsersWithCorrectRequestShouldReturnItem() {
        when(userRepository.getAllUsers()).thenReturn(Collections.emptyList());

        var user = service.getAllUsers();

        assertNotNull(user);
        assertEquals(0, user.size());
        verify(userRepository, times(1)).getAllUsers();
    }

    @Test
    void updateUserWithCorrectRequestShouldReturnItem() {
        var build = USER_1.toBuilder().name("new").build();

        when(userRepository.updateUser(any())).thenReturn(build);

        var user = service.updateUser(build);

        assertNotNull(user);
        assertEquals(build.getId(), user.getId());
        assertEquals(build.getName(), user.getName());
        verify(userRepository, times(1)).updateUser(build);
    }

    @Test
    void deleteUserWithCorrectRequestShouldReturnItem() {
        doNothing().when(userRepository).deleteUser(anyLong());

        service.deleteUser(USER_ID_1);

        verify(userRepository, times(1)).deleteUser(USER_ID_1);
    }
}