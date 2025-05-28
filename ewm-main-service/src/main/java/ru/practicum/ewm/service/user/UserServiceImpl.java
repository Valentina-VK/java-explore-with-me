package ru.practicum.ewm.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.user.NewUserRequest;
import ru.practicum.ewm.dto.user.UserDto;
import ru.practicum.ewm.mapper.UserMapper;
import ru.practicum.ewm.model.User;
import ru.practicum.ewm.repository.UserRepository;
import ru.practicum.ewm.validation.ValidateService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final ValidateService validateService;
    private final UserMapper mapper;

    @Override
    public List<UserDto> getAllByFilters(int from, int size, List<Long> ids) {
        List<User> users;
        if (ids == null || ids.isEmpty()) {
            users = repository.findAllUsersByFilters(from, size);
        } else {
            users = repository.findByIdIn(ids);
        }
        return mapper.toDto(users);
    }

    @Transactional
    @Override
    public UserDto save(NewUserRequest newUser) {
        User user = repository.save(mapper.toEntity(newUser));
        return mapper.toDto(user);
    }

    @Transactional
    @Override
    public void delete(Long userId) {
        validateService.checkUser(userId);
        repository.deleteById(userId);
    }
}