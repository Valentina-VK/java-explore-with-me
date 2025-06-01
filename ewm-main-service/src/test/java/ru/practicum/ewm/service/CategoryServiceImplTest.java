package ru.practicum.ewm.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.category.CategoryDto;
import ru.practicum.ewm.dto.category.NewCategoryDto;
import ru.practicum.ewm.exceptions.ConflictException;
import ru.practicum.ewm.exceptions.NotFoundException;
import ru.practicum.ewm.model.Category;
import ru.practicum.ewm.service.category.CategoryServiceImpl;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class CategoryServiceImplTest {
    private final CategoryServiceImpl categoryService;
    private final EntityManager em;
    private final Long existingId = 11L;
    private final Long notExistingId = 1000L;

    @Nested
    @DisplayName("Tests for method - getAllByFilters")
    class TestGetAllByFilters {
        @Test
        void getAllByFilters_thanReturnResult() {
            List<CategoryDto> result = categoryService.getAllByFilters(1, 2);

            assertThat(result, notNullValue());
            assertThat(result.size(), equalTo(2));
            assertThat(result.getFirst().getId(), equalTo(12L));
            assertThat(result.getLast().getId(), equalTo(13L));
        }
    }

    @Nested
    @DisplayName("Tests for method - getById")
    class TestGetById {

        @Test
        void getById_withExistingId_thanOk() {
            CategoryDto result = categoryService.getById(existingId);

            assertThat(result, notNullValue());
            assertThat(result.getId(), equalTo(existingId));
            assertThat(result.getName(), equalTo("Концерты"));
        }

        @Test
        void getById_withNotExistingId_thanOk() {

            assertThrows((NotFoundException.class), () -> categoryService.getById(notExistingId));
        }
    }

    @Nested
    @DisplayName("Tests for method - save")
    class TestSave {
        NewCategoryDto newCategory = new NewCategoryDto();

        @Test
        void save_withNewName_thanOk() {
            newCategory.setName("Новая категория");

            CategoryDto result = categoryService.save(newCategory);

            assertThat(result, notNullValue());
            assertThat(result.getId(), greaterThan(0L));
            assertThat(result.getName(), equalTo(newCategory.getName()));
        }

        @Test
        void save_withExistingName_thanThrowException() {
            newCategory.setName("Концерты");

            assertThrows((DataIntegrityViolationException.class), () -> categoryService.save(newCategory));
        }
    }

    @Nested
    @DisplayName("Tests for method - update")
    class TestUpdate {
        private final NewCategoryDto newCategory = new NewCategoryDto();

        @Test
        void update_withNewName_thanOk() {
            newCategory.setName("Новая Категория");

            CategoryDto result = categoryService.update(existingId, newCategory);

            assertThat(result, notNullValue());
            assertThat(result.getId(), equalTo(existingId));
            assertThat(result.getName(), equalTo(newCategory.getName()));
        }

        @Test
        void update_withExistingName_thanThrowException() {
            newCategory.setName("Театр");

            assertThrows((ConflictException.class), () -> categoryService.update(existingId, newCategory));
        }

        @Test
        void update_withNotExistingId_thanThrowException() {

            assertThrows((NotFoundException.class), () -> categoryService.update(notExistingId, newCategory));
        }
    }

    @Nested
    @DisplayName("Tests for method - delete")
    class TestDelete {

        @Test
        void delete_withExistingId_thanOk() {
            categoryService.delete(existingId);

            TypedQuery<Category> query = em.createQuery("SELECT c FROM Category AS c", Category.class);

            assertTrue(query.getResultStream()
                    .filter(category -> category.getId().equals(existingId))
                    .findFirst()
                    .isEmpty());
        }

        @Test
        void delete_withNotExistingId_thanThrowException() {

            assertThrows((NotFoundException.class), () -> categoryService.delete(notExistingId));
        }
    }
}