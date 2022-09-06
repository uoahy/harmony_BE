package com.example.harmony.domain.gallery.dto;

import com.example.harmony.domain.gallery.entity.GalleryComment;
import com.example.harmony.domain.user.entity.User;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class CommentResponse {

    private Long id;

    private String content;

    private String commenter;

    private Boolean isCommenter;

    private LocalDate createdAt;

    public CommentResponse(GalleryComment galleryComment, User user) {
        this.id = galleryComment.getId();
        this.content = galleryComment.getContent();
        this.commenter = galleryComment.getUser().getRole().getRole();
        this.isCommenter = galleryComment.getUser().getId().equals(user.getId());
        this.createdAt = galleryComment.getCreatedAt().toLocalDate();
    }
}
