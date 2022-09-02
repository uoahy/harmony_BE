package com.example.harmony.domain.community.dto;

import com.example.harmony.domain.community.model.Post;
import com.example.harmony.domain.community.model.Tag;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
public class PostResponse {

    private String title;

    private String category;

    private String content;

    private List<String> tags;

    private String imageUrl;

    private Map<String,Object> poster;

    private boolean iamPoster;

    private LocalDateTime createdAt;

    private List<PostCommentResponse> comments;

    private int likeCount;

    private boolean like;

    public PostResponse(Post post,Map<String,Object> poster, boolean iamPoster, List<PostCommentResponse> comments, boolean like) {
      this.title = post.getTitle();
      this.category = post.getCategory();
      this.content = post.getContent();
      this.tags = post.getTags().stream()
              .map(Tag::getTag)
              .collect(Collectors.toList());
      this.imageUrl = post.getImageUrl();
      this.poster= poster;
      this.iamPoster = iamPoster;
      this.createdAt = post.getCreatedAt();
      this.comments = comments;
      this.likeCount = post.getLikes().size();
      this.like = like;
    }
}
