package ru.practicum.shareIt.item.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareIt.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class ItemRepository {
    private final Map<Long, Item> items = new HashMap<>();
    private long nextId = 1;

    public Item create(Item item) {
        item.setId(nextId);
        items.put(nextId, item);
        nextId++;
        log.info("Пользователь {} добавил вещь {}", item.getOwner().getName(), item);
        return item;
    }

    public void update(Item item) {
        items.put(item.getId(), item);
        log.info("Пользователь {} обновил информацию о вещи: {}", item.getOwner().getName(), item);
    }

    public Item getById(Long itemId) {
        return items.getOrDefault(itemId, null);
    }

    public List<Item> getByOwner(Long userId) {
        return items.values().stream()
                .filter(item -> item.getOwner().getId().equals(userId))
                .collect(Collectors.toList());
    }

    public Set<Item> search(String text) {
        Set<Item> foundItems = items.values().stream()
                .filter(item -> item.getName().toLowerCase()
                        .contains(text.toLowerCase()))
                .filter(Item::getAvailable)
                .collect(Collectors.toSet());

        Set<Item> foundItems1 = items.values().stream()
                .filter(item -> item.getDescription().toLowerCase()
                        .contains(text.toLowerCase()))
                .filter(Item::getAvailable)
                .collect(Collectors.toSet());
        foundItems.addAll(foundItems1);

        return foundItems;
    }
}

