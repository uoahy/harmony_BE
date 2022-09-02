package com.example.harmony.domain.community.repository;

import com.example.harmony.domain.community.model.Post;
import com.example.harmony.domain.community.model.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostCommentRepository extends JpaRepository<PostComment, Long> {

    Optional<PostComment> findById(Long id);
    List<PostComment> findAllByPostOrderByCreatedAtAsc(Post post);
}