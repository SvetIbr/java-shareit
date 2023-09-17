package ru.practicum.shareIt.item.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareIt.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Класс для хранения всех вещей
 *
 * @author Светлана Ибраева
 * @version 1.0
 */
@Slf4j
@Repository
public class ItemRepository {
    /**
     * Поле хранилище всех вещей
     */
    private final Map<Long, Item> items = new HashMap<>();
    /**
     * Поле присваемого идентификатора каждой вещи при добавлении в хранилище
     */
    private long nextId = 1;

    /**
     * Метод добавления вещи
     *
     * @param item {@link Item}
     * @return копию объекта item с присвоенным идентификатором
     */
    public Item create(Item item) {
        item.setId(nextId);
        items.put(nextId, item);
        nextId++;
        log.info("Пользователь {} добавил вещь {}", item.getOwner().getName(), item);
        return item;
    }

    /**
     * Метод обновления информации о вещи
     *
     * @param item {@link Item}
     */
    public void update(Item item) {
        items.put(item.getId(), item);
        log.info("Пользователь {} обновил информацию о вещи: {}", item.getOwner().getName(), item);
    }

    /**
     * Метод получения вещи по идентификатору
     *
     * @param itemId - идентификатор вещи
     * @return копию объекта item с указанным идентификатором,
     * в случае отсутствия вещи по идентификатору - null
     */
    public Item getById(Long itemId) {
        return items.getOrDefault(itemId, null);
    }

    /**
     * Метод получения владельцем всех своих вещей
     *
     * @param userId - идентификатор владельца
     * @return список объектов Item
     */
    public List<Item> getByOwner(Long userId) {
        return items.values().stream()
                .filter(item -> item.getOwner().getId().equals(userId))
                .collect(Collectors.toList());
    }

    /**
     * Метод получения списка вещей, в названии или описании которых содержится text
     *
     * @param text - текст
     * @return список вещей, доступных для аренды и содержащх в описании или названии text
     */
    public Set<Item> search(String text) {
        Set<Item> foundItemsByName = items.values().stream()
                .filter(item -> item.getName().toLowerCase()
                        .contains(text.toLowerCase()))
                .filter(Item::getAvailable)
                .collect(Collectors.toSet());

        Set<Item> foundItemsByDescription = items.values().stream()
                .filter(item -> item.getDescription().toLowerCase()
                        .contains(text.toLowerCase()))
                .filter(Item::getAvailable)
                .collect(Collectors.toSet());
        foundItemsByName.addAll(foundItemsByDescription);

        return Collections.unmodifiableSet(foundItemsByName);
    }

    /**
     * Метод удаления вещи по идентификатору
     *
     * @param id - идентификатор вещи
     */
    public void deleteById(Long id) {
        log.info("Удалена вещь " + items.get(id));
        items.remove(id);
    }
}

