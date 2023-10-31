package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;


/**
 * Интерфейс хранилища всех вещей
 *
 * @author Светлана Ибраева
 * @version 1.0
 */
public interface ItemRepository extends JpaRepository<Item, Long> {
    /**
     * Метод получения владельцем списка своих вещей
     *
     * @param userId - идентификатор владельца
     * @return список объектов Item
     */
    Page<Item> findAllByOwnerIdOrderByIdAsc(Long userId, Pageable pageable);

    /**
     * Метод получения списка вещей, в названии или описании которых содержится text
     *
     * @param text - текст
     * @return список вещей, доступных для аренды и содержащх в описании или названии text
     */
    @Query("from Item as it " +
            "where " +
            "it.available = true " +
            "and " +
            "(upper(it.name) like upper(concat('%',?1,'%')) " +
            "or upper(it.description) " +
            "like upper(concat('%',?1,'%')))")
    Page<Item> searchByTextOrderByIdDesc(String text, Pageable pageable);

    /**
     * Метод получения списка вещей, которые были созданы в ответ на конкретный запрос
     *
     * @param id - идентификатор запроса
     * @return список объектов Item
     */
    List<Item> findItemsByRequestIdOrderByIdDesc(Long id);
}
