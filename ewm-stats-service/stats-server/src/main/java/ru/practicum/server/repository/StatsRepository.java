package ru.practicum.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.server.model.EndpointHit;
import ru.practicum.server.model.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<EndpointHit, Long> {
    @Query("SELECT new ru.practicum.server.model.ViewStats (h.app, h.uri, COUNT(h.ip) AS hits) " +
           "FROM EndpointHit h " +
           "WHERE (:uris IS NULL OR h.uri IN (:uris)) " +
           "AND (h.timestamp BETWEEN :start AND :end)" +
           "GROUP BY h.app, h.uri " +
           "ORDER BY hits DESC ")
    List<ViewStats> findAllHit(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("SELECT new ru.practicum.server.model.ViewStats (h.app, h.uri, COUNT(DISTINCT h.ip) as hits) " +
           "FROM EndpointHit h " +
           "WHERE (:uris IS NULL OR h.uri IN (:uris)) " +
           "AND (h.timestamp BETWEEN :start AND :end) " +
           "GROUP BY h.app, h.uri " +
           "ORDER BY hits DESC")
    List<ViewStats> findUniqueHit(LocalDateTime start, LocalDateTime end, List<String> uris);

}