package com.newsportal.service;

import com.newsportal.dto.NewsCreateDto;
import com.newsportal.dto.NewsDto;

import java.util.List;

public interface NewsService {
    List<NewsDto> getAllNews();
    List<NewsDto> getNewsByCategory(Integer categoryId);
    NewsDto getNewsById(Integer id);
    NewsDto createNews(NewsCreateDto dto, String authorUsername);
    NewsDto updateNews(Integer id, NewsCreateDto dto);
    void deleteNews(Integer id);
}
