package ru.practicum.ewm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("SELECT new ru.practicum.ewm.model.Category (c.id, c.name) " +
           "FROM Category c " +
           "ORDER BY id " +
           "OFFSET :from ROWS FETCH NEXT :size ROWS ONLY")
    List<Category> findAllByFilters(int from, int size);

    Optional<Category> findByNameIgnoreCase(String name);
}