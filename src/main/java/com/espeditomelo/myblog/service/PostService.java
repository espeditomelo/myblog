package com.espeditomelo.myblog.service;

import com.espeditomelo.myblog.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostService {

    List<Post> findAll();
    List<Post> findAllWithCategoryAndUser();
    Page<Post> findAllWithCategoryAndUserPageable(Pageable pageable);
    List<Post> findAllWithCategoryAndUserByCategory(Long id);
    Page<Post> findAllWithCategoryAndUserByCategoryPageable(Long id, Pageable pageable);
    Post findById(Long id);
    Post save(Post post);

    Post findBySlugWithCategoryAndUser(String slug);
    String generateUniqueSlug(String title);

    Page<Post> findAllWithCategoryAndUserByUserPageable(Long id, Pageable pageable);
}
