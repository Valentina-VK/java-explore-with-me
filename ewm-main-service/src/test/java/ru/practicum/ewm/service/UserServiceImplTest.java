package ru.practicum.ewm.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.user.NewUserRequest;
import ru.practicum.ewm.dto.user.UserDto;
import ru.practicum.ewm.dto.user.UserShortDto;
import ru.practicum.ewm.enums.SubscriptionAction;
import ru.practicum.ewm.exceptions.ConflictException;
import ru.practicum.ewm.exceptions.NotFoundException;
import ru.practicum.ewm.model.User;
import ru.practicum.ewm.service.user.UserServiceImpl;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserServiceImplTest {
    private final UserServiceImpl userService;
    private final EntityManager em;

    @Nested
    @DisplayName("Tests for method - getAllByFilters")
    class TestGetAllByFilters {
        @Test
        void getAllByFilters_withIdsNull_thanReturnResult() {

            List<UserDto> result = userService.getAllByFilters(1, 2, null);

            assertThat(result, notNullValue());
            assertThat(result.size(), equalTo(2));
            assertThat(result.getFirst().getId(), equalTo(12L));
        }

        @Test
        void getAllByFilters_withIdsIn_thanReturnResult() {

            List<UserDto> result = userService.getAllByFilters(0, 2, List.of(15L));

            assertThat(result, notNullValue());
            assertThat(result.size(), equalTo(1));
            assertThat(result.getFirst().getId(), equalTo(15L));
        }

        @Test
        void getAllByFilters_withIdsNotExisting_thanReturnEmptyList() {

            List<UserDto> result = userService.getAllByFilters(0, 2, List.of(150L, 200L));

            assertThat(result, notNullValue());
            assertThat(result.size(), equalTo(0));
        }
    }

    @Nested
    @DisplayName("Tests for method - save")
    class TestSave {
        @Test
        void save_withUniqueEmail_thanOk() {
            NewUserRequest newUser = new NewUserRequest();
            newUser.setName("Valentina");
            newUser.setEmail("Valentina@yandex.ru");

            UserDto result = userService.save(newUser);

            assertThat(result, notNullValue());
            assertThat(result.getId(), greaterThan(0L));
            assertThat(result.getName(), equalTo(newUser.getName()));
            assertThat(result.getEmail(), equalTo(newUser.getEmail()));
        }

        @Test
        void save_withNotUniqueEmail_thanThrowException() {
            NewUserRequest newUser = new NewUserRequest();
            newUser.setName("Valentina");
            newUser.setEmail("user1@yandex.ru");

            assertThrows((DataIntegrityViolationException.class), () -> userService.save(newUser));
        }
    }

    @Nested
    @DisplayName("Tests for method - delete")
    class TestDelete {
        @Test
        void delete_withExistingUserId_thanOk() {
            Long existingId = 13L;
            userService.delete(existingId);

            TypedQuery<User> query = em.createQuery("SELECT u FROM User AS u", User.class);

            assertTrue(query.getResultStream()
                    .filter(user -> user.getId().equals(existingId))
                    .findFirst()
                    .isEmpty());
        }

        @Test
        void delete_withNotExistingUserId_thanThrowException() {
            Long notExistingId = 130L;

            assertThrows((NotFoundException.class), () -> userService.delete(notExistingId));
        }
    }

    @Nested
    @DisplayName("Tests for method - setPermission")
    class TestSetPermission {
        @Test
        void setPermission_true_thanOk() {
            Long existingId = 13L;

            UserDto result = userService.setPermission(existingId, true);

            assertThat(result, notNullValue());
            assertThat(result.isSubscriptionPermission(), equalTo(true));
        }
    }

    @Nested
    @DisplayName("Tests for method - setSubscription")
    class TestSetSubscription {
        Long subscriberId = 13L;

        @Test
        void setSubscription_activateWithValidId_thanOk() {
            Long initiatorId = 20L;
            UserShortDto result = userService.setSubscription(subscriberId, initiatorId, SubscriptionAction.ACTIVATE);

            assertThat(result, notNullValue());
            assertThat(result.getCountSubscribers(), equalTo(1));
        }

        @Test
        void setSubscription_cancelWithValidId_thanOk() {
            Long initiatorId = 19L;
            UserShortDto result = userService.setSubscription(subscriberId, initiatorId, SubscriptionAction.CANCEL);

            assertThat(result, notNullValue());
            assertThat(result.getCountSubscribers(), equalTo(0));
        }

        @Test
        void setSubscription_activateWithNotExistingId_thanThrowException() {
            Long notExistingId = 2000L;

            assertThrows((NotFoundException.class), () -> userService.setSubscription(subscriberId, notExistingId, SubscriptionAction.ACTIVATE));

        }

        @Test
        void setSubscription_activateWithSameId_thanThrowException() {

            assertThrows((ConflictException.class), () -> userService.setSubscription(subscriberId, subscriberId, SubscriptionAction.ACTIVATE));
        }

        @Test
        void setSubscription_activateWithNotPermission_thanThrowException() {
            Long notPermissionId = 12L;

            assertThrows((ConflictException.class), () -> userService.setSubscription(subscriberId, notPermissionId, SubscriptionAction.ACTIVATE));
        }
    }

    @Nested
    @DisplayName("Tests for method - getSubscriptions")
    class TestGetSubscriptions {
        @Test
        void getSubscriptions_thanReturnResult() {
            Long existingId = 13L;

            List<UserShortDto> result = userService.getSubscriptions(existingId);

            assertThat(result, notNullValue());
            assertThat(result.size(), equalTo(2));
        }
    }
}