package com.espeditomelo.myblog.controller;

import com.espeditomelo.myblog.model.Comment;
import com.espeditomelo.myblog.model.Post;
import com.espeditomelo.myblog.service.CommentService;
import com.espeditomelo.myblog.service.PostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CommentController {

    @Autowired
    CommentService commentService;

    @Autowired
    PostService postService;

    @PostMapping("/posts/{postId}/comments")
    public String saveComment(@PathVariable("postId") Long postId,
                              @Valid @ModelAttribute("comment") Comment comment,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes,
                              Model model){

        Post post = postService.findById(postId);


        if(post == null) {
            redirectAttributes.addFlashAttribute("error", "Post not found");
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>posts ta null");
            return "redirect:/posts";
        }



        //System.out.println(">>>>>>>>>>>>>>> comments: " + post.getComments().toString() );


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


//        if(bindingResult.hasErrors()) {
//
//            model.addAttribute("post", post);
//            model.addAttribute("comment", comment);
//            model.addAttribute("error", "All required fields must be completed");
//
//            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>tem campo vazio");
//
//            return "postDetailed";
//        }

        comment.setPost(post);
        commentService.save(comment);

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>estou depois do save comment");

        redirectAttributes.addFlashAttribute("success", "Comment added successfully");
        return "redirect:/posts/" + postId;

    }

}
