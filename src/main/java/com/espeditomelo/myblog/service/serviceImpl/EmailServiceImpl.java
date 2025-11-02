package com.espeditomelo.myblog.service.serviceImpl;

import com.espeditomelo.myblog.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired(required = false)
    private JavaMailSender javaMailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${app.email.admin}")
    private String adminEmail;

    @Value("${app.email.notification.enabled}")
    private boolean emailNotificationsEnabled;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    public void sendSimpleCommentNotification(String postTitle, String commentAuthor,
                                              String commentContent, String postUrl,
                                              boolean isReply) {

        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom(fromEmail);
            simpleMailMessage.setTo(adminEmail);
            simpleMailMessage.setSubject("New " + (isReply ? "Reply" : "Comment") + " on MyBlog");
            String emailText = buildEmailContent(postTitle, commentAuthor, commentContent, postUrl, isReply);
            simpleMailMessage.setText(emailText);
            javaMailSender.send(simpleMailMessage);
            System.out.println(">>>>>>>> E-mail sended successfully to " + adminEmail);
        } catch (Exception e) {
            System.err.println(">>>>>>>>> Error to send e-mail " + e.getClass().getSimpleName() );
            System.err.println(">>>>>>>>> Message: " + e.getMessage());
            if(e.getMessage().contains("550") || e.getMessage().contains("rejected")){
                System.err.println(">>>>>> E-mail rejected.");
            }
        }
    }

    private String buildEmailContent(String postTitle, String commentAuthor, String commentContent,
                                     String postUrl, boolean isReply) {
        return "Details of " + (isReply ? "Reply" : "Comment") + ":\n\n" +
                "Post: " + postTitle + "\n" +
                "Autor: " + commentAuthor + "\n" +
                "Content: " + commentContent + "\n" +
                "Link: " + postUrl + "\n\n" +
                "---\nBlog Notification System";
    }
}
