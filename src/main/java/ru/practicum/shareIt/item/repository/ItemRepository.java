package ru.practicum.shareIt.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareIt.item.model.Item;

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
    List<Item> findAllByOwnerId(Long userId);

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
    List<Item> searchByText(String text);
}
