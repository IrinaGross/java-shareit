package ru.practicum.shareit.user.db;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import ru.practicum.shareit.booking.db.BookingEntity;
import ru.practicum.shareit.item.db.ItemEntity;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "user_table")
@Getter
@Builder
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Long id;

    @Column(name = "user_name", nullable = false)
    private String name;

    @Column(name = "user_email", nullable = false, unique = true)
    private String email;

    @OneToMany(mappedBy = "user")
    private Collection<ItemEntity> items;

    @OneToMany(mappedBy = "user")
    private Collection<BookingEntity> requests;
}
