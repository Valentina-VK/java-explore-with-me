package ru.practicum.client;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static ru.practicum.constants.StatsConstants.DATE_TIME_FORMAT;

public class StatsClient {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
    private final RestTemplate restTemplate;

    public StatsClient(String baseUri) {
        restTemplate = new RestTemplateBuilder()
                .rootUri(baseUri)
                .build();
    }

    public EndpointHitDto saveEndpointHit(EndpointHitDto requestData) {
        return restTemplate.postForObject("/hit", requestData, EndpointHitDto.class);
    }

    public List<ViewStatsDto> getStatistic(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        StringBuilder query = new StringBuilder();
        query
                .append("&")
                .append("start=" + formatter.format(start))
                .append("end=" + formatter.format(end));

        if (uris != null && !uris.isEmpty()) {
            query.append("uris=" + String.join("uris=", uris));
        }
        query.append("unique=" + unique);

        return restTemplate.getForObject(query.toString(), List.class);
    }
}