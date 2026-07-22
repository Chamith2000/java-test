package com.newsportal.repository;

import com.newsportal.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NewsRepository extends JpaRepository<News, Integer> {

    @Query("SELECT DISTINCT n FROM News n JOIN n.categories c WHERE c.id = :categoryId ORDER BY n.createdAt DESC")
    List<News> findByCategoryId(@Param("categoryId") Integer categoryId);

    List<News> findAllByOrderByCreatedAtDesc();
}
