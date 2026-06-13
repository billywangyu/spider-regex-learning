package com.learning.entity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.*;
@Entity
@Table(name = "words")
public class Word {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String word;
    @Column(nullable = false)
    private String translation;
    private String pronunciation;
    @Column(name = "part_of_speech")
    private String partOfSpeech;
    @Column(length = 500)
    private String example;
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    @JsonIgnoreProperties("words")
    private WordCategory category;
    @Column(nullable = false)
    private String difficulty; // beginner, intermediate, advanced
    // getters & setters
    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public String getWord() { return word; } public void setWord(String word) { this.word = word; }
    public String getTranslation() { return translation; } public void setTranslation(String translation) { this.translation = translation; }
    public String getPronunciation() { return pronunciation; } public void setPronunciation(String pronunciation) { this.pronunciation = pronunciation; }
    public String getPartOfSpeech() { return partOfSpeech; } public void setPartOfSpeech(String partOfSpeech) { this.partOfSpeech = partOfSpeech; }
    public String getExample() { return example; } public void setExample(String example) { this.example = example; }
    public WordCategory getCategory() { return category; } public void setCategory(WordCategory category) { this.category = category; }
    public String getDifficulty() { return difficulty; } public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
}
