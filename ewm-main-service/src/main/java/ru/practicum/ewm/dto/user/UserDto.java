package ru.practicum.ewm.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class UserDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    private String name;
    private String email;
    private Integer countSubscribers;
    private List<UserShortDto> subscriptions;
    private boolean subscriptionPermission;
}