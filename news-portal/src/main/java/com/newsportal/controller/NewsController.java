package com.newsportal.controller;

import com.newsportal.dto.CommentCreateDto;
import com.newsportal.service.CommentService;
import com.newsportal.service.NewsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;
    private final CommentService commentService;

    // Step 3: full news item + its comments (public)
    @GetMapping("/news/{id}")
    public String newsDetail(@PathVariable Integer id, Model model) {
        model.addAttribute("news", newsService.getNewsById(id));
        model.addAttribute("comments", commentService.getCommentsForNews(id));
        model.addAttribute("commentCreateDto", new CommentCreateDto());
        return "news-detail";
    }

    // Step 4: add a comment - requires authentication (any role: ADMIN/EDITOR/READER)
    @PostMapping("/news/{id}/comment")
    public String addComment(@PathVariable Integer id,
                              @Valid @ModelAttribute("commentCreateDto") CommentCreateDto commentCreateDto,
                              BindingResult bindingResult,
                              Authentication authentication,
                              Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("news", newsService.getNewsById(id));
            model.addAttribute("comments", commentService.getCommentsForNews(id));
            return "news-detail";
        }
        commentService.addComment(id, commentCreateDto, authentication.getName());
        return "redirect:/news/" + id;
    }
}
