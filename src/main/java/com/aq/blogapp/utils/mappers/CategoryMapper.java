package com.aq.blogapp.utils.mappers;

import com.aq.blogapp.payload.DTO.CategoryDTO;
import com.aq.blogapp.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper
public interface CategoryMapper {

    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    //    @Mapping(target = "blogs", source = "")
    Category categoryDtoToCategory(CategoryDTO categoryDTO);

    CategoryDTO categoryToCategoryDto(Category category);
}
