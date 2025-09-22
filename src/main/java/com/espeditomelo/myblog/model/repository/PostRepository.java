package com.espeditomelo.myblog.model.repository;

import com.espeditomelo.myblog.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT DISTINCT p FROM Post p " +
            "LEFT JOIN FETCH p.user " +
            "LEFT JOIN FETCH p.postCategories pc " +
            "LEFT JOIN FETCH pc.category " +
            "WHERE p.status = 'A' " +
            "ORDER BY p.createdAt DESC")
    List<Post> findAllWithCategoryAndUser();

    @Query("SELECT DISTINCT p FROM Post p " +
            "LEFT JOIN FETCH p.user " +
            "LEFT JOIN FETCH p.postCategories pc " +
            "LEFT JOIN FETCH pc.category " +
            "WHERE p.status = 'A' " +
            "ORDER BY p.createdAt DESC")
    Page<Post> findAllWithCategoryAndUserPageable(Pageable pageable);

    @Query("SELECT DISTINCT p FROM Post p " +
            "LEFT JOIN FETCH p.user " +
            "LEFT JOIN FETCH p.postCategories pc " +
            "LEFT JOIN FETCH pc.category " +
            "WHERE p.status = 'A' " +
            "AND pc.category.id = :id " +
            "ORDER BY p.createdAt DESC")
    List<Post> findAllWithCategoryAndUserByCategory(@Param("id") Long id);

    @Query("SELECT DISTINCT p FROM Post p " +
            "LEFT JOIN FETCH p.user " +
            "LEFT JOIN FETCH p.postCategories pc " +
            "LEFT JOIN FETCH pc.category " +
            "WHERE p.status = 'A' " +
            "AND pc.category.id = :id " +
            "ORDER BY p.createdAt DESC")
    Page<Post> findAllWithCategoryAndUserByCategoryPageable(@Param("id") Long id, Pageable pageable);

    @Query("SELECT p FROM Post p " +
            "LEFT JOIN FETCH p.user " +
            "LEFT JOIN FETCH p.postCategories pc " +
            "LEFT JOIN FETCH pc.category " +
            "WHERE p.status = 'A' " +
            "AND p.slug = :slug")
    Optional<Post> findBySlugWithCategoryAndUser(@Param("slug") String slug);

    boolean existsBySlug(String slug);

    @Query("SELECT p.slug FROM Post p WHERE p.slug LIKE :slugPattern")
    List<String> findSimilarSlugs(@Param("slugPattern") String slugPattern);
}
