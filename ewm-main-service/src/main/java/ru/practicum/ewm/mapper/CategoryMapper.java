package ru.practicum.ewm.mapper;

import org.mapstruct.Mapper;
import ru.practicum.ewm.dto.category.CategoryDto;
import ru.practicum.ewm.dto.category.NewCategoryDto;
import ru.practicum.ewm.model.Category;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    Category toEntity(NewCategoryDto dto);

    CategoryDto toDto(Category category);

    List<CategoryDto> toDto(List<Category> categories);
}