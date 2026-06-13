package com.learning.entity;
import java.io.Serializable;
import java.util.Objects;
public class UserWordProgressId implements Serializable {
    private String userId;
    private Long wordId;
    public UserWordProgressId() {}
    public UserWordProgressId(String userId, Long wordId) { this.userId = userId; this.wordId = wordId; }
    public String getUserId() { return userId; } public void setUserId(String userId) { this.userId = userId; }
    public Long getWordId() { return wordId; } public void setWordId(Long wordId) { this.wordId = wordId; }
    @Override public boolean equals(Object o) { if (this == o) return true; if (!(o instanceof UserWordProgressId)) return false; UserWordProgressId that = (UserWordProgressId) o; return Objects.equals(userId, that.userId) && Objects.equals(wordId, that.wordId); }
    @Override public int hashCode() { return Objects.hash(userId, wordId); }
}
