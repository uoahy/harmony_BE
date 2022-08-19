package com.example.harmony.domain.gallery.entity;

import com.example.harmony.domain.user.entity.Family;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
public class Gallery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    @OneToMany(mappedBy = "gallery")
    private List<Image> images;

    @OneToMany(mappedBy = "gallery")
    private List<GalleryComment> comments;

    @ManyToOne
    private Family family;

    public void addImages(List<Image> images) {
        this.images.addAll(images);
        for (Image image : images) {
            image.setGallery(this);
        }
    }

    public void removeImages(List<Image> images) {
        this.images.removeAll(images);
    }

    public void addComment(GalleryComment galleryComment) {
        comments.add(galleryComment);
        galleryComment.setGallery(this);
    }
}
