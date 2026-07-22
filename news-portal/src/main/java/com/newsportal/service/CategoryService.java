package com.newsportal.service;

import com.newsportal.dto.CategoryDto;

import java.util.List;

public interface CategoryService {
    List<CategoryDto> getAllCategories();
    CategoryDto getCategoryById(Integer id);
    CategoryDto createCategory(CategoryDto dto);
    CategoryDto updateCategory(Integer id, CategoryDto dto);
    void deleteCategory(Integer id);
}
