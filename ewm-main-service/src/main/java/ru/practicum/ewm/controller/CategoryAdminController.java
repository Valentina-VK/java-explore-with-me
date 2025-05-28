package ru.practicum.ewm.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.dto.category.CategoryDto;
import ru.practicum.ewm.dto.category.NewCategoryDto;
import ru.practicum.ewm.service.category.CategoryService;

@RestController
@RequestMapping(path = "/admin/categories")
@RequiredArgsConstructor
@Slf4j
@Validated
public class CategoryAdminController {
    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto saveCategory(@RequestBody @Valid NewCategoryDto newCategory) {
        log.info("POST Создание категории {}", newCategory);
        return categoryService.save(newCategory);
    }

    @PatchMapping("/{catId}")
    public CategoryDto updateCategory(@PathVariable @Positive Long catId,
                                      @RequestBody @Valid NewCategoryDto updatedCategory) {
        log.info("PATCH Обновление категории id {}, новые данные {}", catId, updatedCategory);
        return categoryService.update(catId, updatedCategory);
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @Positive Long catId) {
        log.info("DELETE удаление категории по id {}", catId);
        categoryService.delete(catId);
    }
}

