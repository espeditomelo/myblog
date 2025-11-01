package com.espeditomelo.myblog.service;

public interface EmailService {

//    void sendCommentNotification(String postTitle,
//                                 String commentAuthor,
//                                 String commentContent,
//                                 String postUrl,
//                                 boolean isReply,
//                                 String parentCommentAuthor);

    void sendSimpleCommentNotification(String postTitle,
                                       String commentAuthor,
                                       String commentContent,
                                       String postUrl,
                                       boolean isReply);

}
