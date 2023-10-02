package ru.practicum.shareIt.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareIt.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findAllByOwnerId(Long userId);
    @Query("from Item as it " +
            "where " +
            "it.available = true " +
            "and " +
            "(upper(it.name) like upper(concat('%',?1,'%')) " +
            "or upper(it.description) " +
            "like upper(concat('%',?1,'%')))")
    List<Item> searchAvailableByText(String text);
}
