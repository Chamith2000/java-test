package com.newsportal.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CommentCreateDto {

    @NotBlank(message = "Comment cannot be empty")
    private String content;
}
