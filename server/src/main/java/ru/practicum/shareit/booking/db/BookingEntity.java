package ru.practicum.shareit.booking.db;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.db.ItemEntity;
import ru.practicum.shareit.user.db.UserEntity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "booking_table")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id", nullable = false)
    private Long id;

    @Column(name = "booking_start_date", nullable = false)
    private LocalDateTime start;

    @Column(name = "booking_end_date", nullable = false)
    private LocalDateTime end;

    @ManyToOne
    @JoinColumn(name = "booking_booker_id", nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "booking_item_id", nullable = false)
    private ItemEntity item;

    @Column(name = "booking_status", nullable = false)
    private String status;
}
