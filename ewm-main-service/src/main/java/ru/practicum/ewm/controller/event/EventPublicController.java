package ru.practicum.ewm.controller.event;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.client.StatsClient;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.ewm.dto.event.EventFullDto;
import ru.practicum.ewm.dto.event.EventShortDto;
import ru.practicum.ewm.service.event.EventService;
import ru.practicum.ewm.util.DateTimeMapper;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.ewm.util.EwmConstants.APP_NAME;
import static ru.practicum.ewm.util.EwmConstants.DEFAULT_TEN;
import static ru.practicum.ewm.util.EwmConstants.DEFAULT_ZERO;

@RestController
@RequestMapping(path = "/events")
@RequiredArgsConstructor
@Slf4j
@Validated
public class EventPublicController {
    private final EventService eventService;
    private final StatsClient statsClient;

    @GetMapping
    public List<EventShortDto> findEventsByFilters(@RequestParam(required = false) String text,
                                                   @RequestParam(required = false) List<Long> categories,
                                                   @RequestParam(required = false) Boolean paid,
                                                   @RequestParam(required = false) String rangeStart,
                                                   @RequestParam(required = false) String rangeEnd,
                                                   @RequestParam(defaultValue = "false") boolean onlyAvailable,
                                                   @RequestParam(required = false) String sort,
                                                   @RequestParam(defaultValue = DEFAULT_ZERO) @PositiveOrZero int from,
                                                   @RequestParam(defaultValue = DEFAULT_TEN) @Positive int size,
                                                   HttpServletRequest request) {
        log.info("GET Поиск событий по фильтрам: text {} sort {}, from {}, size {}", text, sort, from, size);

        sendHitToStatsService(request);
        return eventService.searchEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEventById(@Positive @PathVariable Long eventId,
                                     HttpServletRequest request) {
        log.info("GET получение события id {} ", eventId);

        List<ViewStatsDto> statistic = statsClient.getStatistic(LocalDateTime.now().minusYears(5),
                LocalDateTime.now(), List.of(request.getRequestURI()), true);

        long views = statistic.stream().findFirst().map(ViewStatsDto::getHits).orElse(0L);

        EventFullDto response = eventService.getEventById(eventId, views);
        if (response != null) {
            sendHitToStatsService(request);
        }
        return response;
    }


    private void sendHitToStatsService(HttpServletRequest request) {
        EndpointHitDto hitDto = EndpointHitDto.builder()
                .app(APP_NAME)
                .ip(request.getRemoteAddr())
                .uri(request.getRequestURI())
                .timestamp(DateTimeMapper.mapToString(LocalDateTime.now()))
                .build();
        statsClient.saveEndpointHit(hitDto);
    }
}