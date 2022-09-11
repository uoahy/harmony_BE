package com.example.harmony.domain.gallery.repository;

import com.example.harmony.domain.gallery.entity.Gallery;
import com.example.harmony.domain.gallery.entity.GalleryComment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class GalleryCommentRepositoryTest {

    @Autowired
    TestEntityManager em;

    @Autowired
    GalleryCommentRepository galleryCommentRepository;

    @Test
    @DisplayName("갤러리 댓글 개수 조회")
    void countByGalleryId() {
        // given
        Gallery gallery = Gallery.builder().build();

        GalleryComment comment1 = GalleryComment.builder()
                .gallery(gallery)
                .build();

        GalleryComment comment2 = GalleryComment.builder()
                .gallery(gallery)
                .build();
        GalleryComment comment3 = GalleryComment.builder()
                .gallery(gallery)
                .build();

        GalleryComment comment4 = GalleryComment.builder()
                .gallery(gallery)
                .build();

        GalleryComment comment5 = GalleryComment.builder()
                .gallery(gallery)
                .build();

        em.persist(gallery);
        em.persist(comment1);
        em.persist(comment2);
        em.persist(comment3);
        em.persist(comment4);
        em.persist(comment5);

        // when
        long commentCount = galleryCommentRepository.countByGalleryId(gallery.getId());

        // then
        assertEquals(5, commentCount);
    }
}