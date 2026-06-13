package com.learning.service;
import com.learning.entity.*;
import com.learning.repository.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.stream.Collectors;
@Service
public class LearningService {
    @Autowired private WordCategoryRepository categoryRepo;
    @Autowired private WordRepository wordRepo;
    @Autowired private UserWordProgressRepository progressRepo;
    @Autowired private RegexExampleRepository regexRepo;
    public List<WordCategory> getAllCategories() { return categoryRepo.findAll(); }
    public List<Word> getWordsByCategory(Long categoryId) { return wordRepo.findByCategoryId(categoryId); }
    public List<UserWordProgress> getUserProgress(String userId) { return progressRepo.findByUserId(userId); }
    public List<RegexExample> getAllRegexExamples() { return regexRepo.findAll(); }
    @Transactional
    public void updateWordProgress(String userId, Long wordId, Boolean mastered) {
        UserWordProgressId id = new UserWordProgressId(userId, wordId);
        UserWordProgress progress = progressRepo.findById(id).orElse(new UserWordProgress());
        progress.setUserId(userId);
        progress.setWordId(wordId);
        progress.setMastered(mastered);
        progressRepo.save(progress);
    }
    public Map<String, Object> getCategoryProgress(String userId, Long categoryId) {
        List<Word> words = wordRepo.findByCategoryId(categoryId);
        List<UserWordProgress> userProgress = progressRepo.findByUserId(userId);
        Set<Long> masteredWordIds = userProgress.stream().filter(p -> p.getMastered()).map(p -> p.getWordId()).collect(Collectors.toSet());
        long total = words.size();
        long mastered = words.stream().filter(w -> masteredWordIds.contains(w.getId())).count();
        return Map.of("total", total, "mastered", mastered);
    }
    public List<Map<String, Object>> getAllCategoryStats(String userId) {
        List<WordCategory> categories = categoryRepo.findAll();
        List<UserWordProgress> userProgress = progressRepo.findByUserId(userId);
        Set<Long> masteredWordIds = userProgress.stream().filter(p -> p.getMastered()).map(p -> p.getWordId()).collect(Collectors.toSet());
        List<Map<String, Object>> stats = new ArrayList<>();
        for (WordCategory cat : categories) {
            List<Word> wordsInCat = wordRepo.findByCategoryId(cat.getId());
            long total = wordsInCat.size();
            long mastered = wordsInCat.stream().filter(w -> masteredWordIds.contains(w.getId())).count();
            Map<String, Object> stat = new HashMap<>();
            stat.put("categoryId", cat.getId());
            stat.put("total", total);
            stat.put("mastered", mastered);
            stats.add(stat);
        }
        return stats;
    }
    @Transactional
    public void importData(String jsonContent) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(jsonContent);
        if (root.has("categories")) {
            for (JsonNode catNode : root.get("categories")) {
                String name = catNode.get("name").asText();
                Optional<WordCategory> existingCat = categoryRepo.findAll().stream()
                        .filter(c -> c.getName().equals(name)).findFirst();
                WordCategory category;
                if (existingCat.isPresent()) {
                    category = existingCat.get();
                } else {
                    category = new WordCategory();
                    category.setName(name);
                    category.setDescription(catNode.has("description") ? catNode.get("description").asText() : "");
                    category.setIcon(catNode.has("icon") ? catNode.get("icon").asText() : "📚");
                    category = categoryRepo.save(category);
                }
                if (catNode.has("words")) {
                    for (JsonNode wordNode : catNode.get("words")) {
                        String wordStr = wordNode.get("word").asText();
                        boolean exists = wordRepo.findByCategoryId(category.getId()).stream()
                                .anyMatch(w -> w.getWord().equals(wordStr));
                        if (!exists) {
                            Word word = new Word();
                            word.setWord(wordStr);
                            word.setTranslation(wordNode.get("translation").asText());
                            word.setPronunciation(wordNode.has("pronunciation") ? wordNode.get("pronunciation").asText() : "");
                            word.setPartOfSpeech(wordNode.has("partOfSpeech") ? wordNode.get("partOfSpeech").asText() : "");
                            word.setExample(wordNode.has("example") ? wordNode.get("example").asText() : "");
                            word.setDifficulty(wordNode.has("difficulty") ? wordNode.get("difficulty").asText() : "beginner");
                            word.setCategory(category);
                            wordRepo.save(word);
                        }
                    }
                }
            }
        }
        if (root.has("regexExamples")) {
            for (JsonNode regexNode : root.get("regexExamples")) {
                String name = regexNode.get("name").asText();
                if (regexRepo.findAll().stream().noneMatch(r -> r.getName().equals(name))) {
                    RegexExample regex = new RegexExample();
                    regex.setName(name);
                    regex.setPattern(regexNode.get("pattern").asText());
                    regex.setDescription(regexNode.has("description") ? regexNode.get("description").asText() : "");
                    regex.setExampleText(regexNode.has("exampleText") ? regexNode.get("exampleText").asText() : "");
                    regexRepo.save(regex);
                }
            }
        }
    }
}
