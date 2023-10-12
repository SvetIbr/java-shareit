package ru.practicum.shareIt.request.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareIt.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    List<ItemRequest> getByRequestorIdOrderByCreatedAsc(Long userId);

    List<ItemRequest> getByRequestorIdNotOrderByCreatedDesc(Long userId, PageRequest of);
}
