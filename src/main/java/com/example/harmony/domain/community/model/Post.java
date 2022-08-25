package com.example.harmony.domain.community.model;

import com.example.harmony.domain.community.dto.PostRequest;
import com.example.harmony.domain.user.entity.User;
import com.example.harmony.global.common.TimeStamp;
import com.example.harmony.global.s3.UploadResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@Builder
@NoArgsConstructor
@Getter
@Entity
public class Post extends TimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String category;

    private String content;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<Tag> tags;

    @OneToMany(mappedBy="post", cascade = CascadeType.REMOVE)
    private List<Like> likes;

    private String imageUrl;

    private String imageFilename;

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    List<PostComment> comments;

    public Post(PostRequest postRequest, UploadResponse image, User user) {
        this.title = postRequest.getTitle();
        this.category = postRequest.getCategory();
        this.content = postRequest.getContent();
        this.imageUrl = image.getUrl();
        this.imageFilename = image.getFilename();
        this.user = user;
    }

    public Post(PostRequest postRequest, User user) {
        this.title = postRequest.getTitle();
        this.category = postRequest.getCategory();
        this.content = postRequest.getContent();
        this.user = user;
    }

    public void savePostAndImage(PostRequest postRequest, UploadResponse image) {
        this.title = postRequest.getTitle();
        this.category = postRequest.getCategory();
        this.content = postRequest.getContent();
        this.imageUrl = image.getUrl();
        this.imageFilename = image.getFilename();
    }

    public void savePost(PostRequest postRequest) {
        this.title = postRequest.getTitle();
        this.imageUrl = null;
        this.imageFilename = null;
        this.category = postRequest.getCategory();
        this.content = postRequest.getContent();
    }
}
