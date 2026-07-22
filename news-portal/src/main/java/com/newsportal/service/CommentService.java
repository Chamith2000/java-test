package com.newsportal.service;

import com.newsportal.dto.CommentCreateDto;
import com.newsportal.dto.CommentDto;

import java.util.List;

public interface CommentService {
    List<CommentDto> getCommentsForNews(Integer newsId);
    CommentDto addComment(Integer newsId, CommentCreateDto dto, String authorUsername);
}
