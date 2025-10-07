package com.espeditomelo.myblog.service;

import com.espeditomelo.myblog.model.Comment;

import java.util.List;


public interface CommentService {
    Comment save(Comment comment);
    List<Comment> findAll();
    List<Comment> getCommentsByPost(Long id);
}
