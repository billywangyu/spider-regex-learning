package com.learning.entity;
import javax.persistence.*;
@Entity
@Table(name = "regex_examples")
public class RegexExample {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String name;
    @Column(nullable = false)
    private String pattern;
    private String description;
    @Column(name = "example_text")
    private String exampleText;
    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public String getName() { return name; } public void setName(String name) { this.name = name; }
    public String getPattern() { return pattern; } public void setPattern(String pattern) { this.pattern = pattern; }
    public String getDescription() { return description; } public void setDescription(String description) { this.description = description; }
    public String getExampleText() { return exampleText; } public void setExampleText(String exampleText) { this.exampleText = exampleText; }
}
