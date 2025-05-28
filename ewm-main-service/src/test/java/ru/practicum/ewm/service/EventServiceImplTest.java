package ru.practicum.ewm.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.event.EventFullDto;
import ru.practicum.ewm.dto.event.EventShortDto;
import ru.practicum.ewm.dto.event.NewEventDto;
import ru.practicum.ewm.dto.event.UpdateEventUserRequest;
import ru.practicum.ewm.model.EventState;
import ru.practicum.ewm.model.Location;
import ru.practicum.ewm.service.event.EventServiceImpl;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class EventServiceImplTest {
    private final EventServiceImpl eventService;

    @Test
    void saveEvent_simpleTest() {

        NewEventDto eventDto = NewEventDto.builder()
                .eventDate("2026-03-25 13:10:00")
                .title("title")
                .description("description")
                .annotation("annotation")
                .category(12L)
                .location(new Location(null, 10.0f, 12.0f))
                .build();

        EventFullDto result = eventService.save(11L, eventDto);

        assertThat(result, notNullValue());
        assertThat(result.getId(), greaterThan(0L));
        assertThat(result.getParticipantLimit(), equalTo(0));
        assertThat(result.getPaid(), equalTo(false));
        assertThat(result.getRequestModeration(), equalTo(true));
    }

    @Test
    void getUsersEventById_simpleTest() {

        EventFullDto result = eventService.getUsersEventById(11L, 21L);

        assertThat(result, notNullValue());
        assertThat(result.getId(), greaterThan(0L));
    }

    @Test
    void updateEvent_simpleTest() {
        UpdateEventUserRequest eventDto = UpdateEventUserRequest.builder()
                .eventDate("2026-07-25 13:10:00")
                .title("title2")
                .category(13L)
                .location(new Location(null, 12.0f, 12.0f))
                .build();
        EventFullDto result = eventService.updateEvent(12L, 22L, eventDto);

        assertThat(result, notNullValue());
        assertThat(result.getId(), greaterThan(0L));
    }

    @Test
    void getEventsByUser_simpleTest() {
        List<EventShortDto> result = eventService.getEventsByUser(11L, 0, 10);

        assertThat(result, notNullValue());
        assertThat(result.size(), equalTo(1));
    }

    @Test
    void getEventsByAdmin_simpleTest() {
        List<EventFullDto> result = eventService.getEventsByAdmin(List.of(11L), List.of(EventState.PENDING), null,
                "2026-01-25 15:15:00", "2026-09-25 15:15:15", 0,1);

        assertThat(result, notNullValue());
        assertThat(result.size(), equalTo(1));
    }

    @Test
    void searchEvents_simpleTest() {
        List<EventShortDto> result = eventService.searchEvents("an", List.of(12L), null,
                "2026-01-25 15:15:00", "2026-09-25 15:15:15", false, "VIEWS",0, 10);

        assertThat(result, notNullValue());
        assertThat(result.size(), equalTo(1));
    }
}