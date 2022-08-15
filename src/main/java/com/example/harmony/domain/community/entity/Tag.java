package com.example.harmony.domain.community.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tag;

    @ManyToOne
    private Post post;

    public Tag(String tag, Post post) {
        this.tag = tag;
        this.post = post;
    }

}
