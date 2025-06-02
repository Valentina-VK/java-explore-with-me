package ru.practicum.ewm.service.event;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.event.EventFullDto;
import ru.practicum.ewm.dto.event.EventShortDto;
import ru.practicum.ewm.dto.event.NewEventDto;
import ru.practicum.ewm.dto.event.UpdateEventAdminRequest;
import ru.practicum.ewm.dto.event.UpdateEventUserRequest;
import ru.practicum.ewm.enums.AdminStateAction;
import ru.practicum.ewm.enums.EventState;
import ru.practicum.ewm.enums.StateAction;
import ru.practicum.ewm.exceptions.ConflictException;
import ru.practicum.ewm.exceptions.NotFoundException;
import ru.practicum.ewm.exceptions.NotValidDateTimeException;
import ru.practicum.ewm.mapper.EventMapper;
import ru.practicum.ewm.model.Category;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.Location;
import ru.practicum.ewm.model.QEvent;
import ru.practicum.ewm.model.User;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.repository.LocationRepository;
import ru.practicum.ewm.util.DateTimeMapper;
import ru.practicum.ewm.validation.ValidateService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository repository;
    private final ValidateService validateService;
    private final LocationRepository locationRepository;
    private final EventMapper mapper;
    private final EntityManager entityManager;

    @Transactional
    @Override
    public EventFullDto save(Long userId, NewEventDto newEvent) {
        validateService.checkStartTime(newEvent.getEventDate());
        User initiator = validateService.checkUser(userId);
        Category cat = validateService.checkCategory(newEvent.getCategory());
        Location location = setLocationId(newEvent.getLocation());
        newEvent.setLocation(location);

        Event newEventReady = mapper.toEntity(newEvent, cat, initiator, EventState.PENDING, LocalDateTime.now());

        Event event = repository.save(newEventReady);

        return mapper.toDto(event);
    }

    @Override
    public EventFullDto getUsersEventById(Long userId, Long eventId) {
        validateService.checkUser(userId);
        Event event = repository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException("Событие пользователя не найдено, id: " + eventId));

        return mapper.toDto(event);
    }

    @Override
    public EventFullDto getEventById(Long eventId, long views) {
        Event event = repository.findByIdAndState(eventId, EventState.PUBLISHED)
                .orElseThrow(() -> new NotFoundException("Событие не найдено, id: " + eventId));
        event.setViews(views);
        repository.save(event);
        return mapper.toDto(event);
    }

    @Transactional
    @Override
    public EventFullDto updateEvent(Long userId, Long eventId, UpdateEventUserRequest updateEvent) {
        if (updateEvent.getEventDate() != null) {
            validateService.checkStartTime(updateEvent.getEventDate());
        }
        validateService.checkUser(userId);
        Event oldEvent = repository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException("Событие пользователя не найдено, id: " + eventId));
        if (oldEvent.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictException("Запрещено редактировать опубликованные события");
        }

        Category cat = null;
        if (updateEvent.getCategory() != null) {
            cat = validateService.checkCategory(updateEvent.getCategory());
        }

        if (updateEvent.getLocation() != null) {
            Location location = setLocationId(updateEvent.getLocation());
            updateEvent.setLocation(location);
        }
        Event newEventReady = mapper.update(updateEvent, cat, oldEvent);
        if (updateEvent.getStateAction() != null) {
            if (updateEvent.getStateAction().equals(StateAction.SEND_TO_REVIEW)) {
                newEventReady.setState(EventState.PENDING);
            } else {
                newEventReady.setState(EventState.CANCELED);
            }
        }
        return mapper.toDto(repository.save(newEventReady));
    }

    @Transactional
    @Override
    public EventFullDto updateEvent(Long eventId, UpdateEventAdminRequest updateEvent) {
        if (updateEvent.getEventDate() != null) {
            validateService.checkStartTime(updateEvent.getEventDate());
        }
        Event oldEvent = validateService.checkEvent(eventId);
        if (oldEvent.getState().equals(EventState.PUBLISHED) || oldEvent.getState().equals(EventState.CANCELED)) {
            throw new ConflictException("Запрещено редактировать опубликованные и отмененные события");
        }
        Category cat = null;
        if (updateEvent.getCategory() != null) {
            cat = validateService.checkCategory(updateEvent.getCategory());
        }
        if (updateEvent.getLocation() != null) {
            Location location = setLocationId(updateEvent.getLocation());
            updateEvent.setLocation(location);
        }
        Event newEventReady = mapper.update(updateEvent, cat, oldEvent);
        if (updateEvent.getStateAction() != null) {
            if (updateEvent.getStateAction().equals(AdminStateAction.PUBLISH_EVENT)) {
                newEventReady.setState(EventState.PUBLISHED);
            } else {
                newEventReady.setState(EventState.CANCELED);
            }
        }
        return mapper.toDto(repository.save(newEventReady));
    }

    @Transactional
    @Override
    public void updateEvent(Event event) {
        repository.saveAndFlush(event);
    }

    @Override
    public List<EventShortDto> getEventsByUser(Long userId, int from, int size) {
        Pageable page = PageRequest.of(from / size, size);
        return repository.findAllByInitiatorId(userId, page).stream()
                .map(mapper::toShortDto).toList();
    }

    @Override
    public List<EventFullDto> getEventsByAdmin(List<Long> users, List<EventState> states, List<Long> categories,
                                               String rangeStart, String rangeEnd, int from, int size) {
        QEvent event = QEvent.event;
        BooleanExpression byFilters = preSetQuery(event, categories, rangeStart, rangeEnd);
        if (Objects.nonNull(states)) {
            byFilters = byFilters.and(event.state.in(states));
        }
        if (Objects.nonNull(users)) {
            byFilters = byFilters.and(event.initiator.id.in(users));
        }
        List<Event> events = createQuery(event, byFilters, from, size)
                .fetch();

        return events.stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public List<EventShortDto> searchEvents(String text, List<Long> categories, Boolean paid, String rangeStart,
                                            String rangeEnd, boolean onlyAvailable, String sort, int from, int size) {
        QEvent event = QEvent.event;
        BooleanExpression byFilters = preSetQuery(event, categories, rangeStart, rangeEnd);
        byFilters = setAvailable(event, byFilters, onlyAvailable);

        if (Objects.nonNull(paid)) {
            byFilters = byFilters.and(event.paid.eq(paid));
        }
        if (Objects.nonNull(text)) {
            byFilters = byFilters.and((event.description.containsIgnoreCase(text))
                    .or(event.annotation.containsIgnoreCase(text)));
        }

        JPAQuery<Event> query = createQuery(event, byFilters, from, size);

        if (Objects.nonNull(sort)) {
            if (sort.equals("VIEWS")) {
                query = query.orderBy(event.views.asc());
            }
            if (sort.equals("EVENT_DATE")) {
                query = query.orderBy(event.eventDate.asc());
            }
            if (sort.equals("COUNT_SUBSCRIBERS")) {
                query = query.orderBy(event.initiator.count.desc());
            }
        }
        List<Event> events = query.fetch();

        return events.stream()
                .map(mapper::toShortDto)
                .toList();
    }

    @Override
    public List<EventShortDto> getEventsBySubscriptions(Long userId, List<Long> categories, String rangeStart,
                                                        String rangeEnd, boolean onlyAvailable, int from, int size) {
        User user = validateService.checkUser(userId);
        List<Long> subscriptionsIds = user.getSubscriptions().stream()
                .map(User::getId).
                toList();

        QEvent event = QEvent.event;
        BooleanExpression byFilters = preSetQuery(event, categories, rangeStart, rangeEnd);
        byFilters = setAvailable(event, byFilters, onlyAvailable);

        byFilters = byFilters.and(event.initiator.id.in(subscriptionsIds));

        JPAQuery<Event> query = createQuery(event, byFilters, from, size);

        List<Event> events = query.fetch();

        return events.stream()
                .map(mapper::toShortDto)
                .toList();
    }

    private Location setLocationId(Location location) {
        Location existingLocation = locationRepository.findByLatAndLon(location.getLat(), location.getLon());
        if (existingLocation == null) {
            return locationRepository.save(location);
        }
        return existingLocation;
    }

    private BooleanExpression preSetQuery(QEvent event, List<Long> categories, String rangeStart, String rangeEnd) {
        BooleanExpression byFilters;
        if (Objects.nonNull(rangeStart) && Objects.nonNull(rangeEnd)) {
            LocalDateTime start = DateTimeMapper.mapToDateTime(rangeStart);
            LocalDateTime end = DateTimeMapper.mapToDateTime(rangeEnd);
            if (start.isEqual(end) || start.isAfter(end)) {
                throw new NotValidDateTimeException("Некорректные даты в запросе");
            }
            byFilters = event.eventDate.between(start, end);
        } else {
            byFilters = event.eventDate.after(LocalDateTime.now());
        }
        if (Objects.nonNull(categories)) {
            byFilters = byFilters.and(event.category.id.in(categories));
        }
        return byFilters;
    }

    private BooleanExpression setAvailable(QEvent event, BooleanExpression byFilters, boolean onlyAvailable) {
        byFilters = byFilters.and(event.state.eq(EventState.PUBLISHED));
        if (onlyAvailable) {
            byFilters = byFilters.and(event.confirmedRequests.lt(event.participantLimit));
        }
        return byFilters;
    }

    private JPAQuery<Event> createQuery(QEvent event, BooleanExpression byFilters, int from, int size) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        return queryFactory.selectFrom(event)
                .where(byFilters)
                .offset(from)
                .limit(size);
    }
}