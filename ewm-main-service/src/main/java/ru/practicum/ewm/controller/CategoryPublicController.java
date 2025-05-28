package ru.practicum.ewm.controller;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.dto.category.CategoryDto;
import ru.practicum.ewm.service.category.CategoryService;

import java.util.List;

@RestController
@RequestMapping(path = "/categories")
@RequiredArgsConstructor
@Slf4j
@Validated
public class CategoryPublicController {
    private final CategoryService categoryService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CategoryDto> getCategories(@RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                           @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("GET получение категорий по фильтрам от {}, количеством {}", from, size);
        return categoryService.getAllByFilters(from, size);
    }

    @GetMapping("/{catId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto getCategoryById(@PathVariable @Positive Long catId) {
        log.info("GET получение категории по id {}", catId);
        return categoryService.getById(catId);
    }
}

