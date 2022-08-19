package com.example.harmony.domain.community.repository;

import com.example.harmony.domain.community.model.Post;
import com.example.harmony.domain.community.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long> {

    List<Tag> findAllByPost(Post post);
    void deleteAllByPost(Post post);
}