package ru.practicum.shareIt.item.comment.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.shareIt.item.model.Item;
import ru.practicum.shareIt.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "comments")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {
    /**
     * Поле идентификатор
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "text")
    @NotNull(message = "Не указан текст комментария")
    @NotBlank(message = "Не заполнено поле \"текст\"")
    private String text;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    @Column(name = "create_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd, hh:mm:ss")
    private LocalDateTime created;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return Objects.equals(id, comment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}