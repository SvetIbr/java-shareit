package ru.practicum.shareIt.request.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.shareIt.item.dto.ItemForRequestDto;
import ru.practicum.shareIt.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Класс вещи со свойствами <b>id</b>, <b>description</b>, <b>requestor</b>,
 * <b>created</b> и <b>items</b>  для работы через REST-интерфейс
 *
 * @author Светлана Ибраева
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemRequestDto {
    /**
     * Поле идентификатор
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Поле описание
     */
    @Column(name = "description")
    @NotNull
    private String description;

    /**
     * Поле создатель запроса
     */
    @ManyToOne
    @JoinColumn(name = "requestor_id")
    private User requestor;

    /**
     * Поле дата создания
     */
    @Column(name = "create_date")
    @JsonFormat(pattern = "yyyy.MM.dd, HH:mm:ss")
    private LocalDateTime created;

    /**
     * Поле вещи, созданные в ответ на запрос
     */
    private List<ItemForRequestDto> items;
}
