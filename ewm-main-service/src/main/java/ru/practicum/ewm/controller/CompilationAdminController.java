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
import ru.practicum.ewm.dto.compilation.CompilationDto;
import ru.practicum.ewm.dto.compilation.NewCompilationDto;
import ru.practicum.ewm.dto.compilation.UpdateCompilationRequest;
import ru.practicum.ewm.service.compilation.CompilationService;

@RestController
@RequestMapping(path = "/admin/compilations")
@RequiredArgsConstructor
@Slf4j
@Validated
public class CompilationAdminController {
    private final CompilationService compilationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto createCompilation(@Valid @RequestBody NewCompilationDto newCompilation) {
        log.info("POST Создание подборки событий {}", newCompilation);
        return compilationService.create(newCompilation);
    }

    @PatchMapping("/{compId}")
    @ResponseStatus(HttpStatus.OK)
    public CompilationDto updateCompilation(@PathVariable @Positive long compId,
                                            @Valid @RequestBody UpdateCompilationRequest updatedCompilation) {
        log.info("PATCH Обновление подборки событий {}", updatedCompilation);
        return compilationService.update(compId, updatedCompilation);
    }


    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable @Positive long compId) {
        log.info("DELETE Удаление подборки событий id: {}", compId);
        compilationService.delete(compId);
    }
}