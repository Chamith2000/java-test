package com.newsportal.service.impl;

import com.newsportal.dto.CategoryDto;
import com.newsportal.dto.NewsCreateDto;
import com.newsportal.dto.NewsDto;
import com.newsportal.entity.Category;
import com.newsportal.entity.News;
import com.newsportal.entity.User;
import com.newsportal.exception.ResourceNotFoundException;
import com.newsportal.repository.CategoryRepository;
import com.newsportal.repository.NewsRepository;
import com.newsportal.repository.UserRepository;
import com.newsportal.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional(readOnly = true)
    public List<NewsDto> getAllNews() {
        return newsRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<NewsDto> getNewsByCategory(Integer categoryId) {
        // ensure category exists (throws 404 otherwise)
        categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));

        return newsRepository.findByCategoryId(categoryId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public NewsDto getNewsById(Integer id) {
        News news = findNewsOrThrow(id);
        return toDto(news);
    }

    @Override
    public NewsDto createNews(NewsCreateDto dto, String authorUsername) {
        User author = userRepository.findByUsername(authorUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + authorUsername));

        News news = News.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .author(author)
                .categories(resolveCategories(dto.getCategoryIds()))
                .build();

        return toDto(newsRepository.save(news));
    }

    @Override
    public NewsDto updateNews(Integer id, NewsCreateDto dto) {
        News news = findNewsOrThrow(id);
        news.setTitle(dto.getTitle());
        news.setContent(dto.getContent());
        news.setCategories(resolveCategories(dto.getCategoryIds()));
        return toDto(newsRepository.save(news));
    }

    @Override
    public void deleteNews(Integer id) {
        News news = findNewsOrThrow(id);
        newsRepository.delete(news);
    }

    private Set<Category> resolveCategories(Set<Integer> categoryIds) {
        Set<Category> categories = new HashSet<>();
        for (Integer categoryId : categoryIds) {
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));
            categories.add(category);
        }
        return categories;
    }

    /**
     * Maps a News entity to its DTO. Base scalar fields are mapped via ModelMapper;
     * the author username and nested category DTOs are set explicitly since they
     * require flattening / collection transformation that isn't safe to leave to
     * automatic matching for a many-to-many relationship.
     */
    private NewsDto toDto(News news) {
        NewsDto dto = modelMapper.map(news, NewsDto.class);
        dto.setAuthorUsername(news.getAuthor() != null ? news.getAuthor().getUsername() : "Unknown");
        Set<CategoryDto> categoryDtos = news.getCategories().stream()
                .map(c -> modelMapper.map(c, CategoryDto.class))
                .collect(Collectors.toSet());
        dto.setCategories(categoryDtos);
        return dto;
    }

    private News findNewsOrThrow(Integer id) {
        return newsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("News not found with id: " + id));
    }
}
