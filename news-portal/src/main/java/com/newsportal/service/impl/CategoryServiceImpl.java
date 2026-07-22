package com.newsportal.service.impl;

import com.newsportal.dto.CategoryDto;
import com.newsportal.entity.Category;
import com.newsportal.exception.DuplicateResourceException;
import com.newsportal.exception.ResourceNotFoundException;
import com.newsportal.repository.CategoryRepository;
import com.newsportal.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(c -> modelMapper.map(c, CategoryDto.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDto getCategoryById(Integer id) {
        Category category = findCategoryOrThrow(id);
        return modelMapper.map(category, CategoryDto.class);
    }

    @Override
    public CategoryDto createCategory(CategoryDto dto) {
        categoryRepository.findByName(dto.getName()).ifPresent(c -> {
            throw new DuplicateResourceException("Category '" + dto.getName() + "' already exists");
        });
        Category category = Category.builder().name(dto.getName()).build();
        Category saved = categoryRepository.save(category);
        return modelMapper.map(saved, CategoryDto.class);
    }

    @Override
    public CategoryDto updateCategory(Integer id, CategoryDto dto) {
        Category category = findCategoryOrThrow(id);
        category.setName(dto.getName());
        return modelMapper.map(categoryRepository.save(category), CategoryDto.class);
    }

    @Override
    public void deleteCategory(Integer id) {
        Category category = findCategoryOrThrow(id);
        categoryRepository.delete(category);
    }

    private Category findCategoryOrThrow(Integer id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
    }
}
