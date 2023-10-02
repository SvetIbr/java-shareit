package ru.practicum.shareIt.item.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareIt.booking.model.Booking;
import ru.practicum.shareIt.booking.repository.BookingRepository;
import ru.practicum.shareIt.booking.status.BookingStatus;
import ru.practicum.shareIt.error.exception.BadRequestException;
import ru.practicum.shareIt.error.exception.ItemNotFoundException;
import ru.practicum.shareIt.error.exception.UserNotFoundException;
import ru.practicum.shareIt.item.comment.dto.CommentDto;
import ru.practicum.shareIt.item.comment.mapper.CommentMapper;
import ru.practicum.shareIt.item.comment.model.Comment;
import ru.practicum.shareIt.item.comment.repository.CommentRepository;
import ru.practicum.shareIt.item.model.Item;
import ru.practicum.shareIt.item.repository.ItemRepository;
import ru.practicum.shareIt.user.model.User;
import ru.practicum.shareIt.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;


    @Transactional
    public CommentDto addComment(Long userId, Long itemId, CommentDto commentDto) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new ItemNotFoundException("item not found"));
        User author = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("user not found"));
        Sort sortDesc = Sort.by(Sort.Direction.DESC, "end");
        Booking booking = bookingRepository.findTop1BookingByItemIdAndBookerIdAndEndIsBeforeAndStatusIs(
                itemId, userId, LocalDateTime.now(), BookingStatus.APPROVED, sortDesc).orElseThrow(
                () -> new BadRequestException("no booking for comment"));

        Comment comment = CommentMapper.toComment(commentDto, item, author, LocalDateTime.now());
        comment = commentRepository.save(comment);
        return CommentMapper.toCommentDto(comment);
    }

    @Transactional(readOnly = true)
    public List<CommentDto> getCommentsByItem(Long itemId) {
        List<Comment> comments = commentRepository.findAllByItemId(itemId);
        return comments.stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
    }

}
