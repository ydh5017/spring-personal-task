package com.sparta.nbcampspringpersonaltask.controller;

import com.sparta.nbcampspringpersonaltask.dto.ScheduleRequestDto;
import com.sparta.nbcampspringpersonaltask.dto.ScheduleResponseDto;
import com.sparta.nbcampspringpersonaltask.exception.ErrorCode;
import com.sparta.nbcampspringpersonaltask.exception.ScheduleException;
import com.sparta.nbcampspringpersonaltask.file.FileRequestDto;
import com.sparta.nbcampspringpersonaltask.file.FileService;
import com.sparta.nbcampspringpersonaltask.file.FileUtils;
import com.sparta.nbcampspringpersonaltask.service.ScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final FileService fileService;
    private final FileUtils fileUtils;

    @PostMapping("/schedules")
    public ScheduleResponseDto createSchedule(@Valid ScheduleRequestDto requestDto) {
        ScheduleResponseDto responseDto = scheduleService.createSchedule(requestDto);
        List<FileRequestDto> files = fileUtils.uploadFiles(requestDto.getFiles());
        fileService.savaFiles(responseDto.getId(), files);
        return responseDto;
    }

    @GetMapping("/schedules")
    public List<ScheduleResponseDto> getAllSchedules() {
        return scheduleService.findAll();
    }

    @GetMapping("/schedules/contents")
    public List<ScheduleResponseDto> getSchedulesByKeyword(String keyword) {
        return scheduleService.getSchedulesByKeyword(keyword);
    }

    @PutMapping("/schedules/{id}")
    public Long updateSchedule(@PathVariable Long id, @RequestBody @Valid ScheduleRequestDto requestDto) {
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
