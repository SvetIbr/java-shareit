package ru.practicum.shareIt.item.model;

import lombok.*;
import ru.practicum.shareIt.request.model.ItemRequest;
import ru.practicum.shareIt.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Класс вещи со свойствами <b>id</b>, <b>name</b>, <b>description</b>,
 * <b>available</b>, <b>owner</b>, <b>request</b> для работы с базой данных
 *
 * @author Светлана Ибраева
 * @version 1.0
 */
@Entity
@Table(name = "items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Item {
    /**
     * Поле идентификатор
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Поле наименование
     */
    @Column(name = "name")
    @NotNull(message = "Не указано наименование")
    @NotBlank(message = "Не заполнено поле \"наименование\"")
    private String name;

    /**
     * Поле описание
     */
    @Column(name = "description")
    @NotNull(message = "Не указано описание")
    @NotBlank(message = "Не заполнено поле \"описание\"")
    private String description;

    /**
     * Поле статус доступности для аренды
     */
    @Column(name = "is_available")
    @NotNull(message = "Не указан статус доступности")
    private Boolean available;

    /**
     * Поле владелец
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    @NotNull(message = "Не указан владелец")
    private User owner;

    /**
     * Поле запрос на вещь:
     * если вещь была создана по запросу другого пользователя, то в этом
     * поле будет храниться ссылка на соответствующий запрос
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id")
    private ItemRequest request;
}
