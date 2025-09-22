package com.espeditomelo.myblog.service.serviceImpl;

import com.espeditomelo.myblog.model.Post;
import com.espeditomelo.myblog.model.repository.PostRepository;
import com.espeditomelo.myblog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    PostRepository postRepository;

    @Override
    public List<Post> findAll() {
        return postRepository.findAll();
    }

    @Override
    public List<Post> findAllWithCategoryAndUser() {
        return postRepository.findAllWithCategoryAndUser();
    }

    @Override
    public Page<Post> findAllWithCategoryAndUserPageable(Pageable pageable) {
        return postRepository.findAllWithCategoryAndUserPageable(pageable);
    }

    @Override
    public List<Post> findAllWithCategoryAndUserByCategory(Long id) {
        return postRepository.findAllWithCategoryAndUserByCategory(id);
    }

    @Override
    public Page<Post> findAllWithCategoryAndUserByCategoryPageable(Long id, Pageable pageable) {
        return postRepository.findAllWithCategoryAndUserByCategoryPageable(id, pageable);
    }

    @Override
    public Post findById(Long id) {
        return postRepository.findById(id).get();
    }

    @Override
    public Post save(Post post) {
        if(post.getSlug() == null || post.getSlug().isEmpty()) {
            post.setSlug(generateUniqueSlug(post.getTitle()));
        }
        return postRepository.save(post);
    }


    @Override
    public Post findBySlugWithCategoryAndUser(String slug) {
        return postRepository.findBySlugWithCategoryAndUser(slug).orElse(null);
    }



    @Override
    public String generateUniqueSlug(String title) {
        if (title == null || title.trim().isEmpty()) {
            return "post";
        }

        String baseSlug = Post.generateSlug(title);

        if (!postRepository.existsBySlug(baseSlug)) {
            return baseSlug;
        }

        List<String> similarSlugs = postRepository.findSimilarSlugs(baseSlug + "%");
        int counter = 1;
        String uniqueSlug;

        do {
            uniqueSlug = baseSlug + "-" + counter;
            counter++;
        } while (similarSlugs.contains(uniqueSlug) || postRepository.existsBySlug(uniqueSlug));

        return uniqueSlug;
    }
}
