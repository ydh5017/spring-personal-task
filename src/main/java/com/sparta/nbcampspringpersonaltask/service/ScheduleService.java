package com.sparta.nbcampspringpersonaltask.service;

import com.sparta.nbcampspringpersonaltask.Entity.Schedule;
import com.sparta.nbcampspringpersonaltask.dto.ScheduleRequestDto;
import com.sparta.nbcampspringpersonaltask.dto.ScheduleResponseDto;
import com.sparta.nbcampspringpersonaltask.exception.ErrorCode;
import com.sparta.nbcampspringpersonaltask.exception.ScheduleException;
import com.sparta.nbcampspringpersonaltask.repository.ScheduleRepository;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    public ScheduleResponseDto createSchedule(ScheduleRequestDto requestDto) {
        Schedule schedule = new Schedule(requestDto);

        Schedule saveSchedule = scheduleRepository.save(schedule);

        ScheduleResponseDto responseDto = new ScheduleResponseDto(saveSchedule);

        return responseDto;
    }

    public List<ScheduleResponseDto> findAll() {
        return scheduleRepository.findAllByOrderByCreatedAtDesc().stream().map(ScheduleResponseDto::new).toList();
    }

    public List<ScheduleResponseDto> getSchedulesByKeyword(String keyword) {
        return scheduleRepository.findAllByTitleContainsOrderByCreatedAtDesc(keyword).stream().map(ScheduleResponseDto::new).toList();
    }

    @Transactional
    public Long updateSchedule(Long id, ScheduleRequestDto requestDto) {
        Schedule schedule = findScheduleById(id);

        validatePassword(schedule, requestDto);

        return schedule.getId();
    }

    private Schedule findScheduleById(Long id) {
        return scheduleRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("선택한 일정은 존재하지 않습니다."));
    }

    private void validatePassword(Schedule schedule, ScheduleRequestDto requestDto) {
        if (requestDto.getPassword().equals(schedule.getPassword())) {
            schedule.update(requestDto);
        }else {
            throw new ScheduleException(ErrorCode.INVALID_PASSWORD);
        }
    }
}
