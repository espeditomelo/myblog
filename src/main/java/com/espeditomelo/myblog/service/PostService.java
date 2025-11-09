package com.espeditomelo.myblog.service;

import com.espeditomelo.myblog.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface PostService {

    List<Post> findAll();
    List<Post> findAllWithCategoryAndUser();
    Page<Post> findAllWithCategoryAndUserPageable(Pageable pageable);
    List<Post> findAllWithCategoryAndUserByCategory(Long id);
// ?   Page<Post> findAllWithCategoryAndUserByCategoryPageable(Long id, Pageable pageable);
//    Page<Post> findAllWithCategoryAndUserBycategoryNamePageable(String name, Pageable pageable);
    Page<Post> findAllWithCategoryAndUserBySlugCategoryPageable(String slugCategory, Pageable pageable);
    Post findById(Long id);
    Post save(Post post);

    Post findBySlugWithCategoryAndUser(String slug);
    String generateUniqueSlug(String title);

    Page<Post> findAllWithCategoryAndUserByUserPageable(String username, Pageable pageable);
}
