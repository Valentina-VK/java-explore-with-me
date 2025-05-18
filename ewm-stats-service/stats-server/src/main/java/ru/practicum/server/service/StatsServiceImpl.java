package ru.practicum.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.server.mapper.EndpointHitMapper;
import ru.practicum.server.mapper.ViewStatsMapper;
import ru.practicum.server.model.EndpointHit;
import ru.practicum.server.model.ViewStats;
import ru.practicum.server.repository.StatsRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static ru.practicum.constants.StatsConstants.DATE_TIME_FORMAT;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final StatsRepository statsRepository;
    private final EndpointHitMapper hitMapper;
    private final ViewStatsMapper statsMapper;

    @Transactional
    @Override
    public EndpointHitDto saveEndpointHit(EndpointHitDto hitDto) {
        EndpointHit hit = statsRepository.save(hitMapper.toEntity(hitDto));
        return hitMapper.toDto(hit);
    }

    @Override
    public List<ViewStatsDto> getStats(String start, String end, List<String> uris, Boolean unique) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
        LocalDateTime startTime = LocalDateTime.parse(start, formatter);
        LocalDateTime endTime = LocalDateTime.parse(end, formatter);
        List<ViewStats> response;
        if (unique) {
            response = statsRepository.findUniqueHit(startTime, endTime, uris);
        } else {
            response = statsRepository.findAllHit(startTime, endTime, uris);
        }
        return statsMapper.toDto(response);
    }
}