package com.example.harmony.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class MembersResponse {

    private long userId;
    private String name;
    private String role;

}
