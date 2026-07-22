package com.newsportal.service.impl;

import com.newsportal.dto.CommentCreateDto;
import com.newsportal.dto.CommentDto;
import com.newsportal.entity.Comment;
import com.newsportal.entity.News;
import com.newsportal.entity.User;
import com.newsportal.exception.ResourceNotFoundException;
import com.newsportal.repository.CommentRepository;
import com.newsportal.repository.NewsRepository;
import com.newsportal.repository.UserRepository;
import com.newsportal.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final NewsRepository newsRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> getCommentsForNews(Integer newsId) {
        return commentRepository.findByNewsIdOrderByCreatedAtDesc(newsId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto addComment(Integer newsId, CommentCreateDto dto, String authorUsername) {
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new ResourceNotFoundException("News not found with id: " + newsId));
        User user = userRepository.findByUsername(authorUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + authorUsername));

        Comment comment = Comment.builder()
                .news(news)
                .user(user)
                .content(dto.getContent())
                .build();

        return toDto(commentRepository.save(comment));
    }

    private CommentDto toDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getContent(),
                comment.getUser().getUsername(),
                comment.getCreatedAt()
        );
    }
}
