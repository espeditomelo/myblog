package com.espeditomelo.myblog.model.event;

import com.espeditomelo.myblog.model.Comment;

public class CommentCreatedEvent {

    private final Comment comment;
    private final String postUrl;

    public CommentCreatedEvent(Comment comment, String postUrl) {
        this.comment = comment;
        this.postUrl = postUrl;
    }

    public Comment getComment() {
        return comment;
    }
    public String getPostUrl() {
        return postUrl;
    }
}
