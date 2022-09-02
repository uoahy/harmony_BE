package com.example.harmony.domain.community.dto;

import com.example.harmony.domain.community.model.PostComment;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
public class PostCommentResponse {

    private Long commentId;

    private String content;

    private LocalDateTime createdAt;

    private Map<String,Object> commenter;

    private boolean isCommenter;

    public PostCommentResponse(PostComment postComment, Map<String, Object> commenter, boolean isCommenter) {
        this.commentId = postComment.getId();
        this.content = postComment.getContent();
        this.createdAt = postComment.getCreatedAt();
        this.commenter = commenter;
        this.isCommenter = isCommenter;
    }
}
