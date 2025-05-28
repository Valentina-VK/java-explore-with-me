package ru.practicum.ewm.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.compilation.CompilationDto;
import ru.practicum.ewm.dto.compilation.NewCompilationDto;
import ru.practicum.ewm.dto.compilation.UpdateCompilationRequest;
import ru.practicum.ewm.model.Compilation;
import ru.practicum.ewm.service.compilation.CompilationServiceImpl;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class CompilationServiceImplTest {
    private final CompilationServiceImpl compilationService;
    private final EntityManager em;
    private final long existingId = 41L;

    @Test
    void createCompilation_simpleTest() {
        NewCompilationDto newCompilation = new NewCompilationDto();
        newCompilation.setTitle("New Compilation");
        newCompilation.setEvents(List.of(21L, 22L));
        newCompilation.setPinned(true);

        CompilationDto result = compilationService.create(newCompilation);

        assertThat(result, notNullValue());
        assertThat(result.getTitle(), equalTo(newCompilation.getTitle()));
    }

    @Test
    void updateCompilation_simpleTest() {
        UpdateCompilationRequest updatedCompilation = new UpdateCompilationRequest();
        updatedCompilation.setTitle("New Compilation");

        CompilationDto result = compilationService.update(existingId, updatedCompilation);

        assertThat(result, notNullValue());
        assertThat(result.getTitle(), equalTo(updatedCompilation.getTitle()));
        assertThat(result.getId(), equalTo(existingId));
    }

    @Test
    void deleteCompilation_simpleTest() {
        compilationService.delete(existingId);

        TypedQuery<Compilation> query = em.createQuery("SELECT c FROM Compilation AS c", Compilation.class);

        assertTrue(query.getResultStream()
                .filter(compilation -> compilation.getId().equals(existingId))
                .findFirst()
                .isEmpty());
    }

    @Test
    void getByIdCompilation_simpleTest() {

        CompilationDto result = compilationService.getById(existingId);

        assertThat(result, notNullValue());
        ;
        assertThat(result.getId(), equalTo(existingId));

    }

    @Test
    void getCompilations_simpleTest() {

        List<CompilationDto> result = compilationService.getCompilations(false, 1, 10);

        assertThat(result, notNullValue());
        ;
        assertThat(result.size(), equalTo(0));
    }
}