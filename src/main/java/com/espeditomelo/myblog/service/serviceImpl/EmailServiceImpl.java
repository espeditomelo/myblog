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

    @Value("${app.email.admin:g@gmail.com}")
    private String adminEmail;

    @Value("${app.email.notification.enabled:false}")
    private boolean emailNotificationsEnabled;

    @Value("${spring.mail.username:dmi@vinciano.com.br}")
    private String fromEmail;

//    @Override
//    public void sendCommentNotification(String postTitle,
//                                        String commentAuthor,
//                                        String commentContent,
//                                        String postUrl,
//                                        boolean isReply,
//                                        String parentCommentAuthor) {
//
//        if(!emailNotificationsEnabled) {
//            return;
//        }
//
//        try {
//            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
//            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
//            mimeMessageHelper.setTo(adminEmail);
//            mimeMessageHelper.setSubject(" New " + (isReply ? "Reply" : "Comment") + " on blog");
//
//            //conexto para o template thymeleaf
//            Context context = new Context();
//            context.setVariable("postTitle", postTitle);
//            context.setVariable("commentAuthor", commentAuthor);
//            context.setVariable("commentContent", commentContent);
//            context.setVariable("postUrl", postUrl);
//            context.setVariable("isReply", isReply);
//            context.setVariable("parentCommentAuthor", parentCommentAuthor);
//
//            String htmlContent = templateEngine.process("email/comment-notification", context);
//            mimeMessageHelper.setText(htmlContent, true);
//            javaMailSender.send(mimeMessage);
//
//        } catch(MessagingException e) {
//            System.err.println("Error to send e-mail notification " + e.getMessage());
//        }
//    }

    @Override
    public void sendSimpleCommentNotification(String postTitle, String commentAuthor,
                                              String commentContent, String postUrl,
                                              boolean isReply) {

        if(!emailNotificationsEnabled) {
            System.out.println("E-mail notifications desabled");
            return;
        }

        if(javaMailSender == null){
            System.err.println("JavaMailSender not configured");
        }

        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom(fromEmail);
            simpleMailMessage.setTo(adminEmail);
            simpleMailMessage.setSubject("New " + (isReply ? "Reply" : "Comment") + " on blog");
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
                "Autor:" + commentAuthor + "\n" +
                "Content: " + commentContent + "\n" +
                "Link: " + postUrl + "\n\n" +
                "---\nBlog Notification System";
    }
}
