package com.example.harmony.domain.community.repository;

import com.example.harmony.domain.community.entity.Like;
import com.example.harmony.domain.community.entity.Post;
import com.example.harmony.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByPostAndUser(Post post, User user);
    void deleteLikeByPostAndUser(Post post, User user);

}