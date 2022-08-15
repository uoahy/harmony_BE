package com.example.harmony.domain.community.service;

import com.example.harmony.domain.community.dto.PostCommentResponse;
import com.example.harmony.domain.community.dto.PostRequest;
import com.example.harmony.domain.community.dto.PostResponse;
import com.example.harmony.domain.community.entity.Post;
import com.example.harmony.domain.community.entity.PostComment;
import com.example.harmony.domain.community.entity.Tag;
import com.example.harmony.domain.community.repository.LikeRepository;
import com.example.harmony.domain.community.repository.PostRepository;
import com.example.harmony.domain.community.repository.TagRepository;
import com.example.harmony.domain.user.entity.Family;
import com.example.harmony.domain.user.entity.User;
import com.example.harmony.global.s3.S3Service;
import com.example.harmony.global.s3.UploadResponse;
import com.example.harmony.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final LikeRepository likeRepository;
    private final S3Service s3Service;

    // 게시글 작성
    public void createPost(MultipartFile image, PostRequest request, UserDetailsImpl userDetails) {
        // 이미지 저장
        UploadResponse savedImage = s3Service.uploadFile(image);

        // 게시글 객체 저장
        Post post = new Post(request, savedImage, userDetails.getUser());
        postRepository.save(post);

        // 태그 저장
        List<String> tags = request.getTags();
        for (String tag : tags) {
            tagRepository.save(new Tag(tag, post));
        }
    }

    // 게시글 조회
    public PostResponse getPost(Long postId, UserDetailsImpl userDetails) {
        // 게시글
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글이 존재하지 않습니다.")
        );

        // 게시글 작성자 일치여부
        User writer = post.getUser();
        boolean isPoster = writer.equals(userDetails.getUser());

        // 댓글
        List<PostComment> comments = post.getComments();
        List<PostCommentResponse> commentResponseList = new ArrayList<>();
        for(PostComment comment: comments) {
            // 댓글 작성자
            User cmtWriter = comment.getUser();
            Map<String,Object> commenter = userInfo(cmtWriter, cmtWriter.getFamily());

            // 댓글 작성자 일치여부
            boolean isCommenter = cmtWriter.equals(userDetails.getUser());
            commentResponseList.add(new PostCommentResponse(comment, commenter, isCommenter));
        }

        // 좋아요
        boolean like = likeRepository.findByPostAndUser(post, userDetails.getUser()).isPresent();

        return new PostResponse(post, isPoster, commentResponseList, like);
    }

    // 게시글 목록 조회
    public Map<String,Object> getPosts(String category, int page, int size) {
        // 카테고리 유효성 검사
        Set<String> categories = new HashSet<>(Arrays.asList("아빠","엄마","첫째","둘째","N째","막내","외동","동거인"));
        if(!categories.contains(category)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"유효하지 않은 카테고리입니다.");
        }

        Pageable pageable = PageRequest.of(page,size, Sort.Direction.DESC);
        Slice<Post> posts = postRepository.findAllByCategory(category, pageable);
        Map<String,Object> response = new HashMap<>();
        response.put("posts",posts.map(PostResponse::new));
        response.put("last",posts.isLast());

        return response;
    }

    // 게시글 수정
    public void putPost(Long postId, MultipartFile image, PostRequest request, User user) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시물이 존재하지 않습니다.")
        );

        // 게시글 작성자 여부
        if (!user.equals(post.getUser())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "게시글 작성자가 아닙니다.");
        }

        // 기존 이미지 삭제 및 새 이미지 저장
        String previous = post.getImageFilename();
        List<String> previousFile = new ArrayList<>();
        previousFile.add(previous);
        s3Service.deleteFiles(previousFile);
        UploadResponse savedImage = s3Service.uploadFile(image);

        // 게시글 수정내용 저장
        post.savePost(request, savedImage);
        postRepository.save(post);

        // 기존 태그 삭제 및 새 태그 저장
        tagRepository.deleteAllByPost(post);
        List<String> tags = request.getTags();
        for (String tag : tags) {
            tagRepository.save(new Tag(tag, post));
        }
    }

    // 게시글 삭제
    public void deletePost(Long postId, User user) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시물이 존재하지 않습니다.")
        );

        // 게시글 작성자 여부
        if (!post.getUser().equals(user)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "게시글 작성자가 아닙니다.");
        }
        postRepository.deleteById(postId);
    }

    // 작성자 공통양식
    public Map<String,Object> userInfo(User user, Family family) {
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("level",family.getLevel());
        userInfo.put("flower", family.isFlower());
        userInfo.put("nickname", user.getNickname());
        return userInfo;
    }

}

