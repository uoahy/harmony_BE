package com.example.harmony.domain.gallery.entity;

import com.example.harmony.domain.gallery.dto.GalleryCommentRequest;
import com.example.harmony.domain.user.entity.User;
import com.example.harmony.global.common.TimeStamp;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class GalleryComment extends TimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
}
