package ru.practicum.ewm.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.ewm.dto.request.ParticipationRequestDto;
import ru.practicum.ewm.model.RequestStatus;
import ru.practicum.ewm.service.request.RequestServiceImpl;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class RequestServiceImplTest {
    private final RequestServiceImpl requestService;

    @Test
    void getRequestsByUser_simpleTest() {
        List<ParticipationRequestDto> result = requestService.getRequestsByUser(15L);

        assertThat(result, notNullValue());
        assertThat(result.size(), equalTo(1));
    }

    @Test
    void createRequest_simpleTest() {
        ParticipationRequestDto result = requestService.createRequest(13L, 21L);

        assertThat(result, notNullValue());
        assertThat(result.getId(), equalTo(1L));
    }

    @Test
    void cancelRequest_simpleTest() {

        ParticipationRequestDto result = requestService.cancelRequest(15L, 31L);

        assertThat(result, notNullValue());
        assertThat(result.getEvent(), equalTo(21L));
    }


    @Test
    void findAllRequestsByEventIdAndInitiator_simpleTest() {

        List<ParticipationRequestDto> result = requestService.findAllRequestsByEventIdAndInitiator(11L, 21L);

        assertThat(result, notNullValue());
        assertThat(result.size(), equalTo(1));
    }

    @Test
    void updateRequestsStatus_simpleTest() {

        EventRequestStatusUpdateResult result = requestService.updateRequestsStatus(11L, 21L,
                new EventRequestStatusUpdateRequest(List.of(31L), RequestStatus.CONFIRMED));

        assertThat(result, notNullValue());
        assertThat(result.getConfirmedRequests().size(), equalTo(1));
        assertThat(result.getRejectedRequests().size(), equalTo(0));
    }
}