package ru.practicum.shareit.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.TestUtils.*;
import static ru.practicum.shareit.Utils.*;
import static ru.practicum.shareit.booking.mapper.BookingMapper.map;

@WebMvcTest(BookingController.class)
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
class BookingControllerTest {
    private static final BookingState STATE = BookingState.ALL;
    private static final BookingStatus BOOKING_STATUS = BookingStatus.APPROVED;

    @MockBean
    private BookingService service;
    private final ObjectMapper objectMapper;
    private final MockMvc mockMvc;

    @Test
    @SneakyThrows
    void addRequestWithCorrectRequestShouldReturnIsOkWithResponse() {
        var request = BookingDto.builder()
                .itemId(ITEM_ID_1)
                .start(REQUEST_TIME.plusDays(1L))
                .end(REQUEST_TIME.plusDays(2L))
                .build();
        var response = BookingDto.builder()
                .id(BOOKING_ID)
                .start(REQUEST_TIME.plusDays(1L))
                .end(REQUEST_TIME.plusDays(2L))
                .build();

        when(service.newRequest(anyLong(), anyLong(), any()))
                .thenReturn(map(response));

        mockMvc.perform(post("/bookings")
                        .header(X_SHARER_USER_ID_HEADER, USER_ID_1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(response)));

        verify(service, times(1))
                .newRequest(USER_ID_1, ITEM_ID_1, map(request));
    }

    @Test
    @SneakyThrows
    void addRequestWithoutItemIdShouldReturnBadRequest() {
        var request = BookingDto.builder()
                .itemId(null)
                .start(REQUEST_TIME.plusDays(1L))
                .end(REQUEST_TIME.plusDays(2L))
                .build();

        mockMvc.perform(post("/bookings")
                        .header(X_SHARER_USER_ID_HEADER, USER_ID_1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(service, never())
                .newRequest(anyLong(), anyLong(), any());
    }

    @Test
    @SneakyThrows
    void addRequestWithWrongItemIdShouldReturnNotFound() {
        var wrongItemId = ITEM_ID_1 + 1;
        var request = BookingDto.builder()
                .itemId(wrongItemId)
                .start(REQUEST_TIME.plusDays(1))
                .end(REQUEST_TIME.plusDays(2))
                .build();

        when(service.newRequest(anyLong(), anyLong(), any()))
                .thenThrow(NotFoundException.class);

        mockMvc.perform(post("/bookings")
                        .header(X_SHARER_USER_ID_HEADER, USER_ID_1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());

        verify(service, times(1))
                .newRequest(USER_ID_1, wrongItemId, map(request));
    }

    @Test
    @SneakyThrows
    void addRequestWithStartIsNullShouldReturnBadRequest() {
        var request = BookingDto.builder()
                .itemId(ITEM_ID_1)
                .start(null)
                .end(REQUEST_TIME.plusDays(2L))
                .build();

        mockMvc.perform(post("/bookings")
                        .header(X_SHARER_USER_ID_HEADER, USER_ID_1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(service, never())
                .newRequest(anyLong(), anyLong(), any());
    }

    @Test
    @SneakyThrows
    void addRequestWithStartEqualEndShouldReturnBadRequest() {
        var request = BookingDto.builder()
                .itemId(ITEM_ID_1)
                .start(REQUEST_TIME)
                .end(REQUEST_TIME)
                .build();

        mockMvc.perform(post("/bookings")
                        .header(X_SHARER_USER_ID_HEADER, USER_ID_1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(service, never())
                .newRequest(anyLong(), anyLong(), any());
    }

    @Test
    @SneakyThrows
    void addRequestWithEndBeforeStartShouldReturnBadRequest() {
        var request = BookingDto.builder()
                .itemId(ITEM_ID_1)
                .start(REQUEST_TIME.plusDays(2))
                .end(REQUEST_TIME.plusDays(1))
                .build();

        when(service.newRequest(anyLong(), anyLong(), any()))
                .thenThrow(BadRequestException.class);

        mockMvc.perform(post("/bookings")
                        .header(X_SHARER_USER_ID_HEADER, USER_ID_1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(service, times(1))
                .newRequest(USER_ID_1, ITEM_ID_1, map(request));
    }

    @Test
    @SneakyThrows
    void addRequestWithEndIsPastShouldReturnBadRequest() {
        var request = BookingDto.builder()
                .itemId(ITEM_ID_1)
                .start(REQUEST_TIME.plusDays(1))
                .end(REQUEST_TIME.plusDays(1))
                .build();

        when(service.newRequest(anyLong(), anyLong(), any()))
                .thenThrow(BadRequestException.class);

        mockMvc.perform(post("/bookings")
                        .header(X_SHARER_USER_ID_HEADER, USER_ID_1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(service, times(1))
                .newRequest(USER_ID_1, ITEM_ID_1, map(request));
    }

    @Test
    @SneakyThrows
    void addRequestWithStartIsPastShouldReturnBadRequest() {
        var request = BookingDto.builder()
                .itemId(ITEM_ID_1)
                .start(REQUEST_TIME.plusDays(1))
                .end(REQUEST_TIME.plusDays(1))
                .build();

        when(service.newRequest(anyLong(), anyLong(), any()))
                .thenThrow(BadRequestException.class);

        mockMvc.perform(post("/bookings")
                        .header(X_SHARER_USER_ID_HEADER, USER_ID_1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(service, times(1))
                .newRequest(USER_ID_1, ITEM_ID_1, map(request));
    }

    @Test
    @SneakyThrows
    void addRequestWithWrongUserIdShouldReturnNotFound() {
        var wrongUserId = USER_ID_1 + 1;
        var request = BookingDto.builder()
                .itemId(ITEM_ID_1)
                .start(REQUEST_TIME.plusDays(1))
                .end(REQUEST_TIME.plusDays(2))
                .build();

        when(service.newRequest(anyLong(), anyLong(), any()))
                .thenThrow(NotFoundException.class);

        mockMvc.perform(post("/bookings")
                        .header(X_SHARER_USER_ID_HEADER, wrongUserId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());

        verify(service, times(1))
                .newRequest(wrongUserId, ITEM_ID_1, map(request));
    }

    @Test
    @SneakyThrows
    void addRequestWithoutUserIdShouldReturnBadRequest() {
        var request = BookingDto.builder()
                .itemId(ITEM_ID_1)
                .start(REQUEST_TIME.plusDays(1))
                .end(REQUEST_TIME.plusDays(2))
                .build();

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(service, never())
                .newRequest(anyLong(), anyLong(), any());
    }

    @Test
    @SneakyThrows
    void changeStatusWithCorrectRequestShouldReturnIsOkWithResponse() {
        var response = BookingDto.builder()
                .id(BOOKING_ID)
                .status(BOOKING_STATUS)
                .build();

        when(service.changeRequestStatus(anyLong(), anyLong(), any()))
                .thenReturn(map(response));

        mockMvc.perform(patch("/bookings/{bookingId}", BOOKING_ID)
                        .header(X_SHARER_USER_ID_HEADER, USER_ID_1)
                        .param("approved", "true"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(response)));

        verify(service, times(1))
                .changeRequestStatus(USER_ID_1, BOOKING_ID, BOOKING_STATUS);
    }

    @Test
    @SneakyThrows
    void changeStatusWithWrongBookingIdShouldReturnNotFound() {
        long wrongBookingId = BOOKING_ID + 1;

        when(service.changeRequestStatus(anyLong(), anyLong(), any()))
                .thenThrow(NotFoundException.class);

        mockMvc.perform(patch("/bookings/{bookingId}", wrongBookingId)
                        .header(X_SHARER_USER_ID_HEADER, USER_ID_1)
                        .param("approved", "true"))
                .andExpect(status().isNotFound());

        verify(service, times(1))
                .changeRequestStatus(USER_ID_1, wrongBookingId, BOOKING_STATUS);
    }

    @Test
    @SneakyThrows
    void changeStatusWithAlreadyApprovedStatusShouldReturnBadRequest() {
        when(service.changeRequestStatus(any(), anyLong(), any()))
                .thenThrow(BadRequestException.class);

        mockMvc.perform(patch("/bookings/{bookingId}", BOOKING_ID)
                        .header(X_SHARER_USER_ID_HEADER, USER_ID_1)
                        .param("approved", "true"))
                .andExpect(status().isBadRequest());

        verify(service, times(1))
                .changeRequestStatus(USER_ID_1, BOOKING_ID, BOOKING_STATUS);
    }

    @Test
    @SneakyThrows
    void changeStatusWithWrongUserIdShouldReturnNotFound() {
        long wrongUserId = USER_ID_1 + 1;

        when(service.changeRequestStatus(any(), anyLong(), any()))
                .thenThrow(NotFoundException.class);

        mockMvc.perform(patch("/bookings/{bookingId}", BOOKING_ID)
                        .header(X_SHARER_USER_ID_HEADER, wrongUserId)
                        .param("approved", "true"))
                .andExpect(status().isNotFound());

        verify(service, times(1))
                .changeRequestStatus(wrongUserId, BOOKING_ID, BOOKING_STATUS);
    }

    @Test
    @SneakyThrows
    void changeStatusWithoutUserIdShouldReturnBadRequest() {
        mockMvc.perform(patch("/bookings/{bookingId}", BOOKING_ID)
                        .param("approved", "true"))
                .andExpect(status().isBadRequest());

        verify(service, never())
                .changeRequestStatus(anyLong(), anyLong(), any());
    }

    @Test
    @SneakyThrows
    void getByIdWithCorrectRequestShouldReturnIsOkWithResponse() {
        var response = BookingDto.builder()
                .id(BOOKING_ID)
                .build();

        when(service.getRequestById(any(), anyLong()))
                .thenReturn(map(response));

        mockMvc.perform(get("/bookings/{bookingId}", BOOKING_ID)
                        .header(X_SHARER_USER_ID_HEADER, USER_ID_1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(response)));

        verify(service, times(1))
                .getRequestById(USER_ID_1, BOOKING_ID);
    }

    @Test
    @SneakyThrows
    void getByIdWithWrongBookingIdShouldReturnNotFound() {
        long wrongBookingId = BOOKING_ID + 1;

        when(service.getRequestById(any(), anyLong()))
                .thenThrow(NotFoundException.class);

        mockMvc.perform(get("/bookings/{bookingId}", wrongBookingId)
                        .header(X_SHARER_USER_ID_HEADER, USER_ID_1))
                .andExpect(status().isNotFound());

        verify(service, times(1))
                .getRequestById(USER_ID_1, wrongBookingId);
    }

    @Test
    @SneakyThrows
    void getByIdWithWrongUserIdShouldReturnNotFound() {
        Long wrongUserId = USER_ID_1 + 1;

        when(service.getRequestById(anyLong(), anyLong()))
                .thenThrow(NotFoundException.class);

        mockMvc.perform(get("/bookings/{bookingId}", BOOKING_ID)
                        .header(X_SHARER_USER_ID_HEADER, wrongUserId))
                .andExpect(status().isNotFound());

        verify(service, times(1))
                .getRequestById(wrongUserId, BOOKING_ID);
    }

    @Test
    @SneakyThrows
    void getByIdWithoutUserIdShouldReturnBadRequest() {
        mockMvc.perform(get("/bookings/{bookingId}", BOOKING_ID))
                .andExpect(status().isBadRequest());

        verify(service, never())
                .getRequestById(anyLong(), anyLong());
    }

    @Test
    @SneakyThrows
    void getAllRequestsWithCorrectRequestShouldReturnIsOkWithResponse() {
        when(service.getAllRequests(anyLong(), any(), anyInt(), anyInt()))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/bookings")
                        .header(X_SHARER_USER_ID_HEADER, USER_ID_1)
                        .param(STATE_REQUEST_PARAM, STATE.toString())
                        .param(FROM_REQUEST_PARAM, Integer.toString(FROM))
                        .param(SIZE_REQUEST_PARAM, Integer.toString(SIZE)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(Collections.emptyList())));

        verify(service, times(1))
                .getAllRequests(USER_ID_1, STATE, FROM, SIZE);
    }

    @Test
    @SneakyThrows
    void getAllRequestsWithUnknownStateShouldThrowUnsupportedStatusException() {
        when(service.getAllRequests(anyLong(), any(), anyInt(), anyInt()))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/bookings")
                        .header(X_SHARER_USER_ID_HEADER, USER_ID_1)
                        .param(STATE_REQUEST_PARAM, "STATE")
                        .param(FROM_REQUEST_PARAM, Integer.toString(FROM))
                        .param(SIZE_REQUEST_PARAM, Integer.toString(SIZE)))
                .andExpect(status().isBadRequest());

        verify(service, never())
                .getAllRequests(anyLong(), any(), anyInt(), anyInt());
    }

    @Test
    @SneakyThrows
    void getAllRequestsWithoutPaginationParamsShouldReturnIsOkWithResponse() {
        when(service.getAllRequests(anyLong(), any(), anyInt(), anyInt()))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/bookings")
                        .header(X_SHARER_USER_ID_HEADER, USER_ID_1)
                        .param(STATE_REQUEST_PARAM, STATE.toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(Collections.emptyList())));

        verify(service, times(1))
                .getAllRequests(USER_ID_1, STATE, FROM, SIZE);
    }

    @Test
    @SneakyThrows
    void getAllRequestsWithoutPaginationAndStateParamsShouldReturnIsOkWithResponse() {
        when(service.getAllRequests(anyLong(), any(), anyInt(), anyInt()))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/bookings")
                        .header(X_SHARER_USER_ID_HEADER, USER_ID_1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(Collections.emptyList())));

        verify(service, times(1))
                .getAllRequests(USER_ID_1, STATE, FROM, SIZE);
    }

    @Test
    @SneakyThrows
    void getAllRequestsWithWrongFromShouldReturnBadRequest() {
        var wrongFrom = -1;

        mockMvc.perform(get("/bookings")
                        .header(X_SHARER_USER_ID_HEADER, USER_ID_1)
                        .param(STATE_REQUEST_PARAM, STATE.toString())
                        .param(FROM_REQUEST_PARAM, Integer.toString(wrongFrom))
                        .param(SIZE_REQUEST_PARAM, Integer.toString(SIZE)))
                .andExpect(status().isBadRequest());

        verify(service, never())
                .getAllRequests(anyLong(), any(), anyInt(), anyInt());
    }

    @Test
    @SneakyThrows
    void getAllRequestsWithWrongSizeShouldReturnBadRequest() {
        var wrongSize = 0;

        mockMvc.perform(get("/bookings")
                        .header(X_SHARER_USER_ID_HEADER, USER_ID_1)
                        .param(STATE_REQUEST_PARAM, STATE.toString())
                        .param(FROM_REQUEST_PARAM, Integer.toString(FROM))
                        .param(SIZE_REQUEST_PARAM, Integer.toString(wrongSize)))
                .andExpect(status().isBadRequest());

        verify(service, never())
                .getAllRequests(anyLong(), any(), anyInt(), anyInt());
    }

    @Test
    @SneakyThrows
    void getAllRequestsWithWrongUserIdShouldReturnNotFound() {
        var wrongUserId = USER_ID_1 + 1;

        when(service.getAllRequests(anyLong(), any(), anyInt(), anyInt()))
                .thenThrow(NotFoundException.class);

        mockMvc.perform(get("/bookings")
                        .header(X_SHARER_USER_ID_HEADER, wrongUserId)
                        .param(STATE_REQUEST_PARAM, STATE.toString())
                        .param(FROM_REQUEST_PARAM, Integer.toString(FROM))
                        .param(SIZE_REQUEST_PARAM, Integer.toString(SIZE)))
                .andExpect(status().isNotFound());

        verify(service, times(1))
                .getAllRequests(wrongUserId, STATE, FROM, SIZE);
    }


    @Test
    @SneakyThrows
    void getAllRequestsWithoutUserIdShouldReturnBadRequest() {
        mockMvc.perform(get("/bookings")
                        .param(STATE_REQUEST_PARAM, STATE.toString())
                        .param(FROM_REQUEST_PARAM, Integer.toString(FROM))
                        .param(SIZE_REQUEST_PARAM, Integer.toString(SIZE)))
                .andExpect(status().isBadRequest());

        verify(service, never())
                .getAllRequests(anyLong(), any(), anyInt(), anyInt());
    }

    @Test
    @SneakyThrows
    void getAllRequestsForOwnerWithCorrectRequestShouldReturnIsOkWithResponse() {
        when(service.getAllRequestsForOwner(anyLong(), any(), anyInt(), anyInt()))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/bookings/owner")
                        .header(X_SHARER_USER_ID_HEADER, USER_ID_1)
                        .param(STATE_REQUEST_PARAM, STATE.toString())
                        .param(FROM_REQUEST_PARAM, Integer.toString(FROM))
                        .param(SIZE_REQUEST_PARAM, Integer.toString(SIZE)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(Collections.emptyList())));

        verify(service, times(1))
                .getAllRequestsForOwner(USER_ID_1, STATE, FROM, SIZE);
    }

    @Test
    @SneakyThrows
    void getAllRequestsForOwnerWithoutPaginationParamsShouldReturnIsOkWithResponse() {
        when(service.getAllRequestsForOwner(anyLong(), any(), anyInt(), anyInt()))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/bookings/owner")
                        .header(X_SHARER_USER_ID_HEADER, USER_ID_1)
                        .param(STATE_REQUEST_PARAM, STATE.toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(Collections.emptyList())));

        verify(service, times(1))
                .getAllRequestsForOwner(USER_ID_1, STATE, FROM, SIZE);
    }

    @Test
    @SneakyThrows
    void getAllRequestsForOwnerWithoutPaginationAndStateParamsShouldReturnIsOkWithResponse() {
        when(service.getAllRequestsForOwner(anyLong(), any(), anyInt(), anyInt()))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/bookings/owner")
                        .header(X_SHARER_USER_ID_HEADER, USER_ID_1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(Collections.emptyList())));

        verify(service, times(1))
                .getAllRequestsForOwner(USER_ID_1, STATE, FROM, SIZE);
    }

    @Test
    @SneakyThrows
    void getAllRequestsForOwnerWithWrongFromShouldReturnBadRequest() {
        var wrongFrom = -1;

        mockMvc.perform(get("/bookings/owner")
                        .header(X_SHARER_USER_ID_HEADER, USER_ID_1)
                        .param(STATE_REQUEST_PARAM, STATE.toString())
                        .param(FROM_REQUEST_PARAM, Integer.toString(wrongFrom))
                        .param(SIZE_REQUEST_PARAM, Integer.toString(SIZE)))
                .andExpect(status().isBadRequest());

        verify(service, never())
                .getAllRequestsForOwner(anyLong(), any(), anyInt(), anyInt());
    }

    @Test
    @SneakyThrows
    void getAllRequestsForOwnerWithWrongSizeShouldReturnBadRequest() {
        var wrongSize = 0;

        mockMvc.perform(get("/bookings/owner")
                        .header(X_SHARER_USER_ID_HEADER, USER_ID_1)
                        .param(STATE_REQUEST_PARAM, STATE.toString())
                        .param(FROM_REQUEST_PARAM, Integer.toString(FROM))
                        .param(SIZE_REQUEST_PARAM, Integer.toString(wrongSize)))
                .andExpect(status().isBadRequest());

        verify(service, never())
                .getAllRequestsForOwner(anyLong(), any(), anyInt(), anyInt());
    }

    @Test
    @SneakyThrows
    void getAllRequestsForOwnerWithWrongUserIdShouldReturnNotFound() {
        var wrongUserId = USER_ID_1 + 1;

        when(service.getAllRequestsForOwner(anyLong(), any(), anyInt(), anyInt()))
                .thenThrow(NotFoundException.class);

        mockMvc.perform(get("/bookings/owner")
                        .header(X_SHARER_USER_ID_HEADER, wrongUserId)
                        .param(STATE_REQUEST_PARAM, STATE.toString())
                        .param(FROM_REQUEST_PARAM, Integer.toString(FROM))
                        .param(SIZE_REQUEST_PARAM, Integer.toString(SIZE)))
                .andExpect(status().isNotFound());

        verify(service, times(1))
                .getAllRequestsForOwner(wrongUserId, STATE, FROM, SIZE);
    }

    @Test
    @SneakyThrows
    void getAllRequestsForOwnerWithoutUserIdShouldReturnBarRequest() {
        mockMvc.perform(get("/bookings/owner")
                        .param(STATE_REQUEST_PARAM, STATE.toString())
                        .param(FROM_REQUEST_PARAM, Integer.toString(FROM))
                        .param(SIZE_REQUEST_PARAM, Integer.toString(SIZE)))
                .andExpect(status().isBadRequest());

        verify(service, never())
                .getAllRequestsForOwner(anyLong(), any(), anyInt(), anyInt());
    }
}