package com.learning.entity;
import javax.persistence.*;
@Entity
@Table(name = "user_word_progress")
@IdClass(UserWordProgressId.class)
public class UserWordProgress {
    @Id
    @Column(name = "user_id")
    private String userId;
    @Id
    @Column(name = "word_id")
    private Long wordId;
    private Boolean mastered = false;
    public String getUserId() { return userId; } public void setUserId(String userId) { this.userId = userId; }
    public Long getWordId() { return wordId; } public void setWordId(Long wordId) { this.wordId = wordId; }
    public Boolean getMastered() { return mastered; } public void setMastered(Boolean mastered) { this.mastered = mastered; }
}
