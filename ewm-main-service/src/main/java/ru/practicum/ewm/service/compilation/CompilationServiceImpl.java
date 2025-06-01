package ru.practicum.ewm.service.compilation;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.compilation.CompilationDto;
import ru.practicum.ewm.dto.compilation.NewCompilationDto;
import ru.practicum.ewm.dto.compilation.UpdateCompilationRequest;
import ru.practicum.ewm.exceptions.NotFoundException;
import ru.practicum.ewm.mapper.CompilationMapper;
import ru.practicum.ewm.model.Compilation;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.QCompilation;
import ru.practicum.ewm.repository.CompilationRepository;
import ru.practicum.ewm.repository.EventRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository repository;
    private final EventRepository eventRepository;
    private final CompilationMapper mapper;
    private final EntityManager entityManager;

    @Transactional
    @Override
    public CompilationDto create(NewCompilationDto newCompilation) {
        List<Event> events = new ArrayList<>();
        if (newCompilation.getEvents() != null) {
            events = eventRepository.findAllById(newCompilation.getEvents());
        }
        return mapper.toDto(repository.save(mapper.toEntity(newCompilation, events)));
    }

    @Transactional
    @Override
    public CompilationDto update(long compId, UpdateCompilationRequest updatedCompilation) {
        Compilation oldCompilation = repository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Подборка не найдена"));
        List<Event> events = null;
        if (updatedCompilation.getEvents() != null) {
            events = eventRepository.findAllById(updatedCompilation.getEvents());
        }
        return mapper.toDto(repository.save(mapper.update(oldCompilation, updatedCompilation, events)));
    }

    @Transactional
    @Override
    public void delete(long compId) {
        repository.deleteById(compId);
    }

    @Override
    public CompilationDto getById(long compId) {
        Compilation compilation = repository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Подборка не найдена"));
        return mapper.toDto(compilation);
    }

    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, int from, int size) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

        QCompilation compilation = QCompilation.compilation;
        JPAQuery<Compilation> query = queryFactory.selectFrom(compilation)
                .offset(from)
                .limit(size);

        if (Objects.nonNull(pinned)) {
            BooleanExpression expression = compilation.pinned.eq(pinned);
            query.where(expression);
        }

        List<Compilation> compilations = query.fetch();

        return mapper.toDto(compilations);
    }
}