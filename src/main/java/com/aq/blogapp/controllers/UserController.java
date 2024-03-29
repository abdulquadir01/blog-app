package com.aq.blogapp.controllers;

import com.aq.blogapp.vo.DTO.UserDTO;
import com.aq.blogapp.vo.response.ApiResponse;
import com.aq.blogapp.services.UserService;
import com.aq.blogapp.utils.AppUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;


@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

//    public UserController(UserService userService) {
//        this.userService = userService;
//    }


    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        List<UserDTO> userDTOList = new ArrayList<>();

        try {
            userDTOList = userService.getAllUsers();

            return new ResponseEntity<>(userDTOList, HttpStatus.OK);

        } catch (Exception ex) {

            return new ResponseEntity<>(
                    new ApiResponse(
                            HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                            HttpStatus.INTERNAL_SERVER_ERROR.value()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );

        }

    }


    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserById(@PathVariable Long userId) {
        UserDTO userDTO = new UserDTO();

        try {
            userDTO = userService.getUserById(userId);

            System.out.println(userDTO.toString());

            return new ResponseEntity<>(userDTO, HttpStatus.OK);

        } catch (Exception ex) {

            return new ResponseEntity<>(
                    new ApiResponse(
                            HttpStatus.NOT_FOUND.getReasonPhrase(),
                            HttpStatus.NOT_FOUND.value()),
                    HttpStatus.NOT_FOUND
            );
        }

    }


    @PostMapping
    public ResponseEntity<Object> createUser(@Valid @RequestBody UserDTO userDTO) {
        UserDTO createdUserDTO = new UserDTO();

        try {
            if (!AppUtils.anyEmpty(userDTO)) {
                createdUserDTO = userService.createUser(userDTO);

                return new ResponseEntity<>(createdUserDTO, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(
                        new ApiResponse(
                                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                                HttpStatus.BAD_REQUEST.value()),
                        HttpStatus.BAD_REQUEST
                );
            }

        } catch (Exception ex) {
            return new ResponseEntity<>(
                    new ApiResponse(
                            HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                            HttpStatus.INTERNAL_SERVER_ERROR.value()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }


    @PutMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@PathVariable Long userId, @Valid @RequestBody UserDTO userDTO) {
        UserDTO updatedUser = new UserDTO();

        try {

            updatedUser = userService.updateUser(userId, userDTO);

            return new ResponseEntity<>(updatedUser, HttpStatus.OK);

        } catch (Exception ex) {
            return new ResponseEntity<>(
                    new ApiResponse(
                            "Internal Server Error",
                            HttpStatus.INTERNAL_SERVER_ERROR.value()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

    }


    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable Long userId) {
        try {
            userService.deleteUser(userId);

            return new ResponseEntity<>(
                    new ApiResponse(
                            "User Deleted Successfully",
                            HttpStatus.OK.value()),
                    HttpStatus.OK
            );

        } catch (Exception ex) {
            return new ResponseEntity<>(
                    new ApiResponse(
                            HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                            HttpStatus.INTERNAL_SERVER_ERROR.value()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }


}
