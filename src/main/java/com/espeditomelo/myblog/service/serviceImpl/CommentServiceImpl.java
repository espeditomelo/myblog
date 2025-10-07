package com.espeditomelo.myblog.service.serviceImpl;

import com.espeditomelo.myblog.model.Comment;
import com.espeditomelo.myblog.model.repository.CommentRepository;
import com.espeditomelo.myblog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    CommentRepository commentRepository;

    @Override
    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public List<Comment> findAll() {
        return commentRepository.findAll();
    }

    @Override
    public List<Comment> getCommentsByPost(Long postId) {
        return commentRepository.findByPostIdAndParentIsNullOrderByCreatedAtAsc(postId);
    }

}
