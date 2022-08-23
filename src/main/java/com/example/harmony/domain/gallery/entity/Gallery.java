package com.example.harmony.domain.gallery.entity;

import com.example.harmony.domain.gallery.dto.GalleryRequest;
import com.example.harmony.domain.schedule.model.Schedule;
import com.example.harmony.domain.user.entity.Family;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.*;
import java.time.LocalDate;
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

    @Lob
    private String content;

    @ManyToOne
    private Schedule schedule;

    private LocalDate date;

    @OneToMany(mappedBy = "gallery")
    private List<Image> images;

    @OneToMany(mappedBy = "gallery")
    private List<GalleryComment> comments;

    @ManyToOne
    private Family family;

    public Gallery(GalleryRequest galleryRequest, List<Image> images, Family family) {
        this.date = galleryRequest.getDate();
        this.title = galleryRequest.getTitle();
        this.content = galleryRequest.getContent();
        this.images = images;
        this.family = family;
        this.comments = null;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public void addImages(List<Image> images) {
        this.images.addAll(images);
        for (Image image : images) {
            image.setGallery(this);
        }
    }

    public void removeImages(List<Image> images) {
        for (Image image : images) {
            if (image.getGallery() != this) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "갤러리에 없는 이미지입니다");
            }
        }
        this.images.removeAll(images);
    }

    public void addComment(GalleryComment galleryComment) {
        comments.add(galleryComment);
        galleryComment.setGallery(this);
    }
}
