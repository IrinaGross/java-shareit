package ru.practicum.shareit.item.db;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import ru.practicum.shareit.user.db.UserEntity;

import javax.persistence.*;

@Entity
@Table(name = "item_table")
@Getter
@Builder
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
public class ItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id", nullable = false)
    private Long id;

    @Column(name = "item_name", nullable = false)
    private String name;

    @Column(name = "item_desc", nullable = false)
    private String description;

    @Column(name = "item_is_available")
    private Boolean available;

    @ManyToOne
    @JoinColumn(name = "item_owner_id", nullable = false, referencedColumnName = "user_id")
    private UserEntity user;
}
