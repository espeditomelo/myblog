package com.espeditomelo.myblog.controller;

import com.espeditomelo.myblog.model.Category;
import com.espeditomelo.myblog.model.Comment;
import com.espeditomelo.myblog.model.Post;
import com.espeditomelo.myblog.model.User;
import com.espeditomelo.myblog.service.CategoryService;
import com.espeditomelo.myblog.service.CommentService;
import com.espeditomelo.myblog.service.PostService;
import com.espeditomelo.myblog.service.UserService;
import com.espeditomelo.myblog.service.serviceImpl.ImageStorageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class PostController {

    @Autowired
    PostService postService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    UserService userService;

    @Autowired
    ImageStorageService imageStorageService;

    @Autowired
    CommentService commentService;

    private static final int PAGE_SIZE = 5;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView getPosts(@RequestParam(value = "page", defaultValue = "0") int page) {
        ModelAndView modelAndView = new ModelAndView("posts-list");

        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        Page<Post> postsPage = postService.findAllWithCategoryAndUserPageable(pageable);

        modelAndView.addObject("posts", postsPage.getContent());
        modelAndView.addObject("currentPage", page);
        modelAndView.addObject("totalPages", postsPage.getTotalPages());
        modelAndView.addObject("totalItems", postsPage.getTotalElements());
        modelAndView.addObject("hasNext", postsPage.hasNext());
        modelAndView.addObject("hasPrev", postsPage.hasPrevious());
        modelAndView.addObject("selectedCategory", null);

        return modelAndView;
    }

    @RequestMapping(value = "/{slug:[a-z0-9\\-]+}", method = RequestMethod.GET)
    public ModelAndView getPostBySlug(@PathVariable("slug") String slug) {
        ModelAndView modelAndView = new ModelAndView("postDetailed");
        Post post = postService.findBySlugWithCategoryAndUser(slug);
        if(post == null) {
            return new ModelAndView("redirect:/");
        }
        modelAndView.addObject("post", post);

        List<Comment> comments = commentService.getCommentsByPost(post.getId());
        modelAndView.addObject("comments", comments);

        modelAndView.addObject("comment", new Comment());
        return modelAndView;
    }

    @RequestMapping(value = "/postsbyuser/{username}", method = RequestMethod.GET)
    public ModelAndView getPostsByUser(@PathVariable("username") String username,
                                       @RequestParam(value = "page", defaultValue = "0") int page){
        ModelAndView modelAndView = new ModelAndView("posts-list");

        Pageable pageable = PageRequest.of(page,PAGE_SIZE);
        Page<Post> postsPage = postService.findAllWithCategoryAndUserByUserPageable(username, pageable);

        Optional<User> selectedUser = userService.findByUsername(username);

        modelAndView.addObject("posts", postsPage.getContent());
        modelAndView.addObject("currentPage", page);
        modelAndView.addObject("totalPages", postsPage.getTotalPages());
        modelAndView.addObject("totalItems", postsPage.getTotalElements());
        modelAndView.addObject("hasNext", postsPage.hasNext());
        modelAndView.addObject("hasPrev", postsPage.hasPrevious());
        modelAndView.addObject("username", username);
        modelAndView.addObject("selectedUser", selectedUser.orElse(null));

        return modelAndView;
    }


    @RequestMapping(value = "/postsbycategory/{slugCategory}", method = RequestMethod.GET)
    public ModelAndView getPostsByCategory(@PathVariable("slugCategory") String slugCategory,
                                           @RequestParam(value = "page", defaultValue = "0") int page) {
        ModelAndView modelAndView = new ModelAndView("posts-list");

        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        Page<Post> postsPage = postService.findAllWithCategoryAndUserBySlugCategoryPageable(slugCategory, pageable);

        Optional<Category> selectedCategory = categoryService.findBySlugCategory(slugCategory);

        modelAndView.addObject("posts", postsPage.getContent());
        modelAndView.addObject("currentPage", page);
        modelAndView.addObject("totalPages", postsPage.getTotalPages());
        modelAndView.addObject("totalItems", postsPage.getTotalElements());
        modelAndView.addObject("hasNext", postsPage.hasNext());
        modelAndView.addObject("hasPrev", postsPage.hasPrevious());
        modelAndView.addObject("slugCategory", slugCategory);
        modelAndView.addObject("selectedCategory", selectedCategory.orElse(null));

        return modelAndView;
    }

    @RequestMapping(value = "/admin/posts/adminPosts", method = RequestMethod.GET)
    public ModelAndView getAdminPosts() {
        ModelAndView modelAndView = new ModelAndView("adminPosts");
        List<Post> postsAdmin = postService.findAllWithCategoryAndUser();
        modelAndView.addObject("postsAdmin", postsAdmin);
        return modelAndView;
    }

    @RequestMapping(value = "/admin/posts/editPost/{id}", method = RequestMethod.GET)
    public ModelAndView getPostEdit(@PathVariable("id") long id) {
        ModelAndView modelAndView = new ModelAndView("postForm");
        Post post = postService.findById(id);

        List<Long> selectedCategoryIds = post.getCategories().stream()
                .map(Category::getId)
                .toList();

        modelAndView.addObject("categories", categoryService.findAllByNameAsc());
        modelAndView.addObject("users", userService.findAllEnabled());
        modelAndView.addObject("post", post);
        modelAndView.addObject("selectedCategoryIds", selectedCategoryIds);
        return modelAndView;
    }

    @RequestMapping(value = "/admin/posts/editPost/{id}", method = RequestMethod.POST)
    public ModelAndView saveEditedPost(@Valid Post post, BindingResult  bindingResult,
                                 RedirectAttributes redirectAttributes,
                                 @RequestParam(value = "categoryIds", required = false) List<Long> categoryIds,
                                 @RequestParam(value = "mainImage", required = false) MultipartFile mainImage) {

        if(bindingResult.hasErrors()) {
            return getErrorView(post, categoryIds, "All required fields must be completed");
        }

        try {
            Post existingPost = postService.findById(post.getId());

            String currentImageUrl = existingPost.getMainImageUrl();

            existingPost.setTitle(post.getTitle());
            existingPost.setBody(post.getBody());
            existingPost.setStatus(post.getStatus());
            existingPost.setUser(post.getUser());

            // image upload
            if(mainImage != null && !mainImage.isEmpty()) {
                String imageUrl = imageStorageService.store(mainImage);
                existingPost.setMainImageUrl(imageUrl);
            } else {
                existingPost.setMainImageUrl(currentImageUrl);
            }

            existingPost.getPostCategories().clear();

            // categories
            if(categoryIds != null && !categoryIds.isEmpty()) {
                for(Long categoryId : categoryIds) {
                    Category category = categoryService.findById(categoryId);
                    if (category != null) {
                        existingPost.addCategory(category);
                    }
                }
            }

            postService.save(existingPost);
            redirectAttributes.addFlashAttribute("success", "Post updated successfully");
            return new ModelAndView("redirect:/admin/posts/adminPosts");

        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error updating post: " + e.getMessage());
            return getErrorView(post, categoryIds, "Error updating post");
        }
    }

    @RequestMapping(value = "/admin/posts/newpost", method = RequestMethod.GET)
    public ModelAndView getPostForm() {
        ModelAndView modelAndView = new ModelAndView("postForm");
        List<User> users = userService.findAllEnabled();
        modelAndView.addObject("categories", categoryService.findAllByNameAsc());
        modelAndView.addObject("users", userService.findAllEnabled());
        modelAndView.addObject("post", new Post());
        modelAndView.addObject("selectedCategoryIds", new ArrayList<Long>());
        return modelAndView;
    }

    @RequestMapping(value = "/admin/posts/newpost", method = RequestMethod.POST)
    public ModelAndView savePost(@Valid Post post, BindingResult  bindingResult,
                                 RedirectAttributes redirectAttributes,
                                 @RequestParam(value = "categoryIds", required = true) List<Long> categoryIds,
                                 @RequestParam(value = "mainImage", required = false) MultipartFile mainImage) {

        Post postExistsWithTitle = postService.findBySlugWithCategoryAndUser(post.getSlug());

        if (bindingResult.hasErrors()) {
            System.out.println(">>>>>>>>>>>>>>>>>>>> erros de validacao foram encontrados");
            bindingResult.getAllErrors().forEach(error -> {
                System.out.println(">>>>>>>>>> Erro: " + error.getDefaultMessage());
            });
            return getErrorView(post, categoryIds, "All required fields must be completed");
        }

        if (bindingResult.hasErrors() || categoryIds == null || categoryIds.isEmpty() || postExistsWithTitle != null) {
            ModelAndView modelAndView = new ModelAndView("postForm");
            modelAndView.addObject("categories", categoryService.findAllByNameAsc());
            modelAndView.addObject("users", userService.findAllEnabled());
            modelAndView.addObject("post", post);
            modelAndView.addObject("selectedCategoryIds", categoryIds != null ? categoryIds : new ArrayList<>());

            String message = "";

            if (postExistsWithTitle != null) {
                message = "The title already exists!!!";
            } else if (categoryIds == null || categoryIds.isEmpty()) {
                message = "Please select at least one category";
            } else {
                message = "All required fields must be completed";
            }

            modelAndView.addObject("message", message);

            return modelAndView;
        }

        try {
            // image upload
            if(mainImage != null && !mainImage.isEmpty()) {
                String imageUrl = imageStorageService.store(mainImage);
                System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> imageUrl: " + imageUrl);
                post.setMainImageUrl(imageUrl);
                System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> post.getMainImageUrl(): " + post.getMainImageUrl());
            }

            // categories
                for(Long categoryId : categoryIds) {
                    Category category = categoryService.findById(categoryId);
                    if (category != null) {
                        post.addCategory(category);
                    }
                }

            postService.save(post);
            redirectAttributes.addFlashAttribute("success", "Post created successfully");
            return new ModelAndView("redirect:/admin/posts/adminPosts");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error creating post: " + e.getMessage());
            return getErrorView(post, categoryIds, "Error creating post " + e.getMessage());
        }
    }

    private ModelAndView getErrorView(@Valid Post post, List<Long> categoryIds, String message) {
        ModelAndView modelAndView = new ModelAndView("postForm");
        modelAndView.addObject("categories", categoryService.findAllByNameAsc());
        modelAndView.addObject("users", userService.findAllEnabled());
        modelAndView.addObject("post", post);
        modelAndView.addObject("selectedCategoryIds", categoryIds != null ? categoryIds : new ArrayList<>());
        modelAndView.addObject("message", message);
        return modelAndView;
    }
}
