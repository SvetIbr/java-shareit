package ru.practicum.shareit.item;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class ItemRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;
    private User user1;
    private User user2;
    private Item item1;
    private Item item2;

    @BeforeEach
    void before() {
        user1 = userRepository.save(new User(1L, "user1", "user1@email"));
        user2 = userRepository.save(new User(2L, "user2", "user2@email"));
        item1 = itemRepository.save(new Item(1L, "item1", "description1", true, user1, null));
        item2 = itemRepository.save(new Item(2L, "item2", "description2", true, user2, null));
    }

    @AfterEach
    void after() {
        itemRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void findAllByOwnerIdOrderByIdAscTest() {
        final Page<Item> ownerItems = itemRepository.findAllByOwnerIdOrderByIdAsc(user1.getId(),
                PageRequest.of(0 / 10, 10));
        assertNotNull(ownerItems);
        assertEquals(1, ownerItems.getTotalElements());
    }

    @Test
    void searchByTextOrderByIdDescTest() {
        Page<Item> items = itemRepository.searchByTextOrderByIdDesc("desc",
                PageRequest.of(0 / 10, 10));
        assertThat(items.getTotalElements(), is(2L));
    }
}
