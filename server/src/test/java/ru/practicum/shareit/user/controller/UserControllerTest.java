package ru.practicum.shareit.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.TestUtils.USER_ID_1;
import static ru.practicum.shareit.user.mapper.UserMapper.map;

@WebMvcTest(UserController.class)
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
class UserControllerTest {
    private static final String USER_NAME = "name";
    private static final String USER_EMAIL = "foo@bar.biz";
    private static final String USER_NEW_NAME = "newName";

    @MockBean
    private UserService service;
    private final ObjectMapper objectMapper;
    private final MockMvc mockMvc;

    @Test
    @SneakyThrows
    void createWithCorrectRequestShouldReturnIsOkWithResponse() {
        var request = UserDto.builder()
                .name(USER_NAME)
                .email(USER_EMAIL)
                .build();
        var response = UserDto.builder()
                .id(USER_ID_1)
                .name(USER_NAME)
                .email(USER_EMAIL)
                .build();

        when(service.addUser(any()))
                .thenReturn(map(response, response.getId()));

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(response)));

        verify(service, times(1))
                .addUser(map(request, null));
    }

    @Test
    @SneakyThrows
    void createWithoutNameShouldReturnBadRequest() {
        var request = UserDto.builder()
                .email(USER_EMAIL)
                .build();

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(service, never())
                .addUser(any());
    }

    @Test
    @SneakyThrows
    void createWithEmptyNameShouldReturnBadRequest() {
        var request = UserDto.builder()
                .name("")
                .email(USER_EMAIL)
                .build();

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(service, never())
                .addUser(any());
    }

    @Test
    @SneakyThrows
    void createWithDuplicateEmailShouldReturnIsConflict() {
        var request = UserDto.builder()
                .name(USER_NAME)
                .email(USER_EMAIL)
                .build();

        when(service.addUser(any()))
                .thenThrow(ConflictException.class);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());

        verify(service, times(1))
                .addUser(map(request, null));
    }

    @Test
    @SneakyThrows
    void createWithoutEmailShouldReturnBadRequest() {
        var request = UserDto.builder()
                .name(USER_NAME)
                .build();

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(service, never())
                .addUser(any());
    }

    @Test
    @SneakyThrows
    void createWithWrongEmailShouldReturnBadRequest() {
        var request = UserDto.builder()
                .name(USER_NAME)
                .email("ds")
                .build();

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(service, never())
                .addUser(any());
    }

    @Test
    @SneakyThrows
    void createWithEmptyEmailShouldReturnBadRequest() {
        var request = UserDto.builder()
                .name(USER_NAME)
                .email("")
                .build();

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(service, never())
                .addUser(any());
    }

    @Test
    @SneakyThrows
    void getUserWithCorrectRequestShouldReturnIsOkWithResponse() {
        var response = UserDto.builder()
                .id(USER_ID_1)
                .name(USER_NAME)
                .email(USER_EMAIL)
                .build();

        when(service.getUser(anyLong()))
                .thenReturn(map(response, response.getId()));

        mockMvc.perform(get("/users/{userId}", USER_ID_1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(response)));

        verify(service, times(1))
                .getUser(USER_ID_1);
    }

    @Test
    @SneakyThrows
    void getUserWithWrongUserIdShouldReturnNotFound() {
        var wrongUserId = USER_ID_1 + 1;
        when(service.getUser(anyLong()))
                .thenThrow(NotFoundException.class);

        mockMvc.perform(get("/users/{userId}", wrongUserId))
                .andExpect(status().isNotFound());

        verify(service, times(1))
                .getUser(wrongUserId);
    }

    @Test
    @SneakyThrows
    void findAllWithCorrectRequestShouldReturnIsOkWithResponse() {
        when(service.getAllUsers())
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(Collections.emptyList())));

        verify(service, times(1))
                .getAllUsers();
    }

    @Test
    @SneakyThrows
    void updateWithCorrectRequestShouldReturnIsOkWithResponse() {
        var request = UserDto.builder()
                .name(USER_NEW_NAME)
                .build();
        var response = UserDto.builder()
                .id(USER_ID_1)
                .name(USER_NEW_NAME)
                .email(USER_EMAIL)
                .build();

        when(service.updateUser(any()))
                .thenReturn(map(response, response.getId()));

        mockMvc.perform(patch("/users/{userId}", USER_ID_1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(response)));

        verify(service, times(1))
                .updateUser(map(request, USER_ID_1));
    }

    @Test
    @SneakyThrows
    void updateWithWrongUserIdShouldReturnNotFound() {
        var wrongUserId = USER_ID_1;
        var request = UserDto.builder()
                .name(USER_NEW_NAME)
                .build();

        when(service.updateUser(any()))
                .thenThrow(NotFoundException.class);

        mockMvc.perform(patch("/users/{userId}", wrongUserId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());

        verify(service, times(1))
                .updateUser(map(request, wrongUserId));
    }

    @Test
    @SneakyThrows
    void deleteWithCorrectRequestShouldReturnIsOkWithResponse() {
        doNothing()
                .when(service).deleteUser(anyLong());

        mockMvc.perform(delete("/users/{userId}", USER_ID_1))
                .andExpect(status().isOk());

        verify(service, times(1))
                .deleteUser(USER_ID_1);
    }

    @Test
    @SneakyThrows
    void deleteWithWrongUserIdShouldReturnNotFound() {
        var wrongUserId = USER_ID_1;
        doThrow(NotFoundException.class)
                .when(service).deleteUser(anyLong());

        mockMvc.perform(delete("/users/{userId}", wrongUserId))
                .andExpect(status().isNotFound());

        verify(service, times(1))
                .deleteUser(wrongUserId);
    }
}