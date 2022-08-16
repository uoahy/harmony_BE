package com.example.harmony.domain.community.dto;

import com.example.harmony.domain.community.entity.Post;
import com.example.harmony.domain.community.entity.Tag;
import com.example.harmony.domain.user.entity.Family;
import com.example.harmony.domain.user.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public Map<String,Object> userInfo(User user, Family family) {
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("level",family.getLevel());
        userInfo.put("flower", family.isFlower());
        userInfo.put("nickname", user.getNickname());
        return userInfo;
    }

}
