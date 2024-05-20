package com.sparta.nbcampspringpersonaltask.service;

import com.sparta.nbcampspringpersonaltask.Entity.Schedule;
import com.sparta.nbcampspringpersonaltask.dto.ScheduleRequestDto;
import com.sparta.nbcampspringpersonaltask.dto.ScheduleResponseDto;
import com.sparta.nbcampspringpersonaltask.repository.ScheduleRepository;
import com.sparta.nbcampspringpersonaltask.utils.FileUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ScheduleServiceTest {

    @InjectMocks
    private ScheduleService scheduleService;

    @Mock
    private ScheduleRepository scheduleRepository;
    @Mock
    private FileService fileService;
    @Mock
    private FileUtils fileUtils;

    @DisplayName("일정 등록 성공")
    @Test
    void createSchedule() {
        ScheduleRequestDto requestDto = scheduleRequestDto();
        Schedule schedule = new Schedule(requestDto);

        doReturn(schedule)
                .when(scheduleRepository)
                .save(any(Schedule.class));

        ScheduleResponseDto responseDto = scheduleService.createSchedule(requestDto);

        assertThat(responseDto.getTitle()).isEqualTo(requestDto.getTitle());
        assertThat(responseDto.getContent()).isEqualTo(requestDto.getContent());
        assertThat(responseDto.getWriter()).isEqualTo(requestDto.getWriter());

        verify(scheduleRepository, times(1)).save(any(Schedule.class));
        verify(fileUtils, times(1)).uploadFiles(any(requestDto.getFiles().getClass()));
    }

    @DisplayName("일정 목록 조회 성공")
    @Test
    void findAll() {
        List<Schedule> scheduleList = new ArrayList<>();
        when(scheduleRepository.findAllByOrderByCreatedAtDesc()).thenReturn(scheduleList);

        List<ScheduleResponseDto> responseDtoList = scheduleService.findAll();

        assertEquals(responseDtoList.size(), scheduleList.size());
    }

    @DisplayName("제목 키워드 일정 목록 조회 성공")
    @Test
    void getSchedulesByKeyword() {
        String keyword = "test";
        doReturn(scheduleResponseList())
                .when(scheduleRepository)
                .findAllByTitleContainsOrderByCreatedAtDesc(any(String.class));

        List<ScheduleResponseDto> responseDtoList = scheduleService.getSchedulesByKeyword(keyword);

        assertThat(responseDtoList.size()).isEqualTo(scheduleResponseList().size());
    }

    @DisplayName("일정 수정 성공")
    @Test
    void updateSchedule() {
        ScheduleRequestDto requestDto = scheduleRequestDto();
        Long id = 1L;
        Optional<Schedule> schedule = Optional.of(
                new Schedule(id, "title", "content", "writer", "1q2w3e!@#"));

        doReturn(schedule).when(scheduleRepository).findById(any(Long.class));

        Long responseId = scheduleService.updateSchedule(id, requestDto);

        assertThat(responseId).isEqualTo(id);
    }

    @DisplayName("일정 삭제 성공")
    @Test
    void deleteSchedule() {
        Long id = 1L;
        ScheduleRequestDto requestDto = scheduleRequestDto();
        Optional<Schedule> schedule = Optional.of(
                new Schedule(id, "title", "content", "writer", "1q2w3e!@#"));

        doReturn(schedule).when(scheduleRepository).findById(any(Long.class));

        Long responseId = scheduleService.deleteSchedule(id, requestDto);

        assertThat(responseId).isEqualTo(id);

    }

    private ScheduleRequestDto scheduleRequestDto() {
        return ScheduleRequestDto.builder()
                .title("testTitle")
                .content("testContent")
                .writer("test@writer")
                .password("1q2w3e!@#")
                .files(multipartFile())
                .build();
    }

    private List<MultipartFile> multipartFile() {
        List<MultipartFile> multipartFiles = new ArrayList<>();
        multipartFiles.add(new MockMultipartFile("testFile1", "test.png", "image/png", "https://file.notion.so/f/f/d591a2ad-c306-4937-8c3c-92cd8fb2174e/c6356e22-532b-4866-8867-29a386f0cb4b/Untitled.png?id=984387cd-7a2a-45ec-a06c-cc0b197710fe&table=block&spaceId=d591a2ad-c306-4937-8c3c-92cd8fb2174e&expirationTimestamp=1716336000000&signature=LKvHT1eQmJZxjVz9UcvRc65iRzfr1FEdi0huXIfgUsI&downloadName=Untitled.png".getBytes()));
        multipartFiles.add(new MockMultipartFile("testFile2", "test.png", "image/png", "https://file.notion.so/f/f/d591a2ad-c306-4937-8c3c-92cd8fb2174e/c6356e22-532b-4866-8867-29a386f0cb4b/Untitled.png?id=984387cd-7a2a-45ec-a06c-cc0b197710fe&table=block&spaceId=d591a2ad-c306-4937-8c3c-92cd8fb2174e&expirationTimestamp=1716336000000&signature=LKvHT1eQmJZxjVz9UcvRc65iRzfr1FEdi0huXIfgUsI&downloadName=Untitled.png".getBytes()));
        return multipartFiles;
    }

    private List<Schedule> scheduleResponseList() {
        List<Schedule> scheduleResponseDtoList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            scheduleResponseDtoList.add(new Schedule(
                    (long) i, "testTitle", "testContent", "test@writer", "1q2w3e!@#"));
        }
        return scheduleResponseDtoList;
    }
}