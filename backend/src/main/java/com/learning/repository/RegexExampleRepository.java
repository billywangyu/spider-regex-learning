package com.learning.repository;
import com.learning.entity.RegexExample;
import org.springframework.data.jpa.repository.JpaRepository;
public interface RegexExampleRepository extends JpaRepository<RegexExample, Long> {}
