package com.learning.repository;
import com.learning.entity.WordCategory;
import org.springframework.data.jpa.repository.JpaRepository;
public interface WordCategoryRepository extends JpaRepository<WordCategory, Long> {}
