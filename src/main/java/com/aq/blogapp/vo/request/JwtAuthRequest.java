package com.aq.blogapp.vo.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;


@Data
@Builder
public class JwtAuthRequest {
    private String username;
    private String password;
}
