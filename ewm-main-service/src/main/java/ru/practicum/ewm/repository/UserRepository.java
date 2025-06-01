package ru.practicum.ewm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT new ru.practicum.ewm.model.User (u.id, u.name, u.email) " +
           "FROM User u " +
           "ORDER BY id " +
           "OFFSET :from ROWS FETCH NEXT :size ROWS ONLY")
    List<User> findAllUsersByFilters(int from, int size);

    List<User> findByIdIn(List<Long> ids);
}