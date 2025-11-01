package com.espeditomelo.myblog.service.serviceImpl;

import com.espeditomelo.myblog.model.Comment;
import com.espeditomelo.myblog.model.Post;
import com.espeditomelo.myblog.model.repository.CommentRepository;
import com.espeditomelo.myblog.service.CommentService;
import com.espeditomelo.myblog.service.EmailService;
import com.espeditomelo.myblog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    EmailService emailService;

    @Autowired
    PostService postService;

    @Override
    public Comment save(Comment comment) {
        Comment commentSaved = commentRepository.save(comment);

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>  Comment saved successfully");

        // Solucao temporadia: executa uma thread separada manualmente
        new Thread( () -> {
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>  Iniciando envio de email em thread separada");
            try {
                sendEmailNotificationAsync(commentSaved);
            } catch (Exception e) {
                System.err.println(">>>>>>>>>>>>>>> Erro ao enviar e-mail " + e.getMessage());
            }
        }).start();

        return commentSaved;
    }

    @Async
    private void sendEmailNotificationAsync(Comment commentSaved) {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>  Iniciando envio de email assíncrono");

        try {
            sendEmailNotification(commentSaved);
        } catch (Exception e) {
            System.err.println("Error on sent assync e-mail");
        }

    }

    @Override
    public List<Comment> findAll() {
        return commentRepository.findAll();
    }

    @Override
    public List<Comment> getCommentsByPost(Long postId) {
        return commentRepository.findByPostIdAndParentIsNullOrderByCreatedAtAsc(postId);
    }

    @Override
    public Comment getCommentById(Long parentId) {
        return commentRepository.getCommentById(parentId);
    }

    @Override
    public void sendEmailNotification(Comment comment) {

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>  dentro do CommentServiceImpl.sendEmailNotification");

        System.out.println("DEBUG - Iniciando sendEmailNotification");
        System.out.println("DEBUG - Comment ID: " + comment.getId());
        System.out.println("DEBUG - Comment Post: " + comment.getPost());
        System.out.println("DEBUG - EmailService: " + emailService);

        try{
            Post post = postService.findById(comment.getPost().getId());

            if (post == null){
                System.err.println("Error: Post not found for comment");
                return;
            }

            // >>>
            String postUrl = "http://localhost:8080/title/" + post.getSlug();
            boolean isReply = comment.getParent() != null;

            System.out.println(">>>>>>>>>>>>>> Sendind e-mail for: " + post.getTitle());

            // String parentAuthor = isReply ? comment.getParent().getUsername() : null;

            emailService.sendSimpleCommentNotification(
                    post.getTitle(),
                    comment.getUsername(),
                    comment.getContent(),
                    postUrl,
                    isReply
            );

            System.out.println(">>>>>>>>>>>>>>>>>> E-mail enviado com sucesso");

        } catch(Exception e) {
            System.err.println("Error to send e-mail notification " + e.getMessage());
            e.printStackTrace();
        }
    }

}
