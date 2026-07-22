package com.newsportal.controller;

import com.newsportal.service.CategoryService;
import com.newsportal.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final NewsService newsService;

    // Step 1: landing page - list all categories
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
        return "categories";
    }

    // Step 2: news headlines belonging to a selected category
    @GetMapping("/category/{id}")
    public String newsByCategory(@PathVariable Integer id, Model model) {
        model.addAttribute("category", categoryService.getCategoryById(id));
        model.addAttribute("newsList", newsService.getNewsByCategory(id));
        return "news-list";
    }
}
