package com.sparta.nbcampspringpersonaltask.controller;

import com.sparta.nbcampspringpersonaltask.dto.ScheduleRequestDto;
import com.sparta.nbcampspringpersonaltask.dto.ScheduleResponseDto;
import com.sparta.nbcampspringpersonaltask.dto.FileRequestDto;
import com.sparta.nbcampspringpersonaltask.service.FileService;
import com.sparta.nbcampspringpersonaltask.utils.FileUtils;
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

    /**
     * 일정 등록하는 메서드
     * @param requestDto 일정 요청 DTO(제목, 담당자, 내용, 비밀번호, 첨부파일)
     * @return 일정 응답 DTO
     */
    @PostMapping("/schedules")
    public ScheduleResponseDto createSchedule(@Valid ScheduleRequestDto requestDto) {
        return scheduleService.createSchedule(requestDto);
    }

    /**
     * 전체 일정 목록 조회 메서드
     * @return 일정 목록
     */
    @GetMapping("/schedules")
    public List<ScheduleResponseDto> getAllSchedules() {
        return scheduleService.findAll();
    }

    /**
     * 제목 키워드 검색 일정 목록 조회 메서드
     * @param keyword 검색 키워드
     * @return 일정 목록
     */
    @GetMapping("/schedules/contents")
    public List<ScheduleResponseDto> getSchedulesByKeyword(String keyword) {
        return scheduleService.getSchedulesByKeyword(keyword);
    }

    /**
     * 일정 수정 메서드
     * @param id 일정 ID
     * @param requestDto 수정할 값과 비밀번호
     * @return 일정 ID
     */
    @PutMapping("/schedules/{id}")
    public Long updateSchedule(@PathVariable Long id, @RequestBody @Valid ScheduleRequestDto requestDto) {
        return scheduleService.updateSchedule(id, requestDto);
    }

    /**
     * 일정 삭제 메서드
     * @param id 일정 ID
     * @param requestDto 비밀번호
     * @return 일정 ID
     */
    @DeleteMapping("/schedules/{id}")
    public Long deleteSchedule(@PathVariable Long id, @RequestBody ScheduleRequestDto requestDto) {
        return scheduleService.deleteSchedule(id, requestDto);
    }
}
