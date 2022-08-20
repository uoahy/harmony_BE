package com.example.harmony.domain.gallery.repository;

import com.example.harmony.domain.gallery.entity.Gallery;
import com.example.harmony.domain.gallery.entity.Image;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class ImageRepositoryTest {

    @Autowired
    TestEntityManager em;

    @Autowired
    ImageRepository imageRepository;

    @Test
    @DisplayName("갤러리 이미지 개수 조회")
    void countByGalleryId() {
        // given
        Gallery gallery = Gallery.builder().build();

        Image image1 = Image.builder()
                .gallery(gallery)
                .build();

        Image image2 = Image.builder()
                .gallery(gallery)
                .build();

        Image image3 = Image.builder()
                .gallery(gallery)
                .build();

        Image image4 = Image.builder()
                .gallery(gallery)
                .build();

        em.persist(gallery);
        em.persist(image1);
        em.persist(image2);
        em.persist(image3);
        em.persist(image4);

        // when
        long imageCount = imageRepository.countByGalleryId(gallery.getId());

        // then
        assertEquals(4, imageCount);
    }
}