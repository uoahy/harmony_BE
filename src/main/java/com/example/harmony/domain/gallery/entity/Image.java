package com.example.harmony.domain.gallery.entity;

import com.example.harmony.global.s3.UploadResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url;

    private String filename;

    @ManyToOne
    private Gallery gallery;

    public Image(UploadResponse uploadResponse) {
        this.url = uploadResponse.getUrl();
        this.filename = uploadResponse.getFilename();
    }

    public void setGallery(Gallery gallery) {
        this.gallery = gallery;
    }
}
