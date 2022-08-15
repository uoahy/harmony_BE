package com.example.harmony.domain.community.entity;

import com.example.harmony.domain.community.dto.PostRequest;
import com.example.harmony.domain.user.entity.User;
import com.example.harmony.global.common.TimeStamp;
import com.example.harmony.global.s3.UploadResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

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

    @OneToMany(mappedBy = "post")
    private List<Tag> tags;

    @OneToMany(mappedBy="post")
    private List<Like> likes;
    private String imageUrl;

    private String imageFilename;

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "post")
    List<PostComment> comments;

    public Post(PostRequest postRequest, UploadResponse image, User user) {
        this.title = postRequest.getTitle();
        this.category = postRequest.getCategory();
        this.content = postRequest.getContent();
        this.imageUrl = image.getUrl();
        this.imageFilename = image.getFilename();
        this.user = user;
    }

    public void savePost(PostRequest postRequest, UploadResponse image) {
        this.title = postRequest.getTitle();
        this.category = postRequest.getCategory();
        this.content = postRequest.getContent();
        this.imageUrl = image.getUrl();
        this.imageFilename = image.getFilename();
    }
}
