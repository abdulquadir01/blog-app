package com.aq.blogapp.services.impl;

import com.aq.blogapp.vo.DTO.BlogDTO;
import com.aq.blogapp.exceptions.ResourceNotFoundException;
import com.aq.blogapp.utils.mappers.BlogMapper;
import com.aq.blogapp.entity.Blog;
import com.aq.blogapp.entity.Category;
import com.aq.blogapp.entity.User;
import com.aq.blogapp.vo.response.BlogResponse;
import com.aq.blogapp.respositories.BlogRepository;
import com.aq.blogapp.respositories.CategoryRepository;
import com.aq.blogapp.respositories.UserRepository;
import com.aq.blogapp.services.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BlogServiceImpl implements BlogService {

    private final BlogRepository blogRepository;
    private final BlogMapper blogMapper;

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

//    public BlogServiceImpl(
//            BlogRepository blogRepository,
//            BlogMapper blogMapper,
//            UserRepository userRepository,
//            CategoryRepository categoryRepository
//    ) {
//        this.blogRepository = blogRepository;
//        this.blogMapper = blogMapper;
//        this.userRepository = userRepository;
//        this.categoryRepository = categoryRepository;
//    }


    @Override
    public BlogResponse getAllBlog(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {

        Pageable pageable = createSortedPageable(pageNumber, pageSize, sortBy, sortDir);

        Page<Blog> blogPage = blogRepository.findAll(pageable);

        return createBlogResponse(blogPage);
    }

    @Override
    public BlogDTO getBlogById(Long id) {
        BlogDTO blogDTO = new BlogDTO();

        try {
            if (id != null) {
                blogDTO = blogRepository
                        .findById(id)
                        .map(blogMapper::blogToBlogDto)
                        .orElseThrow(() -> new ResourceNotFoundException("Blog", "BlogId", id));
            }
        } catch (NoSuchElementException ex) {
            throw new ResourceNotFoundException("Blog", "BlogId", id);
        }

        return blogDTO;
    }


    @Override
    public BlogDTO createBlog(BlogDTO blogDTO, Long userId, Long categoryId) {
        BlogDTO newBlogDTO = new BlogDTO();
        Blog createdBlog = new Blog();
        Blog newBlog = new Blog();

        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formattedDate = localDateTime.format(dateTimeFormatter);
        System.out.println("Formatted Date"+ formattedDate);


        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));
        Category category = categoryRepository
                .findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

        newBlog = blogMapper.blogDtoToBlog(blogDTO);
        newBlog.setImageName("default.png");
        newBlog.setBloggedDate(formattedDate);
        newBlog.setUser(user);
        newBlog.setCategory(category);

        createdBlog = blogRepository.save(newBlog);

        newBlogDTO = blogMapper.blogToBlogDto(createdBlog);
        System.out.println("new blogdto: "+newBlogDTO.toString());

        return newBlogDTO;
    }

    @Override
    public BlogDTO updateBlog(Long id, BlogDTO blogDTO) {
        BlogDTO updatedBlog = new BlogDTO();
        Blog savedBlog = new Blog();

        try {

            Blog exitingBlog = blogRepository
                    .findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Blog", "blogId", id));

            exitingBlog.setTitle(blogDTO.getTitle());
            exitingBlog.setContent(blogDTO.getContent());
            exitingBlog.setImageName(blogDTO.getImageName());

            savedBlog = blogRepository.save(exitingBlog);

            updatedBlog = blogMapper.blogToBlogDto(savedBlog);


        } catch (Exception ex) {
            throw new ResourceNotFoundException("Blog", "blogId", id);
        }

        return updatedBlog;
    }


    @Override
    public BlogResponse getBlogsByCategory(Long categoryId, Integer pageNumber, Integer pageSize,
                                           String sortBy, String sortDir) {

        Pageable pageable = createSortedPageable(pageNumber, pageSize, sortBy, sortDir);

        Category category = categoryRepository
                .findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

        Page<Blog> blogPage = blogRepository.findAllByCategory(category, pageable);

        return createBlogResponse(blogPage);
    }


    @Override
    public BlogResponse getBlogsByUser(Long userId, Integer pageNumber, Integer pageSize,
                                       String sortBy, String sortDir) {

        Pageable pageable = createSortedPageable(pageNumber, pageSize, sortBy, sortDir);

        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));

        Page<Blog> blogPage = blogRepository
                .findAllByUser(user, pageable);

        return createBlogResponse(blogPage);
    }


    @Override
    public void deleteBlog(Long id) {

        BlogDTO deleteBlog = new BlogDTO();

        deleteBlog = blogRepository
                .findById(id)
                .map(BlogMapper.INSTANCE::blogToBlogDto)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", id));

        blogRepository.deleteById(id);

    }

    //  TBD - improve the search() for a better search result.
    @Override
    public List<BlogDTO> searchByTitle(String keywords) {
        List<BlogDTO> searchedBlogs = new ArrayList<>();

        searchedBlogs = blogRepository
                .findByTitleContaining(keywords)
                .stream().map(blogMapper::blogToBlogDto)
                .collect(Collectors.toList());

        return searchedBlogs;
    }


    //  ==================================================================
//    PRIVATE METHODS
    private BlogResponse createBlogResponse(Page<Blog> blogPage) {

        BlogResponse blogResponse = new BlogResponse();

        List<BlogDTO> blogs;
        blogs = blogPage
                .getContent()
                .stream()
                .map(blogMapper::blogToBlogDto)
                .collect(Collectors.toList());

        blogResponse.setBlogs(blogs);
        blogResponse.setPageNumber(blogPage.getNumber());
        blogResponse.setPageSize(blogPage.getSize());
        blogResponse.setTotalPages(blogPage.getTotalPages());
        blogResponse.setTotalElements(blogPage.getTotalElements());
        blogResponse.setLastPage(blogPage.isLast());

        return blogResponse;
    }


    private Pageable createSortedPageable(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {

//      Be cautious of this statement
//      !! CAUTION !! TBD - find a way to initialize sort with some other value than null
        Sort sort = null;

        if (sortDir.equalsIgnoreCase("asc")) {
            sort = Sort.by(sortBy).ascending();
        } else if (sortDir.equalsIgnoreCase("desc")) {
            sort = Sort.by(sortBy).descending();
        }

//      !! CAUTION !! TBD - find a way to initialize sort with some other value than null
        return PageRequest.of(pageNumber, pageSize, sort);
    }


//EoC - End of Class
}
