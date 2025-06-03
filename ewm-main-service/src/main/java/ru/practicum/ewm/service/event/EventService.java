package ru.practicum.ewm.service.event;

import ru.practicum.ewm.dto.event.EventFullDto;
import ru.practicum.ewm.dto.event.EventShortDto;
import ru.practicum.ewm.dto.event.NewEventDto;
import ru.practicum.ewm.dto.event.UpdateEventAdminRequest;
import ru.practicum.ewm.dto.event.UpdateEventUserRequest;
import ru.practicum.ewm.enums.EventState;
import ru.practicum.ewm.model.Event;

import java.util.List;

public interface EventService {

    EventFullDto save(Long userId, NewEventDto newEvent);

    EventFullDto updateEvent(Long userId, Long eventId, UpdateEventUserRequest updateEvent);

    EventFullDto updateEvent(Long eventId, UpdateEventAdminRequest updateEvent);

    void updateEvent(Event event);

    EventFullDto getUsersEventById(Long userId, Long eventId);

    EventFullDto getEventById(Long eventId, long views);

    List<EventShortDto> getEventsByUser(Long userId, int from, int size);

    List<EventFullDto> getEventsByAdmin(List<Long> users, List<EventState> states, List<Long> categories,
                                        String rangeStart, String rangeEnd, int from, int size);

    List<EventShortDto> searchEvents(String text, List<Long> categories, Boolean paid, String rangeStart,
                                     String rangeEnd, boolean onlyAvailable, String sort, int from, int size);

    List<EventShortDto> getEventsBySubscriptions(Long userId, List<Long> categories, String rangeStart,
                                                 String rangeEnd, boolean onlyAvailable, int from, int size);
}