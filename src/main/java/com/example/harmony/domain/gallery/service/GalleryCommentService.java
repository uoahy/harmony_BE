package com.example.harmony.domain.gallery.service;

import com.example.harmony.domain.gallery.dto.GalleryCommentRequest;
import com.example.harmony.domain.gallery.entity.Gallery;
import com.example.harmony.domain.gallery.entity.GalleryComment;
import com.example.harmony.domain.gallery.repository.GalleryCommentRepository;
import com.example.harmony.domain.gallery.repository.GalleryRepository;
import com.example.harmony.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class GalleryCommentService {

    private final GalleryRepository galleryRepository;

    private final GalleryCommentRepository galleryCommentRepository;

    @Transactional
    public void writeGalleryComment(Long galleryId, GalleryCommentRequest galleryCommentRequest, User user) {
        Gallery gallery = galleryRepository.findById(galleryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "갤러리를 찾을 수 없습니다"));
        if (!gallery.getFamily().getId().equals(user.getFamily().getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "댓글 작성 권한이 없습니다");
        }
        GalleryComment galleryComment = new GalleryComment(galleryCommentRequest, user);
        gallery.addComment(galleryComment);
        galleryCommentRepository.save(galleryComment);
        user.getFamily().plusScore(5);
    }

    public void editGalleryComment(Long galleryCommentId, GalleryCommentRequest galleryCommentRequest, User user) {
        GalleryComment galleryComment = galleryCommentRepository.findById(galleryCommentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "갤러리 댓글을 찾을 수 없습니다"));
        if (!galleryComment.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "댓글 수정 권한이 없습니다");
        }
        galleryComment.edit(galleryCommentRequest);
        galleryCommentRepository.save(galleryComment);
    }

    @Transactional
    public void deleteGalleryComment(Long galleryCommentId, User user) {
        GalleryComment galleryComment = galleryCommentRepository.findById(galleryCommentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "갤러리 댓글을 찾을 수 없습니다"));
        if (!galleryComment.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "댓글 삭제 권한이 없습니다");
        }
        galleryCommentRepository.deleteById(galleryCommentId);
        user.getFamily().minusScore(5);
    }
}
