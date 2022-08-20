package com.example.harmony.domain.gallery.entity;

import com.example.harmony.domain.gallery.dto.GalleryCommentRequest;
import com.example.harmony.domain.user.entity.User;
import com.example.harmony.global.common.TimeStamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
public class GalleryComment extends TimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private String content;

    @ManyToOne
    private Gallery gallery;

    @ManyToOne
    private User user;

    public GalleryComment(GalleryCommentRequest galleryCommentRequest, User user) {
        this.content = galleryCommentRequest.getContent();
        this.user = user;
    }

    public void setGallery(Gallery gallery) {
        this.gallery = gallery;
    }

    public void edit(GalleryCommentRequest galleryCommentRequest) {
        this.content = galleryCommentRequest.getContent();
    }
}
