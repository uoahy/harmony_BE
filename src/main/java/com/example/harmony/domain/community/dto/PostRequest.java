package com.example.harmony.domain.community.dto;

import lombok.Getter;
import lombok.ToString;

import java.util.List;

@ToString
@Getter
public class PostRequest {

    private String title;

    private String category;

    private String content;

    private List<String> tags;

}
