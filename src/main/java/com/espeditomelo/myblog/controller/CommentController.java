package com.espeditomelo.myblog.controller;

import com.espeditomelo.myblog.model.Comment;
import com.espeditomelo.myblog.model.Post;
import com.espeditomelo.myblog.model.event.CommentCreatedEvent;
import com.espeditomelo.myblog.service.CommentService;
import com.espeditomelo.myblog.service.PostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CommentController {

    @Autowired
    CommentService commentService;

    @Autowired
    PostService postService;

    @Autowired
    ApplicationEventPublisher applicationEventPublisher;

//    @PostMapping("/posts/{slug}/comments")
    @PostMapping("/{slug}/comments")
    public String saveComment(@PathVariable("slug") String slug,
                              @RequestParam(required = false) Long parentId,
                              @Valid @ModelAttribute("comment") Comment comment,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes,
                              Model model){

        Post post = postService.findBySlugWithCategoryAndUser(slug);

        if(post == null) {
            redirectAttributes.addFlashAttribute("error", "Post not found");
//            return "redirect:/posts";
            return "redirect:/";
        }

        if (bindingResult.hasErrors()) {
            bindingResult.getFieldErrors().forEach(error -> {
                System.out.println(">>> Campo inválido: " + error.getField()
                        + " | Valor rejeitado: " + error.getRejectedValue()
                        + " | Mensagem: " + error.getDefaultMessage());
            });

            model.addAttribute("post", post);
            model.addAttribute("comment", comment);
            model.addAttribute("error", "All required fields must be completed");

            return "postDetailed";
        }

        comment.setPost(post);

        if(parentId != null) {
            Comment parentComment = commentService.getCommentById(parentId);
            comment.setParent(parentComment);
        }

        Comment commentSaved = commentService.save(comment);

        redirectAttributes.addFlashAttribute("success", "Comment added successfully");

        return "redirect:/" + slug;
    }

//    @PostMapping("/posts/{postId}/comments")
//    public String saveComment(@PathVariable("postId") Long postId,
//                              @RequestParam(required = false) Long parentId,
//                              @Valid @ModelAttribute("comment") Comment comment,
//                              BindingResult bindingResult,
//                              RedirectAttributes redirectAttributes,
//                              Model model){
//
//        Post post = postService.findById(postId);
//
//        if(post == null) {
//            redirectAttributes.addFlashAttribute("error",
//                    "Post not found");
//            return "redirect:/posts";
//        }
//
//        if (bindingResult.hasErrors()) {
//            bindingResult.getFieldErrors().forEach(error -> {
//                System.out.println(">>> Campo inválido: " + error.getField()
//                        + " | Valor rejeitado: " + error.getRejectedValue()
//                        + " | Mensagem: " + error.getDefaultMessage());
//            });
//
//            model.addAttribute("post", post);
//            model.addAttribute("comment", comment);
//            model.addAttribute("error", "All required fields must be completed");
//
//            return "postDetailed";
//        }
//
//        comment.setPost(post);
//
//        if(parentId != null) {
//            Comment parentComment = commentService.getCommentById(parentId);
//            comment.setParent(parentComment);
//        }
//
//        Comment commentSaved = commentService.save(comment);
//
//        redirectAttributes.addFlashAttribute("success", "Comment added successfully");
//
//        return "redirect:/posts/" + postId;
//    }


}
