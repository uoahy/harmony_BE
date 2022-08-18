package com.example.harmony.domain.schedule.model;

import lombok.Getter;

@Getter
public enum Category {

    EAT_OUT("외식"),
    TRIP("여행"),
    COOK("요리"),
    CLEAN("청소"),
    ETC("기타"),
    PERSONAL("개인");

    private String title;

    Category(String title) {
        this.title = title;
    }
}
