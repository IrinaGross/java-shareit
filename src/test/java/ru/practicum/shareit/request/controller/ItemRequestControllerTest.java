package ru.practicum.shareit.request.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.TestUtils.*;
import static ru.practicum.shareit.Utils.*;
import static ru.practicum.shareit.request.mapper.ItemRequestMapper.map;

@WebMvcTest(ItemRequestController.class)
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
class ItemRequestControllerTest {
    private static final String ITEM_REQUEST_DESCRIPTION = "description";

    @MockBean
    private ItemRequestService service;
    private final ObjectMapper objectMapper;
    private final MockMvc mockMvc;

    @Test
    @SneakyThrows
    void addNewWithCorrectRequestShouldReturnIsOkWithResponse() {
        var request = ItemRequestDto.builder()
                .description(ITEM_REQUEST_DESCRIPTION)
                .build();
        var response = ItemRequestDto.builder()
                .id(ITEM_REQUEST_ID)
                .description(ITEM_REQUEST_DESCRIPTION)
                .build();

        when(service.create(USER_ID_1, map(request)))
                .thenReturn(map(response));

        mockMvc.perform(post("/requests")
                        .header(X_SHARER_USER_ID_HEADER, USER_ID_1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(response)));

        verify(service, times(1))
                .create(USER_ID_1, map(request));
    }

    @Test
    @SneakyThrows
    void addNewWithoutDescriptionShouldReturnBadRequest() {
        var request = ItemRequestDto.builder()
                .build();

        mockMvc.perform(post("/requests")
                        .header(X_SHARER_USER_ID_HEADER, USER_ID_1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(service, never())
                .create(anyLong(), any());
    }

    @Test
    @SneakyThrows
    void addNewWithWrongUserIdShouldReturnNotFound() {
        var wrongUserId = USER_ID_1;
        var request = ItemRequestDto.builder()
                .description(ITEM_REQUEST_DESCRIPTION)
                .build();

        when(service.create(wrongUserId, map(request)))
                .thenThrow(NotFoundException.class);

        mockMvc.perform(post("/requests")
                        .header(X_SHARER_USER_ID_HEADER, wrongUserId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());

        verify(service, times(1))
                .create(wrongUserId, map(request));
    }

    @Test
    @SneakyThrows
    void addNewWithoutUserIdShouldReturnBadRequest() {
        var request = ItemRequestDto.builder()
                .description(ITEM_REQUEST_DESCRIPTION)
                .build();

        mockMvc.perform(post("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(service, never())
                .create(anyLong(), any());
    }

    @Test
    @SneakyThrows
    void getAllForWithCorrectRequestShouldReturnIsOkWithResponse() {
        when(service.getAll(USER_ID_1))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/requests")
                        .header(X_SHARER_USER_ID_HEADER, USER_ID_1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(Collections.emptyList())));

        verify(service, times(1))
                .getAll(USER_ID_1);
    }

    @Test
    @SneakyThrows
    void getAllForWithWrongUserIdShouldReturnNotFound() {
        var wrongUserId = USER_ID_1;

        when(service.getAll(wrongUserId))
                .thenThrow(NotFoundException.class);

        mockMvc.perform(get("/requests")
                        .header(X_SHARER_USER_ID_HEADER, wrongUserId))
                .andExpect(status().isNotFound());

        verify(service, times(1))
                .getAll(wrongUserId);
    }

    @Test
    @SneakyThrows
    void getAllForWithoutUserIdShouldReturnBadRequest() {
        mockMvc.perform(get("/requests"))
                .andExpect(status().isBadRequest());

        verify(service, never())
                .getAll(anyLong());
    }

    @Test
    @SneakyThrows
    void getByIdWithCorrectRequestShouldReturnIsOkWithResponse() {
        var response = ItemRequestDto.builder()
                .id(ITEM_REQUEST_ID)
                .build();

        when(service.getById(USER_ID_1, ITEM_REQUEST_ID))
                .thenReturn(map(response));

        mockMvc.perform(get("/requests/{requestId}", ITEM_REQUEST_ID)
                        .header(X_SHARER_USER_ID_HEADER, USER_ID_1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(response)));

        verify(service, times(1))
                .getById(USER_ID_1, ITEM_REQUEST_ID);
    }

    @Test
    @SneakyThrows
    void getByIdWithWrongItemRequestIdShouldReturnNotFound() {
        var wrongItemRequestId = ITEM_REQUEST_ID + 1;

        when(service.getById(USER_ID_1, wrongItemRequestId))
                .thenThrow(NotFoundException.class);

        mockMvc.perform(get("/requests/{requestId}", wrongItemRequestId)
                        .header(X_SHARER_USER_ID_HEADER, USER_ID_1))
                .andExpect(status().isNotFound());

        verify(service, times(1))
                .getById(USER_ID_1, wrongItemRequestId);
    }

    @Test
    @SneakyThrows
    void getByIdWithWrongUserIdShouldReturnNotFound() {
        var wrongUserId = USER_ID_1 + 1;

        when(service.getById(wrongUserId, ITEM_REQUEST_ID))
                .thenThrow(NotFoundException.class);

        mockMvc.perform(get("/requests/{requestId}", ITEM_REQUEST_ID)
                        .header(X_SHARER_USER_ID_HEADER, wrongUserId))
                .andExpect(status().isNotFound());

        verify(service, times(1))
                .getById(wrongUserId, ITEM_REQUEST_ID);
    }

    @Test
    @SneakyThrows
    void getByIdWithoutUserIdShouldReturnBadRequest() {
        mockMvc.perform(get("/requests/{requestId}", ITEM_REQUEST_ID))
                .andExpect(status().isBadRequest());

        verify(service, never())
                .getById(anyLong(), anyLong());
    }

    @Test
    @SneakyThrows
    void getAllWithCorrectRequestShouldReturnIsOkWithResponse() {
        when(service.getAll(USER_ID_1, FROM, SIZE))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/requests/all")
                        .header(X_SHARER_USER_ID_HEADER, USER_ID_1)
                        .param(FROM_REQUEST_PARAM, Integer.toString(FROM))
                        .param(SIZE_REQUEST_PARAM, Integer.toString(SIZE)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(Collections.emptyList())));

        verify(service, times(1))
                .getAll(USER_ID_1, FROM, SIZE);
    }

    @Test
    @SneakyThrows
    void getAllWithWrongUserIdShouldReturnNotFound() {
        var wrongUserId = USER_ID_1;

        when(service.getAll(wrongUserId, FROM, SIZE))
                .thenThrow(NotFoundException.class);

        mockMvc.perform(get("/requests/all")
                        .header(X_SHARER_USER_ID_HEADER, wrongUserId)
                        .param(FROM_REQUEST_PARAM, Integer.toString(FROM))
                        .param(SIZE_REQUEST_PARAM, Integer.toString(SIZE)))
                .andExpect(status().isNotFound());

        verify(service, times(1))
                .getAll(wrongUserId, FROM, SIZE);
    }

    @Test
    @SneakyThrows
    void getAllWithoutUserIdShouldReturnBadRequest() {
        mockMvc.perform(get("/requests/all")
                        .param(FROM_REQUEST_PARAM, Integer.toString(FROM))
                        .param(SIZE_REQUEST_PARAM, Integer.toString(SIZE)))
                .andExpect(status().isBadRequest());

        verify(service, never())
                .getAll(anyLong(), anyInt(), anyInt());
    }

    @Test
    @SneakyThrows
    void getAllWithoutPaginationParamsShouldReturnIsOkWithResponse() {
        var expectedFrom = FROM;
        var expectedSize = SIZE;

        when(service.getAll(USER_ID_1, expectedFrom, expectedSize))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/requests/all")
                        .header(X_SHARER_USER_ID_HEADER, USER_ID_1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(Collections.emptyList())));

        verify(service, times(1))
                .getAll(USER_ID_1, expectedFrom, expectedSize);
    }

    @Test
    @SneakyThrows
    void getAllWithWrongFromShouldReturnBadRequest() {
        var wrongFrom = -1;

        mockMvc.perform(get("/requests/all")
                        .header(X_SHARER_USER_ID_HEADER, USER_ID_1)
                        .param(FROM_REQUEST_PARAM, Integer.toString(wrongFrom))
                        .param(SIZE_REQUEST_PARAM, Integer.toString(SIZE)))
                .andExpect(status().isBadRequest());

        verify(service, never())
                .getAll(anyLong(), anyInt(), anyInt());
    }

    @Test
    @SneakyThrows
    void getAllWithWrongSizeShouldReturnBadRequest() {
        var wrongSize = 0;

        mockMvc.perform(get("/requests/all")
                        .header(X_SHARER_USER_ID_HEADER, USER_ID_1)
                        .param(FROM_REQUEST_PARAM, Integer.toString(FROM))
                        .param(SIZE_REQUEST_PARAM, Integer.toString(wrongSize)))
                .andExpect(status().isBadRequest());

        verify(service, never())
                .getAll(anyLong(), anyInt(), anyInt());
    }
}