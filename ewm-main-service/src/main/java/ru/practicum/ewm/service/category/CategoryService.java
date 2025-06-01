package ru.practicum.ewm.service.category;

import ru.practicum.ewm.dto.category.CategoryDto;
import ru.practicum.ewm.dto.category.NewCategoryDto;

import java.util.List;

public interface CategoryService {
    List<CategoryDto> getAllByFilters(int from, int size);

    CategoryDto getById(Long catId);

    CategoryDto save(NewCategoryDto newCategory);

    CategoryDto update(Long catId, NewCategoryDto updatedCategory);

    void delete(Long catId);
}