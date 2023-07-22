package ru.practicum.shareit.item.db;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.db.UserEntity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comment_table")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id", nullable = false)
    private Long id;

    @Column(length = 512, name = "comment_text", nullable = false)
    private String text;

    @Column(name = "comment_created_date", nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "comment_author_id", nullable = false)
    private UserEntity author;

    @ManyToOne
    @JoinColumn(name = "comment_item_id", nullable = false)
    private ItemEntity item;
}
