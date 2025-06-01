package ru.practicum.ewm.service.category;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.category.CategoryDto;
import ru.practicum.ewm.dto.category.NewCategoryDto;
import ru.practicum.ewm.exceptions.ConflictException;
import ru.practicum.ewm.exceptions.NotFoundException;
import ru.practicum.ewm.mapper.CategoryMapper;
import ru.practicum.ewm.model.Category;
import ru.practicum.ewm.repository.CategoryRepository;
import ru.practicum.ewm.validation.ValidateService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository repository;
    private final ValidateService validateService;
    private final CategoryMapper mapper;

    @Override
    public List<CategoryDto> getAllByFilters(int from, int size) {
        List<Category> categories = repository.findAllByFilters(from, size);
        return mapper.toDto(categories);
    }

    @Override
    public CategoryDto getById(Long catId) {
        Category category = repository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Категория не найдена, id: " + catId));
        return mapper.toDto(category);
    }

    @Transactional
    @Override
    public CategoryDto save(NewCategoryDto newCategory) {
        Category category = repository.save(mapper.toEntity(newCategory));
        return mapper.toDto(category);
    }

    @Transactional
    @Override
    public CategoryDto update(Long catId, NewCategoryDto updatedCategory) {
        Category oldCategory = validateService.checkCategory(catId);
        if (!oldCategory.getName().equals(updatedCategory.getName())) {
            if (repository.findByNameIgnoreCase(updatedCategory.getName()).isPresent()) {
                throw new ConflictException("Категория с указанным именем уже существует");
            }
        }
        Category category = mapper.toEntity(updatedCategory);
        category.setId(catId);
        return mapper.toDto(repository.save(category));
    }

    @Transactional
    @Override
    public void delete(Long catId) {
        validateService.checkCategory(catId);
        repository.deleteById(catId);
    }
}