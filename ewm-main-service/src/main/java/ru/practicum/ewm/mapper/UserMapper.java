package ru.practicum.ewm.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.ewm.dto.user.NewUserRequest;
import ru.practicum.ewm.dto.user.UserDto;
import ru.practicum.ewm.dto.user.UserShortDto;
import ru.practicum.ewm.model.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(NewUserRequest dto);

    @Mapping(target = "countSubscribers", source = "user.count")
    UserDto toDto(User user);

    @Mapping(target = "countSubscribers", source = "user.count")
    UserShortDto toShortDto(User user);

    List<UserDto> toDto(List<User> users);
}