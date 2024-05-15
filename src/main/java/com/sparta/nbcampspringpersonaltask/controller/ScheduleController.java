package com.sparta.nbcampspringpersonaltask.controller;

import com.sparta.nbcampspringpersonaltask.Entity.Schedule;
import com.sparta.nbcampspringpersonaltask.dto.ScheduleRequestDto;
import com.sparta.nbcampspringpersonaltask.dto.ScheduleResponseDto;
import com.sparta.nbcampspringpersonaltask.exception.ErrorCode;
import com.sparta.nbcampspringpersonaltask.exception.ScheduleException;
import com.sparta.nbcampspringpersonaltask.service.ScheduleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Slf4j
public class ScheduleController {

    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @PostMapping("/schedules")
    public ScheduleResponseDto createSchedule(@RequestBody ScheduleRequestDto requestDto) {
        return scheduleService.createSchedule(requestDto);
    }

    @GetMapping("/schedules")
    public List<ScheduleResponseDto> getAllSchedules() {
        return scheduleService.findAll();
    }

    @GetMapping("/schedules/contents")
    public List<ScheduleResponseDto> getSchedulesByKeyword(String keyword) {
        log.info("Get Schedules by keyword: " + keyword);
        return scheduleService.getSchedulesByKeyword(keyword);
    }

    @PutMapping("/schedules/{id}")
    public Long updateSchedule(@PathVariable Long id, @RequestBody ScheduleRequestDto requestDto) {
        return scheduleService.updateSchedule(id, requestDto);
    }

    @DeleteMapping("/schedules/{id}")
    public Long deleteSchedule(@PathVariable Long id, @RequestBody ScheduleRequestDto requestDto) {
        return scheduleService.deleteSchedule(id, requestDto);
    }

    @GetMapping("/exception/test")
    public String testException() {
        throw new ScheduleException(ErrorCode.INVALID_PASSWORD);
    }
}
