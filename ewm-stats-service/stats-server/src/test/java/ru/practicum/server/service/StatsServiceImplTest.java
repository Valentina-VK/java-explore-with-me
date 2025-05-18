package ru.practicum.server.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class StatsServiceImplTest {
    private final StatsServiceImpl statsService;

    @Test
    void saveEndpointHit() {
        EndpointHitDto hitDto = EndpointHitDto.builder()
                .app("ewm-main-service")
                .ip("192.163.0.1")
                .uri("/events/1")
                .timestamp("2022-09-06 11:00:23")
                .build();

        EndpointHitDto result = statsService.saveEndpointHit(hitDto);

        assertThat(result, notNullValue());
        assertThat(result.getApp(), equalTo(hitDto.getApp()));
        assertThat(result.getIp(), equalTo(hitDto.getIp()));
        assertThat(result.getUri(), equalTo(hitDto.getUri()));
        assertThat(result.getTimestamp(), equalTo(hitDto.getTimestamp()));
    }

    @Nested
    @DisplayName("Tests for method - getStats")
    class TestGetStats {
        private final String start = "2020-01-01 00:00:00";
        private final String end = "2025-12-31 23:59:59";
        private final List<String> uris = List.of("/events/1");

        @Test
        void getStats_withUniqueFalse() {
            Boolean unique = false;

            List<ViewStatsDto> result = statsService.getStats(start, end, uris, unique);

            assertThat(result, notNullValue());
            assertThat(result.getFirst().getHits(), equalTo(2L));
        }

        @Test
        void getStats_withUniqueTrue() {
            Boolean unique = true;

            List<ViewStatsDto> result = statsService.getStats(start, end, uris, unique);

            assertThat(result, notNullValue());
            assertThat(result.getFirst().getHits(), equalTo(1L));
        }

    }
}