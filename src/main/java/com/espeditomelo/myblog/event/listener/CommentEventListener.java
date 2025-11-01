package com.espeditomelo.myblog.event.listener;

import com.espeditomelo.myblog.model.event.CommentCreatedEvent;
import com.espeditomelo.myblog.service.EmailService;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class CommentEventListener {

    private EmailService emailService;

    @Async
    @EventListener
    public void handleCommentCreated(CommentCreatedEvent commentCreatedEvent) {
        boolean isReply = commentCreatedEvent.getComment().getParent() != null;


    }

}
