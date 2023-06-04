package ru.practicum.shareit.booking;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Data
public class Booking {
    private Long id;
    private Long idItemBooking;
    private Long idUserBooking;
    private LocalDateTime startBooking;
    private LocalDateTime endBooking;
    private Boolean approveBooking;
    private String review;
}
