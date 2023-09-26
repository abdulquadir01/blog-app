package com.aq.blogapp.vo.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class UserRequest {
    @NotEmpty
    @Size(min = 3, message = "First Name must be at least 3 characters")
    private String firstName;

    @NotEmpty
    @Size(min = 3, message = "Last Name must be at least 3 characters")
    private String lastName;

    @Email(message = "Not a valid email address")
    @Pattern(regexp = "^[\\w.%+-]+@[a-zA-Z]{2,20}+\\.[a-zA-Z]{2,6}$", message = "Please provide a valid email address")
    private String email;

    @NotEmpty
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
            message = "Password must contain 1 upper case, 1 lower case, 1 number, 1 special character")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    @NotEmpty
    @Size(min = 50, message = "About should not be less than 50 characters")
    private String about;

}
