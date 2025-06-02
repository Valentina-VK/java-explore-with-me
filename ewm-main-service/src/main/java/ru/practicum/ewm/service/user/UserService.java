package ru.practicum.ewm.service.user;

import ru.practicum.ewm.dto.user.NewUserRequest;
import ru.practicum.ewm.dto.user.UserDto;
import ru.practicum.ewm.dto.user.UserShortDto;
import ru.practicum.ewm.enums.SubscriptionAction;

import java.util.List;

public interface UserService {
    List<UserDto> getAllByFilters(int from, int size, List<Long> ids);

    UserDto save(NewUserRequest newUser);

    void delete(Long userId);

    UserDto setPermission(Long userId, boolean enabled);

    UserShortDto setSubscription(Long userId, Long subsId, SubscriptionAction action);

    List<UserShortDto> getSubscriptions(Long userId);
}