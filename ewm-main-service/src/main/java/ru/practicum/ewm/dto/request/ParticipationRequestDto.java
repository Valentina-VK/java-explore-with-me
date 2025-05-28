package ru.practicum.ewm.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ParticipationRequestDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    private String created;

    private long event;

    private long requester;

    private String status;
}