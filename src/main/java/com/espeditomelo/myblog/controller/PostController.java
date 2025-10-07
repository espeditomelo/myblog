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
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

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

    @GetMapping(value = "/")
    public String redirectToPosts(){
        return "redirect:/posts";
    }

    @RequestMapping(value = "/posts", method = RequestMethod.GET)
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

//    @RequestMapping(value = "/posts/{id}", method = RequestMethod.GET)
//    public ModelAndView getPostDetailed(@PathVariable("id") long id) {
//        ModelAndView modelAndView = new ModelAndView("postDetailed");
//        Post post = postService.findById(id);
//        modelAndView.addObject("post", post);
//
//        // comment added
//        modelAndView.addObject("comment", new Comment());
//
//        return modelAndView;
//    }

    @GetMapping(value = "/posts/{id}")
    public String getPostDetailed(@PathVariable Long id, Model model){
        Post post = postService.findById(id);
        model.addAttribute("post", post);
        List<Comment> comments = commentService.getCommentsByPost(id);
        model.addAttribute("comments", comments);
        model.addAttribute("comment", new Comment());
        return "postDetailed";
    }

    @RequestMapping(value = "/title/{slug:[a-z0-9\\-]+}", method = RequestMethod.GET)
    public ModelAndView getPostBySlug(@PathVariable("slug") String slug) {
        ModelAndView modelAndView = new ModelAndView("postDetailed");
        Post post = postService.findBySlugWithCategoryAndUser(slug);
        if(post == null) {
            return new ModelAndView("redirect:/posts");
        }
        modelAndView.addObject("post", post);

        List<Comment> comments = commentService.getCommentsByPost(post.getId());
        modelAndView.addObject("comments", comments);

        modelAndView.addObject("comment", new Comment());
        return modelAndView;
    }

    @RequestMapping(value = "/postsbyuser/{id}", method = RequestMethod.GET)
    public ModelAndView getPostsByUser(@PathVariable("id") long id,
                                       @RequestParam(value = "page", defaultValue = "0") int page){
            ModelAndView modelAndView = new ModelAndView("posts-list");

            Pageable pageable = PageRequest.of(page,PAGE_SIZE);
            Page<Post> postsPage = postService.findAllWithCategoryAndUserByUserPageable(id, pageable);

            User selectedUser = userService.findById(id);

            modelAndView.addObject("posts", postsPage.getContent());
            modelAndView.addObject("currentPage", page);
            modelAndView.addObject("totalPages", postsPage.getTotalPages());
            modelAndView.addObject("totalItems", postsPage.getTotalElements());
            modelAndView.addObject("hasNext", postsPage.hasNext());
            modelAndView.addObject("hasPrev", postsPage.hasPrevious());
            modelAndView.addObject("userId", id);
            modelAndView.addObject("selectedUser", selectedUser);

            return modelAndView;
    }

    @RequestMapping(value = "/postsbycategory/{id}", method = RequestMethod.GET)
    public ModelAndView getPostsByCategory(@PathVariable("id") long id,
                                           @RequestParam(value = "page", defaultValue = "0") int page) {
        ModelAndView modelAndView = new ModelAndView("posts-list");

        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        Page<Post> postsPage = postService.findAllWithCategoryAndUserByCategoryPageable(id, pageable);

        Category selectedCategory = categoryService.findById(id);

        modelAndView.addObject("posts", postsPage.getContent());
        modelAndView.addObject("currentPage", page);
        modelAndView.addObject("totalPages", postsPage.getTotalPages());
        modelAndView.addObject("totalItems", postsPage.getTotalElements());
        modelAndView.addObject("hasNext", postsPage.hasNext());
        modelAndView.addObject("hasPrev", postsPage.hasPrevious());
        modelAndView.addObject("categoryId", id);
        modelAndView.addObject("selectedCategory", selectedCategory);

        return modelAndView;
    }

    @RequestMapping(value = "/newpost", method = RequestMethod.GET)
    public ModelAndView getPostForm() {
        ModelAndView modelAndView = new ModelAndView("postForm");
              List<User> users = userService.findAllEnabled();
        modelAndView.addObject("categories", categoryService.findAll());
        modelAndView.addObject("users", userService.findAllEnabled());
        modelAndView.addObject("post", new Post());
        modelAndView.addObject("selectedCategoryIds", new ArrayList<Long>());
        return modelAndView;
    }

    @RequestMapping(value = "/newpost", method = RequestMethod.POST)
    public ModelAndView savePost(@Valid Post post, BindingResult  bindingResult,
                                 RedirectAttributes redirectAttributes,
                                 @RequestParam(value = "categoryIds", required = true) List<Long> categoryIds,
                                 @RequestParam(value = "mainImage", required = false) MultipartFile mainImage) {

        if(bindingResult.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView("postForm");
            modelAndView.addObject("categories", categoryService.findAll());
            modelAndView.addObject("users", userService.findAllEnabled());
            modelAndView.addObject("post", post);
            modelAndView.addObject("selectedCategoryIds", categoryIds != null ? categoryIds : new ArrayList<>());
            modelAndView.addObject("message", "All required fields must be completed");
            return modelAndView;
        }

        try {
            // image upload
            if(mainImage != null && !mainImage.isEmpty()) {
                String imageUrl = imageStorageService.store(mainImage);
                post.setMainImageUrl(imageUrl);
            }

            // categories
            if(categoryIds != null && !categoryIds.isEmpty()) {
                for(Long categoryId : categoryIds) {
                    Category category = categoryService.findById(categoryId);
                    if (category != null) {
                        post.addCategory(category);
                    }
                }
            }

            postService.save(post);
            redirectAttributes.addFlashAttribute("success", "Post created successfully");
            return new ModelAndView("redirect:/posts");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error creating post: " + e.getMessage());
            return getErrorView(post, categoryIds);
        }



    }

    private ModelAndView getErrorView(@Valid Post post, List<Long> categoryIds) {
        ModelAndView modelAndView = new ModelAndView("postForm");
        modelAndView.addObject("categories", categoryService.findAll());
        modelAndView.addObject("users", userService.findAllEnabled());
        modelAndView.addObject("post", post);
        modelAndView.addObject("selectedCategoryIds", categoryIds != null ? categoryIds : new ArrayList<>());
        modelAndView.addObject("message", "All required fields must be completed");
        return modelAndView;
    }
}
