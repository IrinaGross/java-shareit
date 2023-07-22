package ru.practicum.shareit.request.db;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.db.ItemEntity;
import ru.practicum.shareit.user.db.UserEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;

@Entity
@Table(name = "request_table")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id", nullable = false)
    private Long id;

    @Column(name = "request_desc", length = 512, nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "request_creator_id", nullable = false)
    private UserEntity creator;

    @Column(name = "request_date", nullable = false)
    private LocalDateTime date;

    @OneToMany(mappedBy = "request")
    private Collection<ItemEntity> items;
}
