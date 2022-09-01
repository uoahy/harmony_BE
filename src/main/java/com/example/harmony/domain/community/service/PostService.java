package com.example.harmony.domain.community.service;

import com.example.harmony.domain.community.dto.PostCommentResponse;
import com.example.harmony.domain.community.dto.PostListResponse;
import com.example.harmony.domain.community.dto.PostRequest;
import com.example.harmony.domain.community.dto.PostResponse;
import com.example.harmony.domain.community.model.Post;
import com.example.harmony.domain.community.model.PostComment;
import com.example.harmony.domain.community.model.Tag;
import com.example.harmony.domain.community.repository.LikeRepository;
import com.example.harmony.domain.community.repository.PostCommentRepository;
import com.example.harmony.domain.community.repository.PostRepository;
import com.example.harmony.domain.community.repository.TagRepository;
import com.example.harmony.domain.user.entity.Family;
import com.example.harmony.domain.user.entity.User;
import com.example.harmony.global.s3.S3Service;
import com.example.harmony.global.s3.UploadResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.*;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final PostCommentRepository commentRepository;
    private final TagRepository tagRepository;
    private final LikeRepository likeRepository;
    private final S3Service s3Service;

    // 게시글 작성
    public String createPost(PostRequest request,User user) {
        validCategory(request.getCategory());
        MultipartFile image = request.getImage();

        // 이미지 존재여부에 따른 게시글 객체 저장
        if(image==null) {
            Post post = new Post(request,user);
            postRepository.save(post);
            saveTag(request, post);
        } else {
            UploadResponse savedImage = s3Service.uploadFile(image);
            Post post = new Post(request, savedImage, user);
            postRepository.save(post);
            saveTag(request, post);
        }
        return "게시글 작성이 완료되었습니다.";
    }

    // 게시글 조회
    public PostResponse getPost(Long postId, User user) {
        // 게시글
        Post post = findByPostId(postId);

        // 게시글 작성자 일치여부
        User writer = post.getUser();
        boolean isPoster = writer.getId().equals(user.getId());

        // 작성자 정보
        Map<String, Object> poster = userInfo(writer, writer.getFamily());

        // 댓글
        List<PostComment> comments = commentRepository.findAllByPostContainingOrderByCreatedAtDesc(post);
        List<PostCommentResponse> commentResponseList = new ArrayList<>();
        for(PostComment comment: comments) {
            // 댓글 작성자
            User cmtWriter = comment.getUser();
            Map<String,Object> commenter = userInfo(cmtWriter, cmtWriter.getFamily());

            // 댓글 작성자 일치여부
            boolean isCommenter = cmtWriter.getId().equals(user.getId());
            commentResponseList.add(new PostCommentResponse(comment, commenter, isCommenter));
        }

        // 좋아요
        boolean like = likeRepository.findByPostAndUser(post,user).isPresent();

        return new PostResponse(post, poster, isPoster, commentResponseList, like);
    }

    // 게시글 목록 조회
    public Slice<PostListResponse> getPosts(String category, int page, int size) {
        Pageable pageable = PageRequest.of(page,size);
        validCategory(category);

        Slice<Post> posts;
        if(category.equals("전체")) {
            posts = postRepository.findAllByOrderByCreatedAtDesc(pageable);
        } else {
            posts = postRepository.findAllByCategoryContainingOrderByCreatedAtDesc(category, pageable);
        }

        return posts.map(PostListResponse::new);
    }

    // 게시글 수정
    @Transactional
    public String putPost(Long postId, PostRequest request, User user) {
        Post post = findByPostId(postId);
        validCategory(request.getCategory());

        // 게시글 작성자 일치여부
        getAuthority(post.getUser(),user);

        // 기존 이미지 존재할 경우 삭제
        if(post.getImageUrl()!=null) {
            List<String> previousFile = new ArrayList<>();
            previousFile.add(post.getImageFilename());
            s3Service.deleteFiles(previousFile);
        }

        // 이미지 존재여부
        MultipartFile image = request.getImage();
        if(image==null) {
            post.savePost(request);
            postRepository.save(post);
        } else {
            UploadResponse savedImage = s3Service.uploadFile(image);
            post.savePostAndImage(request, savedImage);
            postRepository.save(post);
        }

        // 기존 태그 삭제 및 새 태그 저장
        tagRepository.deleteAllByPost(post);
        saveTag(request, post);

        return "게시글 수정이 완료되었습니다.";
    }

    // 게시글 삭제
    @Transactional
    public String deletePost(Long postId, User user) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시물이 존재하지 않습니다.")
        );

        // 게시글 작성자 일치여부
        getAuthority(post.getUser(),user);

        // 이미지 존재할 경우 삭제
        if(post.getImageUrl()!=null) {
            List<String> image = new ArrayList<>();
            image.add(post.getImageFilename());
            s3Service.deleteFiles(image);
        }
        postRepository.deleteById(postId);

        return "게시글 삭제가 완료되었습니다.";
    }

    // 작성자 공통양식
    public static Map<String, Object> userInfo(User user, Family family) {
        // 레벨 측정
        int score = family.getTotalScore();
        int level;
        if (score > 2999) {
            level = 4;
        } else if (score > 769) {
            level = 3;
        } else if (score > 219) {
            level = 2;
        } else if (score > 54) {
            level = 1;
        } else {
            level = 0;
        }

        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("level", level);
        userInfo.put("flower", family.isFlower());
        userInfo.put("nickname", user.getNickname());
        return userInfo;
    }

    // 태그 저장
    public void saveTag(PostRequest request, Post post) {
        List<String> tags = request.getTags();
        for (String tag : tags) {
            tagRepository.save(new Tag(tag, post));
        }
    }

    // 작성자 일치여부
    public void getAuthority(User writer, User user) {
        if (!writer.getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "게시글에 대한 권한이 없습니다.");
        }
    }

    // 게시글 불러오기
    public Post findByPostId(Long postId) {
        return postRepository.findById(postId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시물이 존재하지 않습니다.")
        );
    }

    // 카테고리 유효성검사
    public void validCategory(String category) {
        Set<String> categories = new HashSet<>(Arrays.asList("아빠","엄마","첫째","둘째","N째","막내","외동","동거인","전체"));
        if(!categories.contains(category)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"유효하지 않은 카테고리입니다.");
        }
    }

}
