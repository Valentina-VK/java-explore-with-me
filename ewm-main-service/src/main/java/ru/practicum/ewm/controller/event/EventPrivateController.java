package ru.practicum.ewm.controller.event;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.dto.event.EventFullDto;
import ru.practicum.ewm.dto.event.EventShortDto;
import ru.practicum.ewm.dto.event.NewEventDto;
import ru.practicum.ewm.dto.event.UpdateEventUserRequest;
import ru.practicum.ewm.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.ewm.dto.request.ParticipationRequestDto;
import ru.practicum.ewm.enums.RequestStatus;
import ru.practicum.ewm.service.event.EventService;
import ru.practicum.ewm.service.request.RequestService;

import java.util.List;

import static ru.practicum.ewm.util.EwmConstants.DEFAULT_TEN;
import static ru.practicum.ewm.util.EwmConstants.DEFAULT_ZERO;

@RestController
@RequestMapping(path = "/users/{userId}/events")
@RequiredArgsConstructor
@Slf4j
@Validated
public class EventPrivateController {
    private final EventService eventService;
    private final RequestService requestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto saveEvent(@PathVariable @Positive Long userId,
                                  @RequestBody @Valid NewEventDto newEvent) {
        log.info("POST Создание события {} пользователем с id {} ", newEvent, userId);
        return eventService.save(userId, newEvent);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEventByUser(@Positive @PathVariable Long userId,
                                          @Positive @PathVariable Long eventId,
                                          @Valid @RequestBody UpdateEventUserRequest updateEvent) {
        log.info("PATCH обновление события id {} пользователем id {} - {}", eventId, userId, updateEvent);
        return eventService.updateEvent(userId, eventId, updateEvent);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEventById(@PathVariable @Positive Long userId,
                                     @PathVariable @Positive Long eventId) {
        log.info("GET получение события id {} пользователем id {}", eventId, userId);
        return eventService.getUsersEventById(userId, eventId);
    }

    @GetMapping
    public List<EventShortDto> getEventsByUser(@PathVariable @Positive Long userId,
                                               @RequestParam(name = "from", defaultValue = DEFAULT_ZERO) @PositiveOrZero int from,
                                               @RequestParam(name = "size", defaultValue = DEFAULT_TEN) @Positive int size) {
        log.info("GET получение событий пользователя id {}", userId);
        return eventService.getEventsByUser(userId, from, size);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getRequestsByEvent(@PathVariable @Positive Long userId,
                                                            @PathVariable @Positive Long eventId) {
        log.info("GET получение списка запросов по событию с id {} пользователя с id {}", eventId, userId);
        return requestService.findAllRequestsByEventIdAndInitiator(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult updateRequests(@PathVariable Long userId,
                                                         @PathVariable Long eventId,
                                                         @RequestBody EventRequestStatusUpdateRequest statusUpdateRequest) {
        log.info("PATCH обновление статуса запросов на участие в событии id {} пользователя с id {}", eventId, userId);
        RequestStatus newStatus = statusUpdateRequest.getStatus();
        if (!newStatus.equals(RequestStatus.CONFIRMED) && !newStatus.equals(RequestStatus.REJECTED)) {
            throw new ValidationException("Некорректный статус для обновления");
        }
        return requestService.updateRequestsStatus(userId, eventId, statusUpdateRequest);
    }

    @GetMapping("/subscriptions")
    public List<EventShortDto> getEventsBySubscriptions(@PathVariable @Positive Long userId,
                                                        @RequestParam(required = false) List<Long> categories,
                                                        @RequestParam(required = false) String rangeStart,
                                                        @RequestParam(required = false) String rangeEnd,
                                                        @RequestParam(defaultValue = "false") boolean onlyAvailable,
                                                        @RequestParam(name = "from", defaultValue = DEFAULT_ZERO) @PositiveOrZero int from,
                                                        @RequestParam(name = "size", defaultValue = DEFAULT_TEN) @Positive int size) {
        log.info("GET получение событий по подписке пользователя id {}", userId);
        return eventService.getEventsBySubscriptions(userId, categories, rangeStart, rangeEnd, onlyAvailable, from, size);
    }
}