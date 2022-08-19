package com.example.harmony.domain.community.dto;

import com.example.harmony.domain.community.entity.Post;
import com.example.harmony.domain.community.entity.Tag;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.harmony.domain.community.service.PostService.userInfo;

@Getter
@Setter
public class PostListResponse {

    private Long postId;

    private String title;

    private String content;

    private List<String> tags;

    private String imageUrl;

    private Map<String,Object> poster;

    private LocalDateTime createdAt;

    private int commentCount;

    private int likeCount;

    public PostListResponse(Post post) {
        this.postId = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.tags = post.getTags().stream()
                .map(Tag::getTag)
                .collect(Collectors.toList());
        this.imageUrl = post.getImageUrl();
        this.poster = userInfo(post.getUser(),post.getUser().getFamily());
        this.createdAt = post.getCreatedAt();
        this.commentCount = post.getComments().size();
        this.likeCount = post.getLikes().size();
    }

}
