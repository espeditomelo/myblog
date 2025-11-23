package com.espeditomelo.myblog.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "POSTS")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "The title is required")
    @Column(nullable = false)
    @Size(max = 255)
    private String title;

    @Column(unique = true, nullable = false, length = 300)
    private String slug;

    @NotBlank(message = "The body is required")
    @Column(columnDefinition = "TEXT", nullable = false)
    private String body;

    @NotBlank(message = "The status is required")
    @Column(nullable = false)
    private String status;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id",referencedColumnName = "id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonIgnore
    private List<PostCategory> postCategories = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "postLike", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Like> likes = new ArrayList<>();

    @Column(name = "main_image_url")
    private String mainImageUrl;

    public Post() {}

    public Post(String title, String body) {
        this.title = title;
        this.body = body;
        this.slug = generateSlug(title);
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        if(title != null && (this.slug == null || this.slug.isEmpty())) {
            this.slug = generateSlug(title);
        }
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }

//    public void addCategory(Category category){
//        PostCategory postCategory = new PostCategory(this, category);
//        postCategories.add(postCategory);
//    }

    public void addCategory(Category category){
        if (this.postCategories == null) {
            this.postCategories = new ArrayList<>();
        }

        boolean categoryExists = postCategories.stream()
//                .anyMatch(pc -> pc.getCategory().equals(category));
                .anyMatch(postCategory -> postCategory.getCategory() != null && postCategory.getCategory().equals(category));

        if (!categoryExists) {
            PostCategory postCategory = new PostCategory(this, category);
            postCategories.add(postCategory);
            System.out.println(">>>>>>>>>>>>>>>>>>>>> Added Category ID " + category.getId() + ", Name: " + category.getName());
        }
    }

//    public void removeCategory(Category category){
//        PostCategory postCategory = postCategories.stream()
//                .filter(pc -> pc.getCategory().equals(category))
//                .findFirst()
//                .orElse(null);
//        if(postCategory != null){
//            postCategories.remove(postCategory);
//        }
//    }

    public void removeCategory(Category category){
        if (this.postCategories != null) {
            // encontra categoria que relaciona esse post com a categoria
            PostCategory postCategoryToRemove = postCategories.stream().filter(pc -> pc.getCategory()
                    .equals(category))
                    .findFirst()
                    .orElse(null);

            if (postCategoryToRemove != null) {
                postCategories.remove(postCategoryToRemove);
                postCategoryToRemove.setPost(null);
                postCategoryToRemove.setCategory(null);
            }
        }
    }

//    public void clearCategories() {
//        if (this.postCategories != null) {
//            Iterator<PostCategory> iterator = this.postCategories.iterator();
//            while (iterator.hasNext()) {
//                PostCategory postCategory = iterator.next();
//                postCategory.setPost(null);
//                postCategory.setCategory(null);
//            }
//        }
//    }

    public void clearCategories() {

        System.out.println(">>>>>>>>>>>>>>>>>> clearing categories for post: " + this.getId());
        System.out.println(">>>>>>>>>>>>>>>>>> curent categories count: " + (this.postCategories != null ? this.postCategories.size() : 0));

        if (this.postCategories != null) {
            for (PostCategory postCategory : this.postCategories) {
                System.out.println(">>>>>>>>>>>>>>>>>> removing PostCategory - ID: " + this.getId() + ", Category ID: " +
                                (postCategory.getCategory() != null ? postCategory.getCategory().getId() : "NULL"));
            }
            this.postCategories.clear();
        }
        System.out.println(">>>>>>>>>>>>>>>>> After clearing - categories count: " + (this.postCategories != null ? this.postCategories.size() : 0));
    }



    @Transient
    public List<Category> getCategories(){
        return postCategories.stream()
                .map(PostCategory::getCategory)
                .collect(Collectors.toList());
    }

    public List<PostCategory> getPostCategories() {
        return postCategories;
    }

    public void setPostCategories(List<PostCategory> postCategories) {
        this.postCategories = postCategories;
    }

    public String getMainImageUrl() {
        return mainImageUrl;
    }

    public void setMainImageUrl(String mainImageUrl) {
        this.mainImageUrl = mainImageUrl;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<Like> getLikes() {
        return likes;
    }

    public void setLikes(List<Like> likes) {
        this.likes = likes;
    }

    public static String generateSlug(String title) {
        if(title == null || title.trim().isEmpty()) {
            return "";
        }

        return getString(title);
    }

    private static String getString(String text) {
        return text
                .toLowerCase()
                .trim()
                // Remove acentos
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