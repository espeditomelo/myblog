package com.espeditomelo.myblog.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "CATEGORIES")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false, length = 300)
    private String slugCategory;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<PostCategory> postCategories= new ArrayList<>();

    public Category() {}

//    public Category(String name) {
//        this.name = name;
//        this.slugCategory = generateSlugCategory(name);
//    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        if(name != null && !name.trim().isEmpty()) {
            this.slugCategory = generateSlugCategory(name);
        }
    }

    public String getSlugCategory() {
        return slugCategory;
    }

    public void setSlugCategory(String slugCategory) {
        this.slugCategory = slugCategory;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<Post> getPosts(){
        return postCategories.stream()
                .map(PostCategory::getPost)
                .toList();
    }

    public static String generateSlugCategory(String name) {
        if(name == null || name.trim().isEmpty()) {
            return null;
        }
        return getString(name);
    }

    private static String getString(String text) {
        return text
                .toLowerCase()
                .trim()
                .replaceAll("[àáâãäå]", "a")
                .replaceAll("[èéêë]", "e")
                .replaceAll("[ìíîï]", "i")
                .replaceAll("[òóôõö]", "o")
                .replaceAll("[ùúûü]", "u")
                .replaceAll("[ç]", "c")
                .replaceAll("[ñ]", "n")
                // Remove caracteres especiais e substitui espaços por hífens
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-")
                .replaceAll("^-|-$", "");
    }

}
