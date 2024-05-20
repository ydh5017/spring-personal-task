package com.sparta.nbcampspringpersonaltask.service;

import com.sparta.nbcampspringpersonaltask.Entity.Schedule;
import com.sparta.nbcampspringpersonaltask.dto.FileRequestDto;
import com.sparta.nbcampspringpersonaltask.dto.ScheduleRequestDto;
import com.sparta.nbcampspringpersonaltask.dto.ScheduleResponseDto;
import com.sparta.nbcampspringpersonaltask.enumType.ErrorCode;
import com.sparta.nbcampspringpersonaltask.exception.ScheduleException;
import com.sparta.nbcampspringpersonaltask.dto.FileResponseDto;
import com.sparta.nbcampspringpersonaltask.utils.FileUtils;
import com.sparta.nbcampspringpersonaltask.repository.ScheduleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final FileService fileService;
    private final FileUtils fileUtils;

    public ScheduleService(ScheduleRepository scheduleRepository, FileService fileService, FileUtils fileUtils) {
        this.scheduleRepository = scheduleRepository;
        this.fileService = fileService;
        this.fileUtils = fileUtils;
    }

    /**
     * 일정 생성 메서드
     * @param requestDto 일정 정보
     * @return 일정 정보
     */
    public ScheduleResponseDto createSchedule(ScheduleRequestDto requestDto) {
        Schedule schedule = new Schedule(requestDto);

        Schedule saveSchedule = scheduleRepository.save(schedule);

        ScheduleResponseDto responseDto = new ScheduleResponseDto(saveSchedule);

        // 파일 업로드
        List<FileRequestDto> files = fileUtils.uploadFiles(requestDto.getFiles());
        // 파일 정보 DB에 저장
        fileService.savaFiles(responseDto.getId(), files);

        return responseDto;
    }

    /**
     * 전체 일정 조회 메서드
     * @return 일정 목록
     */
    public List<ScheduleResponseDto> findAll() {
        return scheduleRepository.findAllByOrderByCreatedAtDesc().stream().map(ScheduleResponseDto::new).toList();
    }

    /**
     * 제목 키워드 일정 목록 조회 메서드
     * @param keyword 키워드
     * @return 일정 목록
     */
    public List<ScheduleResponseDto> getSchedulesByKeyword(String keyword) {
        return scheduleRepository.findAllByTitleContainsOrderByCreatedAtDesc(keyword).stream().map(ScheduleResponseDto::new).toList();
    }

    /**
     * 일정 수정 메서드
     * @param id 일정 ID
     * @param requestDto 수정할 정보, 비말번호
     * @return 일정 ID
     */
    @Transactional
    public Long updateSchedule(Long id, ScheduleRequestDto requestDto) {
        Schedule schedule = findScheduleById(id);
        schedule.validatePassword(requestDto);
        schedule.update(requestDto);
        return schedule.getId();
    }

    /**
     * 일정,파일 삭제 메서드
     * @param id 일정 ID
     * @param requestDto 일정 정보
     * @return 일정 ID
     */
    @Transactional
    public Long deleteSchedule(Long id, ScheduleRequestDto requestDto) {
        Schedule schedule = findScheduleById(id);
        schedule.validatePassword(requestDto);
        scheduleRepository.delete(schedule);

        List<FileResponseDto> files = fileService.findAllFilesByScheduleId(id); // 삭제할 파일 정보 조회
        fileUtils.deleteFiles(files); // 디스크에서 파일 삭제
        fileService.deleteAllByScheduleId(id); // DB에서 파일 정보 삭제
        return schedule.getId();
    }

    /**
     * 일정 객체 반환 메서드
     * @param id 일정 ID
     * @return 일정 객체
     * @throws ScheduleException 일정이 존재하지 않는 경우
     */
    private Schedule findScheduleById(Long id) {
        return scheduleRepository.findById(id).orElseThrow(()-> new ScheduleException(ErrorCode.SCHEDULE_NOT_FOUND));
    }
}
