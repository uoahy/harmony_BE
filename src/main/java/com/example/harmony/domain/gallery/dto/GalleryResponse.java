package com.example.harmony.domain.gallery.dto;

import com.example.harmony.domain.gallery.entity.Gallery;
import com.example.harmony.domain.gallery.entity.Image;
import com.example.harmony.domain.user.entity.User;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class GalleryResponse {

    private Long id;

    private String title;

    private LocalDate date;

    private List<String> imageUrls;

    private List<CommentResponse> comments;

    public GalleryResponse(Gallery gallery, User user) {
        this.id = gallery.getId();
        this.title = gallery.getTitle();
        this.date = gallery.getDate();
        this.imageUrls = gallery.getImages().stream()
                .limit(5)
                .map(Image::getUrl)
                .collect(Collectors.toList());
        this.comments = gallery.getComments().stream()
                .map(x -> new CommentResponse(x, user))
                .collect(Collectors.toList());
    }
}
