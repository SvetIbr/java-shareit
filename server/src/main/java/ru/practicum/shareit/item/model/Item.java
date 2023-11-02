package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;

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
    private String name;

    /**
     * Поле описание
     */
    @Column(name = "description")
    private String description;

    /**
     * Поле статус доступности для аренды
     */
    @Column(name = "is_available")
    private Boolean available;

    /**
     * Поле владелец
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
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
