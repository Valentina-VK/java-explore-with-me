package ru.practicum.ewm.service.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.ewm.dto.request.ParticipationRequestDto;
import ru.practicum.ewm.exceptions.ConflictException;
import ru.practicum.ewm.exceptions.NotFoundException;
import ru.practicum.ewm.mapper.RequestMapper;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.EventState;
import ru.practicum.ewm.model.Request;
import ru.practicum.ewm.model.RequestStatus;
import ru.practicum.ewm.model.User;
import ru.practicum.ewm.repository.RequestRepository;
import ru.practicum.ewm.service.event.EventService;
import ru.practicum.ewm.validation.ValidateService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository repository;
    private final EventService eventService;
    private final ValidateService validateService;
    private final RequestMapper mapper;

    @Override
    public List<ParticipationRequestDto> getRequestsByUser(Long userId) {
        validateService.checkUser(userId);
        return mapper.toDto(repository.findAllByRequesterId(userId));
    }

    @Transactional
    @Override
    public ParticipationRequestDto createRequest(Long userId, Long eventId) {
        User user = validateService.checkUser(userId);
        Event event = validateService.checkEvent(eventId);

        if (event.getInitiator().getId().equals(userId)) {
            throw new ConflictException("Нельзя подавать заявку на своё собственное событие.");
        }
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictException("Нельзя подавать заявку на неопубликованное событие.");
        }

        int confirmedRequests = event.getConfirmedRequests();
        int limit = event.getParticipantLimit();

        if (confirmedRequests == limit && limit != 0) {
            throw new ConflictException("У события достигнут лимит запросов на участие.");
        }
        RequestStatus status;
        if (!event.getRequestModeration() || limit == 0) {
            status = RequestStatus.CONFIRMED;
            event.setConfirmedRequests(confirmedRequests + 1);
            eventService.updateEvent(event);
        } else {
            status = RequestStatus.PENDING;
        }
        Request newRequest = Request.builder()
                .created(LocalDateTime.now())
                .requester(user)
                .status(status)
                .event(event)
                .build();
        return mapper.toDto(repository.save(newRequest));
    }

    @Transactional
    @Override
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        Request request = repository.findByIdAndRequesterId(requestId, userId)
                .orElseThrow(() -> new NotFoundException("Запрос на участие текущего пользователя не найден, id: "
                                                         + requestId));

        request.setStatus(RequestStatus.CANCELED);
        return mapper.toDto(repository.save(request));
    }

    @Override
    public List<ParticipationRequestDto> findAllRequestsByEventIdAndInitiator(Long userId, Long eventId) {
        validateService.checkUser(userId);
        Event event = validateService.checkEvent(eventId);
        if (event.getInitiator().getId().longValue() != userId.longValue()) {
            return List.of();
        }
        return mapper.toDto(repository.findAllByEventId(eventId));
    }

    @Transactional
    @Override
    public EventRequestStatusUpdateResult updateRequestsStatus(Long userId, Long eventId,
                                                               EventRequestStatusUpdateRequest statusUpdateRequest) {
        validateService.checkUser(userId);
        Event event = validateService.checkEvent(eventId);
        if (event.getInitiator().getId().longValue() != userId.longValue()) {
            throw new ConflictException("Изменение статуса заявок доступно только иннициатору события");
        }
        if (event.getState() != EventState.PUBLISHED) {
            throw new ConflictException("Нельзя подавать заявку на неопубликованное событие");
        }

        EventRequestStatusUpdateResult result = new EventRequestStatusUpdateResult(new ArrayList<>(), new ArrayList<>());
        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            return result;
        }
        int countConfirmed = event.getConfirmedRequests();
        int limit = event.getParticipantLimit();
        if (countConfirmed == limit) {
            throw new ConflictException("Лимит заявок исчерпан");
        }
        List<Request> requests = repository.findAllByEventIdAndIdIn(eventId, statusUpdateRequest.getRequestIds());

        for (Request request : requests) {
            if (!request.getStatus().equals(RequestStatus.PENDING)) {
                throw new ConflictException("Заявка не в статусе ожидания, id: " + request.getId());
            }
            if (statusUpdateRequest.getStatus().equals(RequestStatus.REJECTED)) {
                request.setStatus(statusUpdateRequest.getStatus());
                result.getRejectedRequests().add(mapper.toDto(request));
            } else {
                if (countConfirmed < limit) {
                    ++countConfirmed;
                    request.setStatus(statusUpdateRequest.getStatus());
                    result.getConfirmedRequests().add(mapper.toDto(request));
                } else {
                    request.setStatus(RequestStatus.REJECTED);
                    result.getRejectedRequests().add(mapper.toDto(request));
                }
            }
        }
        repository.saveAll(requests);
        event.setConfirmedRequests(countConfirmed);

        eventService.updateEvent(event);

        return result;
    }
}