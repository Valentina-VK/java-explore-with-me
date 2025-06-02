package ru.practicum.ewm.controller.user;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.dto.user.UserDto;
import ru.practicum.ewm.dto.user.UserShortDto;
import ru.practicum.ewm.enums.SubscriptionAction;
import ru.practicum.ewm.service.user.UserService;

import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}/subscriptions")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserPrivateController {
    private final UserService userService;

    @PatchMapping("/permission")
    public UserDto enableSubscription(@PathVariable @Positive Long userId,
                                      @RequestParam(defaultValue = "false") boolean enabled) {
        log.info("PATCH обновление разрешения на подписку пользователем id {}, значение {}", userId, enabled);
        return userService.setPermission(userId, enabled);
    }

    @PatchMapping("/{subsId}/activate")
    public UserShortDto activateSubscription(@PathVariable @Positive Long userId,
                                             @PathVariable @Positive Long subsId) {
        log.info("PATCH активация подписки пользователем id {}, на пользователя {}", userId, subsId);
        return userService.setSubscription(userId, subsId, SubscriptionAction.ACTIVATE);
    }

    @PatchMapping("/{subsId}/cancel")
    public UserShortDto cancelSubscription(@PathVariable @Positive Long userId,
                                           @PathVariable @Positive Long subsId) {
        log.info("PATCH отмена подписки пользователем id {}, на пользователя {}", userId, subsId);
        return userService.setSubscription(userId, subsId, SubscriptionAction.CANCEL);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserShortDto> getSubscriptions(@PathVariable @Positive Long userId) {
        log.info("GET получение списка пользователей, на которых активна подписка, текущего пользователя {}", userId);
        return userService.getSubscriptions(userId);
    }
}