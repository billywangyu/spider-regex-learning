package com.learning.controller;
import com.learning.entity.*;
import com.learning.service.LearningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.nio.charset.StandardCharsets;
import java.util.*;
@RestController @RequestMapping("/api")
public class LearningController {
    @Autowired private LearningService service;
    @GetMapping("/categories") public List<WordCategory> getCategories() { return service.getAllCategories(); }
    @GetMapping("/words") public List<Word> getWords(@RequestParam Long categoryId) { return service.getWordsByCategory(categoryId); }
    @GetMapping("/progress") public List<UserWordProgress> getProgress(@RequestParam String userId) { return service.getUserProgress(userId); }
    @PostMapping("/progress") public void updateProgress(@RequestBody Map<String,Object> body) {
        String userId = (String) body.get("userId");
        Long wordId = ((Number) body.get("wordId")).longValue();
        Boolean mastered = (Boolean) body.get("mastered");
        service.updateWordProgress(userId, wordId, mastered);
    }
    @GetMapping("/regex") public List<RegexExample> getRegexExamples() { return service.getAllRegexExamples(); }
    @GetMapping("/category-stats") public List<Map<String,Object>> getCategoryStats(@RequestParam String userId) { return service.getAllCategoryStats(userId); }
    @PostMapping("/import-data") public Map<String,String> importData(@RequestParam("file") MultipartFile file) {
        try {
            String content = new String(file.getBytes(), StandardCharsets.UTF_8);
            service.importData(content);
            return Map.of("status", "success", "message", "数据导入成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Map.of("status", "error", "message", "导入失败: " + e.getMessage());
        }
    }
}
