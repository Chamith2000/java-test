package com.newsportal.controller;

import com.newsportal.dto.NewsCreateDto;
import com.newsportal.service.CategoryService;
import com.newsportal.service.NewsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/editor")
@PreAuthorize("hasAnyRole('EDITOR','ADMIN')")
@RequiredArgsConstructor
public class EditorController {

    private final NewsService newsService;
    private final CategoryService categoryService;

    @GetMapping("/news")
    public String listNews(Model model) {
        model.addAttribute("newsList", newsService.getAllNews());
        return "editor/news-manage";
    }

    @GetMapping("/news/new")
    public String newNewsForm(Model model) {
        model.addAttribute("newsCreateDto", new NewsCreateDto());
        model.addAttribute("allCategories", categoryService.getAllCategories());
        model.addAttribute("formTitle", "Create News");
        model.addAttribute("formAction", "/editor/news/new");
        return "editor/news-form";
    }

    @PostMapping("/news/new")
    public String createNews(@Valid @ModelAttribute("newsCreateDto") NewsCreateDto dto,
                              BindingResult bindingResult,
                              Authentication authentication,
                              Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("allCategories", categoryService.getAllCategories());
            model.addAttribute("formTitle", "Create News");
            model.addAttribute("formAction", "/editor/news/new");
            return "editor/news-form";
        }
        newsService.createNews(dto, authentication.getName());
        return "redirect:/editor/news";
    }

    @GetMapping("/news/{id}/edit")
    public String editNewsForm(@PathVariable Integer id, Model model) {
        var news = newsService.getNewsById(id);
        NewsCreateDto dto = new NewsCreateDto();
        dto.setTitle(news.getTitle());
        dto.setContent(news.getContent());
        dto.setCategoryIds(news.getCategories().stream().map(c -> c.getId()).collect(java.util.stream.Collectors.toSet()));

        model.addAttribute("newsCreateDto", dto);
        model.addAttribute("allCategories", categoryService.getAllCategories());
        model.addAttribute("formTitle", "Edit News");
        model.addAttribute("formAction", "/editor/news/" + id + "/edit");
        return "editor/news-form";
    }

    @PostMapping("/news/{id}/edit")
    public String updateNews(@PathVariable Integer id,
                              @Valid @ModelAttribute("newsCreateDto") NewsCreateDto dto,
                              BindingResult bindingResult,
                              Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("allCategories", categoryService.getAllCategories());
            model.addAttribute("formTitle", "Edit News");
            model.addAttribute("formAction", "/editor/news/" + id + "/edit");
            return "editor/news-form";
        }
        newsService.updateNews(id, dto);
        return "redirect:/editor/news";
    }

    @PostMapping("/news/{id}/delete")
    public String deleteNews(@PathVariable Integer id) {
        newsService.deleteNews(id);
        return "redirect:/editor/news";
    }
}
