package ru.practicum.ewm.service.compilation;

import ru.practicum.ewm.dto.compilation.CompilationDto;
import ru.practicum.ewm.dto.compilation.NewCompilationDto;
import ru.practicum.ewm.dto.compilation.UpdateCompilationRequest;

import java.util.List;

public interface CompilationService {

    CompilationDto create(NewCompilationDto newCompilation);

    CompilationDto update(long compId, UpdateCompilationRequest updatedCompilation);

    void delete(long compId);

    CompilationDto getById(long compId);

    List<CompilationDto> getCompilations(Boolean pinned, int from, int size);
}