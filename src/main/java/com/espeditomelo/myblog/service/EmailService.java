package com.espeditomelo.myblog.service;

public interface EmailService {

    void sendSimpleCommentNotification(String postTitle,
                                       String commentAuthor,
                                       String commentContent,
                                       String postUrl,
                                       boolean isReply);

}
