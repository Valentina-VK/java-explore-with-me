package ru.practicum.ewm.controller.compilation;

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
import ru.practicum.ewm.dto.compilation.CompilationDto;
import ru.practicum.ewm.service.compilation.CompilationService;

import java.util.List;

import static ru.practicum.ewm.util.EwmConstants.DEFAULT_TEN;
import static ru.practicum.ewm.util.EwmConstants.DEFAULT_ZERO;

@RestController
@RequestMapping(path = "/compilations")
@RequiredArgsConstructor
@Slf4j
@Validated
public class CompilationPublicController {
    private final CompilationService compilationService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CompilationDto> getAllByParam(@RequestParam(required = false) Boolean pinned,
                                              @RequestParam(defaultValue = DEFAULT_ZERO) @PositiveOrZero int from,
                                              @RequestParam(defaultValue = DEFAULT_TEN) @Positive int size) {
        log.info("GET Поиск подборок событий по параметрам");
        return compilationService.getCompilations(pinned, from, size);
    }

    @GetMapping("/{compId}")
    public CompilationDto getById(@PathVariable @Positive long compId) {
        log.info("GET Поиск подборки событий по id: {}", compId);
        return compilationService.getById(compId);
    }
}