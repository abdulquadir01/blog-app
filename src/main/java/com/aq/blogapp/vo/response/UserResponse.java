package com.aq.blogapp.vo.response;

import lombok.*;

import java.util.HashSet;
import java.util.Set;


@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class UserResponse {

    private Long userId;

    private String firstName;

    private String lastName;

    private String email;

    private String about;

    private Set<RoleResponse> roles = new HashSet<>();

}
