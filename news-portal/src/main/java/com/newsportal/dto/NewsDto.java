package com.newsportal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsDto {
    private Integer id;
    private String title;
    private String content;
    private String authorUsername;
    private Timestamp createdAt;
    private Set<CategoryDto> categories = new HashSet<>();
}
