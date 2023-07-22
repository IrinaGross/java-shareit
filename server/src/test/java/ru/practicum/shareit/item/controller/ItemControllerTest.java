package ru.practicum.shareit.item.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.Utils;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.Const.*;
import static ru.practicum.shareit.TestUtils.*;
import static ru.practicum.shareit.item.mapper.CommentMapper.map;
import static ru.practicum.shareit.item.mapper.ItemMapper.map;

@WebMvcTest(ItemController.class)
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
class ItemControllerTest {
    private static final String SEARCH_QUERY = "soap";
    private static final String ITEM_NAME = "name";
    private static final String ITEM_DESC = "desc";
    private static final String ITEM_NEW_DESC = "DESC";
    private static final boolean ITEM_AVAILABLE = true;
    private static final Long COMMENT_ID = 1L;
    private static final String COMMENT_TEXT = "comment";

    @MockBean
    private ItemService service;
    private final ObjectMapper objectMapper;
    private final MockMvc mockMvc;

    @Test
    @SneakyThrows
    void getItemsWithCorrectRequestShouldReturnIsOkWithResponse() {
        when(service.getItems(anyLong(), any()))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/items")
                        .header(X_SHARER_USER_ID_HEADER, USER_ID_1)
                        .param(FROM_REQUEST_PARAM, Integer.toString(FROM))
                        .param(SIZE_REQUEST_PARAM, Integer.toString(SIZE)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(Collections.emptyList())));

        verify(service, times(1))
                .getItems(USER_ID_1, Utils.newPage(FROM, SIZE));
    }

    @Test
    @SneakyThrows
    void getItemsWithoutPaginationParamsShouldReturnIsOkWithResponse() {
        when(service.getItems(anyLong(), any()))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/items")
                        .header(X_SHARER_USER_ID_HEADER, USER_ID_1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(Collections.emptyList())));

        verify(service, times(1))
                .getItems(USER_ID_1, Utils.newPage(FROM, SIZE));
    }

    @Test
    @SneakyThrows
    void getItemsWithoutPaginationAndStateParamsShouldReturnIsOkWithResponse() {
        when(service.getItems(anyLong(), any()))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/items")
                        .header(X_SHARER_USER_ID_HEADER, USER_ID_1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(Collections.emptyList())));

        verify(service, times(1))
                .getItems(USER_ID_1, Utils.newPage(FROM, SIZE));
    }

    @Test
    @SneakyThrows
    void getItemsWithWrongFromShouldReturnBadRequest() {
        var wrongFrom = -1;

        mockMvc.perform(get("/items")
                        .header(X_SHARER_USER_ID_HEADER, USER_ID_1)
                        .param(FROM_REQUEST_PARAM, Integer.toString(wrongFrom))
                        .param(SIZE_REQUEST_PARAM, Integer.toString(SIZE)))
                .andExpect(status().isBadRequest());

        verify(service, never())
                .getItems(anyLong(), any());
    }

    @Test
    @SneakyThrows
    void getItemsWithWrongSizeShouldReturnBadRequest() {
        var wrongSize = 0;

        mockMvc.perform(get("/items")
                        .header(X_SHARER_USER_ID_HEADER, USER_ID_1)
                        .param(FROM_REQUEST_PARAM, Integer.toString(FROM))
                        .param(SIZE_REQUEST_PARAM, Integer.toString(wrongSize)))
                .andExpect(status().isBadRequest());

        verify(service, never())
                .getItems(anyLong(), any());
    }

    @Test
    @SneakyThrows
    void getItemsWithWrongUserIdShouldReturnNotFound() {
        var wrongUserId = USER_ID_1 + 1;

        when(service.getItems(anyLong(), any()))
                .thenThrow(NotFoundException.class);

        mockMvc.perform(get("/items")
                        .header(X_SHARER_USER_ID_HEADER, wrongUserId)
                        .param(FROM_REQUEST_PARAM, Integer.toString(FROM))
                        .param(SIZE_REQUEST_PARAM, Integer.toString(SIZE)))
                .andExpect(status().isNotFound());

        verify(service, times(1))
                .getItems(wrongUserId, Utils.newPage(FROM, SIZE));
    }

    @Test
    @SneakyThrows
    void getItemsWithoutUserIdShouldReturnBadRequest() {
        mockMvc.perform(get("/items")
                        .param(FROM_REQUEST_PARAM, Integer.toString(FROM))
                        .param(SIZE_REQUEST_PARAM, Integer.toString(SIZE)))
                .andExpect(status().isBadRequest());

        verify(service, never())
                .getItems(anyLong(), any());
    }

    @Test
    @SneakyThrows
    void getItemWithCorrectRequestShouldReturnIsOkWithResponse() {
        var response = ItemDto.builder()
                .id(ITEM_ID_1)
                .build();

        when(service.getItem(anyLong(), anyLong()))
                .thenReturn(map(response, response.getId()));

        mockMvc.perform(get("/items/{itemId}", ITEM_ID_1)
                        .header(X_SHARER_USER_ID_HEADER, USER_ID_1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(response)));

        verify(service, times(1))
                .getItem(USER_ID_1, ITEM_ID_1);
    }

    @Test
    @SneakyThrows
    void getItemWithWrongItemIdShouldReturnNotFound() {
        Long wrongItemId = ITEM_ID_1 + 1;

        when(service.getItem(anyLong(), anyLong()))
                .thenThrow(NotFoundException.class);

        mockMvc.perform(get("/items/{itemId}", wrongItemId)
                        .header(X_SHARER_USER_ID_HEADER, USER_ID_1))
                .andExpect(status().isNotFound());

        verify(service, times(1))
                .getItem(USER_ID_1, wrongItemId);
    }

    @Test
    @SneakyThrows
    void getItemWithWrongUserIdShouldReturnNotFound() {
        Long wrongUserId = USER_ID_1 + 1;

        when(service.getItem(anyLong(), anyLong()))
                .thenThrow(NotFoundException.class);

        mockMvc.perform(get("/items/{itemId}", ITEM_ID_1)
                        .header(X_SHARER_USER_ID_HEADER, wrongUserId))
                .andExpect(status().isNotFound());

        verify(service, times(1))
                .getItem(wrongUserId, ITEM_ID_1);
    }

    @Test
    @SneakyThrows
    void getItemWithoutUserIdShouldReturnBadRequest() {
        mockMvc.perform(get("/items/{itemId}", ITEM_ID_1))
                .andExpect(status().isBadRequest());

        verify(service, never())
                .getItem(anyLong(), anyLong());
    }

    @Test
    @SneakyThrows
    void searchItemsWithCorrectRequestShouldReturnIsOkWithResponse() {
        when(service.searchBy(anyString(), any()))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/items/search")
                        .param(SEARCH_REQUEST_PARAM, SEARCH_QUERY)
                        .param(FROM_REQUEST_PARAM, Integer.toString(FROM))
                        .param(SIZE_REQUEST_PARAM, Integer.toString(SIZE)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(Collections.emptyList())));

        verify(service, times(1))
                .searchBy(SEARCH_QUERY, Utils.newPage(FROM, SIZE));
    }

    @Test
    @SneakyThrows
    void searchItemsWithEmptyQueryShouldReturnIsOkWithEmptyBody() {
        var emptyQuery = "";

        when(service.searchBy(anyString(), any()))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/items/search")
                        .param(SEARCH_REQUEST_PARAM, emptyQuery)
                        .param(FROM_REQUEST_PARAM, Integer.toString(FROM))
                        .param(SIZE_REQUEST_PARAM, Integer.toString(SIZE)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(Collections.emptyList())));

        verify(service, times(1))
                .searchBy(emptyQuery, Utils.newPage(FROM, SIZE));
    }

    @Test
    @SneakyThrows
    void searchItemsWithoutPaginationParamsShouldReturnIsOkWithResponse() {
        when(service.searchBy(anyString(), any()))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/items/search")
                        .param(SEARCH_REQUEST_PARAM, SEARCH_QUERY))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(Collections.emptyList())));

        verify(service, times(1))
                .searchBy(SEARCH_QUERY, Utils.newPage(FROM, SIZE));
    }

    @Test
    @SneakyThrows
    void searchItemsWithoutPaginationAndEmptyQueryParamsShouldReturnIsOkWithResponse() {
        var expectedQuery = "";

        when(service.searchBy(anyString(), any()))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/items/search")
                        .param(SEARCH_REQUEST_PARAM, expectedQuery))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(Collections.emptyList())));

        verify(service, times(1))
                .searchBy(expectedQuery, Utils.newPage(FROM, SIZE));
    }

    @Test
    @SneakyThrows
    void searchItemsWithWrongFromShouldReturnBadRequest() {
        var wrongFrom = -1;

        mockMvc.perform(get("/items/search")
                        .param(SEARCH_REQUEST_PARAM, SEARCH_QUERY)
                        .param(FROM_REQUEST_PARAM, Integer.toString(wrongFrom))
                        .param(SIZE_REQUEST_PARAM, Integer.toString(SIZE)))
                .andExpect(status().isBadRequest());

        verify(service, never())
                .searchBy(anyString(), any());
    }

    @Test
    @SneakyThrows
    void searchItemsWithWrongSizeShouldReturnBadRequest() {
        var wrongSize = 0;

        mockMvc.perform(get("/items/search")
                        .param(SEARCH_REQUEST_PARAM, SEARCH_QUERY)
                        .param(FROM_REQUEST_PARAM, Integer.toString(FROM))
                        .param(SIZE_REQUEST_PARAM, Integer.toString(wrongSize)))
                .andExpect(status().isBadRequest());

        verify(service, never())
                .searchBy(anyString(), any());
    }

    @Test
    @SneakyThrows
    void addItemWithCorrectRequestShouldReturnIsOkWithResponse() {
        var request = ItemDto.builder()
                .name(ITEM_NAME)
                .description(ITEM_DESC)
                .available(ITEM_AVAILABLE)
                .build();
        var response = ItemDto.builder()
                .id(ITEM_ID_1)
                .name(ITEM_NAME)
                .description(ITEM_DESC)
                .available(ITEM_AVAILABLE)
                .build();

        when(service.addNewItem(anyLong(), any()))
                .thenReturn(map(response, response.getId()));

        mockMvc.perform(post("/items")
                        .header(X_SHARER_USER_ID_HEADER, USER_ID_1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(response)));

        verify(service, times(1))
                .addNewItem(USER_ID_1, map(request, request.getId()));
    }

    @Test
    @SneakyThrows
    void addItemWithoutNameShouldReturnBadRequest() {
        var request = ItemDto.builder()
                .description(ITEM_DESC)
                .available(ITEM_AVAILABLE)
                .build();

        mockMvc.perform(post("/items")
                        .header(X_SHARER_USER_ID_HEADER, USER_ID_1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(service, never())
                .addNewItem(anyLong(), any());
    }

    @Test
    @SneakyThrows
    void addItemWithEmptyNameShouldReturnBadRequest() {
        var request = ItemDto.builder()
                .name("")
                .description(ITEM_DESC)
                .available(ITEM_AVAILABLE)
                .build();

        mockMvc.perform(post("/items")
                        .header(X_SHARER_USER_ID_HEADER, USER_ID_1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(service, never())
                .addNewItem(anyLong(), any());
    }

    @Test
    @SneakyThrows
    void addItemWithoutDescriptionShouldReturnBadRequest() {
        var request = ItemDto.builder()
                .name(ITEM_NAME)
                .available(ITEM_AVAILABLE)
                .build();

        mockMvc.perform(post("/items")
                        .header(X_SHARER_USER_ID_HEADER, USER_ID_1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(service, never())
                .addNewItem(anyLong(), any());
    }

    @Test
    @SneakyThrows
    void addItemWithEmptyDescriptionShouldReturnBadRequest() {
        var request = ItemDto.builder()
                .name(ITEM_NAME)
                .description("")
                .available(ITEM_AVAILABLE)
                .build();

        mockMvc.perform(post("/items")
                        .header(X_SHARER_USER_ID_HEADER, USER_ID_1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(service, never())
                .addNewItem(anyLong(), any());
    }

    @Test
    @SneakyThrows
    void addItemWithoutAvailableShouldReturnBadRequest() {
        var request = ItemDto.builder()
                .name(ITEM_NAME)
                .description(ITEM_DESC)
                .build();

        mockMvc.perform(post("/items")
                        .header(X_SHARER_USER_ID_HEADER, USER_ID_1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(service, never())
                .addNewItem(anyLong(), any());
    }

    @Test
    @SneakyThrows
    void addItemWithWrongUserIdShouldReturnBadRequest() {
        var request = ItemDto.builder()
                .name(ITEM_NAME)
                .description(ITEM_DESC)
                .available(ITEM_AVAILABLE)
                .build();

        when(service.addNewItem(anyLong(), any()))
                .thenThrow(NotFoundException.class);

        mockMvc.perform(post("/items")
                        .header(X_SHARER_USER_ID_HEADER, USER_ID_1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());

        verify(service, times(1))
                .addNewItem(USER_ID_1, map(request, null));
    }

    @Test
    @SneakyThrows
    void addItemWithoutUserIdShouldReturnBadRequest() {
        var request = ItemDto.builder()
                .name(ITEM_NAME)
                .description(ITEM_DESC)
                .available(ITEM_AVAILABLE)
                .build();

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(service, never())
                .addNewItem(anyLong(), any());
    }

    @Test
    @SneakyThrows
    void updateItemWithCorrectRequestShouldReturnIsOkWithResponse() {
        var request = ItemDto.builder()
                .description(ITEM_NEW_DESC)
                .build();
        var response = ItemDto.builder()
                .id(ITEM_ID_1)
                .name(ITEM_NAME)
                .description(ITEM_NEW_DESC)
                .available(ITEM_AVAILABLE)
                .build();

        when(service.updateItem(anyLong(), any()))
                .thenReturn(map(response, response.getId()));

        mockMvc.perform(patch("/items/{itemId}", ITEM_ID_1)
                        .header(X_SHARER_USER_ID_HEADER, USER_ID_1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(response)));

        verify(service, times(1))
                .updateItem(USER_ID_1, map(request, ITEM_ID_1));
    }

    @Test
    @SneakyThrows
    void updateItemWithWrongItemIdShouldReturnNotFound() {
        var wrongItemId = ITEM_ID_1 + 1;
        var request = ItemDto.builder()
                .description(ITEM_NEW_DESC)
                .build();

        when(service.updateItem(anyLong(), any()))
                .thenThrow(NotFoundException.class);

        mockMvc.perform(patch("/items/{itemId}", wrongItemId)
                        .header(X_SHARER_USER_ID_HEADER, USER_ID_1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());

        verify(service, times(1))
                .updateItem(USER_ID_1, map(request, wrongItemId));
    }

    @Test
    @SneakyThrows
    void updateItemWithWrongUserIdShouldReturnNotFound() {
        var wrongUserId = USER_ID_1 + 1;
        var request = ItemDto.builder()
                .description(ITEM_NEW_DESC)
                .build();

        when(service.updateItem(anyLong(), any()))
                .thenThrow(NotFoundException.class);

        mockMvc.perform(patch("/items/{itemId}", ITEM_ID_1)
                        .header(X_SHARER_USER_ID_HEADER, wrongUserId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());

        verify(service, times(1))
                .updateItem(wrongUserId, map(request, ITEM_ID_1));
    }

    @Test
    @SneakyThrows
    void updateItemWithoutUserIdShouldReturnBadRequest() {
        var request = ItemDto.builder()
                .description(ITEM_NEW_DESC)
                .build();

        mockMvc.perform(patch("/items/{itemId}", ITEM_ID_1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(service, never())
                .updateItem(anyLong(), any());
    }

    @Test
    @SneakyThrows
    void deleteItemWithCorrectRequestShouldReturnIsOk() {
        mockMvc.perform(delete("/items/{itemId}", ITEM_ID_1)
                        .header(X_SHARER_USER_ID_HEADER, USER_ID_1))
                .andExpect(status().isOk());

        verify(service, times(1))
                .deleteItem(USER_ID_1, ITEM_ID_1);
    }

    @Test
    @SneakyThrows
    void deleteItemWithWrongItemIdShouldReturnNotFound() {
        var wrongItemId = ITEM_ID_1 + 1;

        doThrow(NotFoundException.class)
                .when(service).deleteItem(anyLong(), anyLong());

        mockMvc.perform(delete("/items/{itemId}", wrongItemId)
                        .header(X_SHARER_USER_ID_HEADER, USER_ID_1))
                .andExpect(status().isNotFound());

        verify(service, times(1))
                .deleteItem(USER_ID_1, wrongItemId);
    }

    @Test
    @SneakyThrows
    void deleteItemWithWrongUserIdShouldReturnNotFound() {
        var wrongUserId = USER_ID_1 + 1;

        doThrow(NotFoundException.class)
                .when(service).deleteItem(anyLong(), anyLong());

        mockMvc.perform(delete("/items/{itemId}", ITEM_ID_1)
                        .header(X_SHARER_USER_ID_HEADER, wrongUserId))
                .andExpect(status().isNotFound());

        verify(service, times(1))
                .deleteItem(wrongUserId, ITEM_ID_1);
    }

    @Test
    @SneakyThrows
    void deleteItemWithoutUserIdShouldReturnBadRequest() {
        mockMvc.perform(delete("/items/{itemId}", ITEM_ID_1))
                .andExpect(status().isBadRequest());

        verify(service, never())
                .deleteItem(anyLong(), anyLong());
    }

    @Test
    @SneakyThrows
    void createCommentWithCorrectRequestShouldReturnIsOkWithResponse() {
        var request = CommentDto.builder()
                .text(COMMENT_TEXT)
                .build();
        var response = CommentDto.builder()
                .id(COMMENT_ID)
                .text(COMMENT_TEXT)
                .created(REQUEST_TIME)
                .build();

        when(service.createComment(anyLong(), anyLong(), any()))
                .thenReturn(map(response));

        mockMvc.perform(post("/items/{itemId}/comment", ITEM_ID_1)
                        .header(X_SHARER_USER_ID_HEADER, USER_ID_1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(response)));

        verify(service, times(1))
                .createComment(USER_ID_1, ITEM_ID_1, map(request));
    }

    @Test
    @SneakyThrows
    void createCommentWithWrongItemIdShouldReturnNotFound() {
        Long wrongItemId = ITEM_ID_1 + 1;
        var request = CommentDto.builder()
                .text(COMMENT_TEXT)
                .build();

        when(service.createComment(anyLong(), anyLong(), any()))
                .thenThrow(NotFoundException.class);

        mockMvc.perform(post("/items/{itemId}/comment", wrongItemId)
                        .header(X_SHARER_USER_ID_HEADER, USER_ID_1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());

        verify(service, times(1))
                .createComment(USER_ID_1, wrongItemId, map(request));
    }

    @Test
    @SneakyThrows
    void createCommentWithWrongUserIdShouldReturnNotFound() {
        var wrongUserId = USER_ID_1 + 1;
        var request = CommentDto.builder()
                .text(COMMENT_TEXT)
                .build();

        when(service.createComment(anyLong(), anyLong(), any()))
                .thenThrow(NotFoundException.class);

        mockMvc.perform(post("/items/{itemId}/comment", ITEM_ID_1)
                        .header(X_SHARER_USER_ID_HEADER, wrongUserId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());

        verify(service, times(1))
                .createComment(wrongUserId, ITEM_ID_1, map(request));
    }

    @Test
    @SneakyThrows
    void createCommentWithoutUserIdShouldReturnBadRequest() {
        var request = CommentDto.builder()
                .text(COMMENT_TEXT)
                .build();

        mockMvc.perform(post("/items/{itemId}/comment", ITEM_ID_1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(service, never())
                .createComment(anyLong(), anyLong(), any());
    }
}