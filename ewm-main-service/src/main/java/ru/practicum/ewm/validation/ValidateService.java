package ru.practicum.ewm.validation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.exceptions.NotFoundException;
import ru.practicum.ewm.exceptions.NotValidDateTimeException;
import ru.practicum.ewm.model.Category;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.Request;
import ru.practicum.ewm.model.User;
import ru.practicum.ewm.repository.CategoryRepository;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.repository.RequestRepository;
import ru.practicum.ewm.repository.UserRepository;
import ru.practicum.ewm.util.DateTimeMapper;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ValidateService {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;

    public User checkUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден, id: " + id));
    }

    public Category checkCategory(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Категория не найдена, id: " + id));
    }

    public Event checkEvent(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Событие не найдено, id: " + id));
    }


    public void checkStartTime(String eventDate) {
        LocalDateTime start = DateTimeMapper.mapToDateTime(eventDate);
        if (!start.isAfter(LocalDateTime.now().plusHours(2L))) {
            throw new NotValidDateTimeException("Начало события должно быть не ранее двух часов от текущего момента");
        }
    }

    public Request checkRequest(Long id) {
        return requestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Запрос на участие не найден, id: " + id));
    }

}
