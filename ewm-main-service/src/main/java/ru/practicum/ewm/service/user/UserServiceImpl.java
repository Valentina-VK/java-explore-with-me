package ru.practicum.ewm.service.user;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.user.NewUserRequest;
import ru.practicum.ewm.dto.user.UserDto;
import ru.practicum.ewm.dto.user.UserShortDto;
import ru.practicum.ewm.enums.SubscriptionAction;
import ru.practicum.ewm.exceptions.ConflictException;
import ru.practicum.ewm.mapper.UserMapper;
import ru.practicum.ewm.model.QUser;
import ru.practicum.ewm.model.User;
import ru.practicum.ewm.repository.UserRepository;
import ru.practicum.ewm.validation.ValidateService;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final ValidateService validateService;
    private final UserMapper mapper;
    private final EntityManager entityManager;

    @Override
    public List<UserDto> getAllByFilters(int from, int size, List<Long> ids) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QUser user = QUser.user;
        BooleanExpression byFilters = null;
        if (Objects.nonNull(ids)) {
            byFilters = user.id.in(ids);
        }

        List<User> users = queryFactory.selectFrom(user)
                .where(byFilters)
                .offset(from)
                .limit(size)
                .fetch();

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

    @Transactional
    @Override
    public UserDto setPermission(Long userId, boolean enabled) {
        User user = validateService.checkUser(userId);
        if (!enabled) {
            user.setSubscribers(null);
            user.setCount(0);
        }
        user.setSubscriptionPermission(enabled);

        repository.save(user);
        return mapper.toDto(user);
    }

    @Transactional
    @Override
    public UserShortDto setSubscription(Long userId, Long subsId, SubscriptionAction action) {
        if (userId.longValue() == subsId.longValue()) {
            throw new ConflictException("Подписка пользователя на себя запрещена");
        }
        User user = validateService.checkUser(userId);
        User subscription = validateService.checkUser(subsId);
        if (!subscription.isSubscriptionPermission()) {
            throw new ConflictException("Подписка на данного пользователя запрещена");
        }
        switch (action) {
            case CANCEL -> user.getSubscriptions().remove(subscription);
            case ACTIVATE -> user.getSubscriptions().add(subscription);
        }
        repository.saveAndFlush(user);
        entityManager.refresh(subscription);
        return mapper.toShortDto(subscription);
    }

    @Override
    public List<UserShortDto> getSubscriptions(Long userId) {
        User user = validateService.checkUser(userId);

        return user.getSubscriptions().stream()
                .map(mapper::toShortDto)
                .toList();
    }
}