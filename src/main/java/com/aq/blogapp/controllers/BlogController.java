package com.aq.blogapp.controllers;

import com.aq.blogapp.DTO.BlogDTO;
import com.aq.blogapp.exceptions.ResourceNotFoundException;
import com.aq.blogapp.payload.BlogResponse;
import com.aq.blogapp.services.BlogService;
import com.aq.blogapp.utils.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api")
public class BlogController {

    private final BlogService blogService;

    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }


    @GetMapping("/blogs")
    public ResponseEntity<Object> getAllBlogs(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = "postId", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir) {

        BlogResponse blogResponse = new BlogResponse();
        System.out.println("sortdir :" + sortDir);

        try {
            blogResponse = blogService.getAllBlog(pageNumber, pageSize, sortBy, sortDir);

            return new ResponseEntity<>(blogResponse, HttpStatus.OK);

        } catch (Exception ex) {
            return new ResponseEntity<>(
                    new ApiResponse(
                            HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                            HttpStatus.INTERNAL_SERVER_ERROR.value()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

    }


    @GetMapping("/blogs/{blogId}")
    public ResponseEntity<Object> getBlogById(@PathVariable Long blogId) {
        BlogDTO blogDTOById = new BlogDTO();

        try {
            blogDTOById = blogService.getBlogById(blogId);

            return new ResponseEntity<>(blogDTOById, HttpStatus.OK);

        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(
                    new ApiResponse(
                            HttpStatus.NOT_FOUND.getReasonPhrase(),
                            HttpStatus.NOT_FOUND.value()),
                    HttpStatus.NOT_FOUND
            );
        }

    }


    @GetMapping("/category/{categoryId}/blogs")
    public ResponseEntity<Object> getBlogByCategory(
            @PathVariable Long categoryId,
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = "postId", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir) {

        BlogResponse blogsByCategoryResponse = new BlogResponse();

        try {
            blogsByCategoryResponse = blogService.getBlogsByCategory(categoryId, pageNumber, pageSize, sortBy, sortDir);

            return new ResponseEntity<>(blogsByCategoryResponse, HttpStatus.OK);

        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(
                    new ApiResponse(
                            HttpStatus.NOT_FOUND.getReasonPhrase(),
                            HttpStatus.NOT_FOUND.value()),
                    HttpStatus.NOT_FOUND
            );
        }

    }


    @GetMapping("/user/{userId}/blogs")
    public ResponseEntity<Object> getBlogByUser(
            @PathVariable Long userId,
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = "postId", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir) {

        BlogResponse blogsByUser = new BlogResponse();

        try {
            blogsByUser = blogService.getBlogsByUser(userId, pageNumber, pageSize, sortBy, sortDir);

            return new ResponseEntity<>(blogsByUser, HttpStatus.OK);

        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(
                    new ApiResponse(
                            HttpStatus.NOT_FOUND.getReasonPhrase(),
                            HttpStatus.NOT_FOUND.value()),
                    HttpStatus.NOT_FOUND
            );
        }

    }


    @PostMapping("/user/{userId}/category/{categoryId}/blogs")
    public ResponseEntity<Object> createBlog(@Valid @RequestBody BlogDTO blogDTO,
                                             @PathVariable Long userId, @PathVariable Long categoryId) {
        BlogDTO newBlogDTO = new BlogDTO();

        try {
//            if(!AppUtils.anyEmpty(blogDTO)){
            if (true) {
                newBlogDTO = blogService.createBlog(blogDTO, userId, categoryId);

                return new ResponseEntity<>(newBlogDTO, HttpStatus.CREATED);

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


    @PutMapping("/blogs/{blogId}")
    public ResponseEntity<Object> updateBlog(@PathVariable Long blogId, @Valid @RequestBody BlogDTO blogDTO) {
        BlogDTO updatedBlog = new BlogDTO();

        try {
            updatedBlog = blogService.updateBlog(blogId, blogDTO);

            return new ResponseEntity<>(updatedBlog, HttpStatus.OK);

        } catch (Exception ex) {
            return new ResponseEntity<>(
                    new ApiResponse(
                            "Internal Server Error",
                            HttpStatus.INTERNAL_SERVER_ERROR.value()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

    }


    @DeleteMapping("/blogs/{blogId}")
    public ResponseEntity<Object> deleteBlog(@PathVariable Long blogId) {

        try {
            blogService.deleteBlog(blogId);

            return new ResponseEntity<>(
                    new ApiResponse(
                            "Category Deleted Successfully",
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


    //SEARCH METHOD
    @GetMapping("/blogs/search/{keyword}")
    public ResponseEntity<Object> searchBlogs(@PathVariable String keyword){
        List<BlogDTO> searchedBlogs = new ArrayList<>();

        searchedBlogs = blogService.searchByTitle(keyword);

        return new ResponseEntity<>(searchedBlogs, HttpStatus.OK);
    }


//EoC - End of Class
}
