package com.sparta.nbcampspringpersonaltask.controller;

import com.google.gson.Gson;
import com.sparta.nbcampspringpersonaltask.dto.ScheduleRequestDto;
import com.sparta.nbcampspringpersonaltask.dto.ScheduleResponseDto;
import com.sparta.nbcampspringpersonaltask.service.FileService;
import com.sparta.nbcampspringpersonaltask.service.ScheduleService;
import com.sparta.nbcampspringpersonaltask.utils.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockPart;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ScheduleControllerTest {

    @InjectMocks
    private ScheduleController scheduleController;

    @Mock
    private ScheduleService scheduleService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(scheduleController).build();
    }

    @DisplayName("일정 등록 성공")
    @Test
    void createScheduleSuccess() throws Exception {
        // given
        ScheduleRequestDto requestDto = scheduleRequestDto();
        ScheduleResponseDto responseDto = scheduleResponseDto();

        doReturn(responseDto)
                .when(scheduleService)
                .createSchedule(any(ScheduleRequestDto.class));

        // when
//        ResultActions resultActions = mockMvc.perform(
//                MockMvcRequestBuilders.post("/api/schedules")
//                        .contentType(MediaType.MULTIPART_FORM_DATA)
//                        .content(new Gson().toJson(requestDto))
//        );
        // form-data로 받는 경우
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.multipart("/api/schedules")
                        .part(new MockPart("title", requestDto.getTitle().getBytes(StandardCharsets.UTF_8)))
                        .part(new MockPart("content", requestDto.getContent().getBytes(StandardCharsets.UTF_8)))
                        .part(new MockPart("writer", requestDto.getWriter().getBytes(StandardCharsets.UTF_8)))
                        .part(new MockPart("password", requestDto.getPassword().getBytes(StandardCharsets.UTF_8)))
        );

        // then
        MvcResult mvcResult = resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("title", responseDto.getTitle()).exists())
                .andExpect(jsonPath("content", responseDto.getContent()).exists())
                .andExpect(jsonPath("writer", responseDto.getWriter()).exists())
                .andReturn();
    }

    @DisplayName("일정 목록 조회 성공")
    @Test
    void getAllSchedulesSuccess() throws Exception {
        // given
        doReturn(scheduleResponseList())
                .when(scheduleService)
                .findAll();

        // when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/schedules"));

        // then
        MvcResult mvcResult = resultActions.andExpect(status().isOk()).andReturn();

        List responseDtoList = new Gson().fromJson(mvcResult.getResponse().getContentAsString(), List.class);
        assertThat(responseDtoList.size()).isEqualTo(5);
    }

    @DisplayName("제목 키워드 일정 목록 조회 성공")
    @Test
    void getSchedulesByKeywordSuccess() throws Exception {
        // given
        String keyword = "test";
        doReturn(scheduleResponseList())
                .when(scheduleService)
                .getSchedulesByKeyword(keyword);

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/schedules/contents?keyword=" + keyword));

        // then
        MvcResult mvcResult = resultActions.andExpect(status().isOk()).andReturn();
        List responseDtoList = new Gson().fromJson(mvcResult.getResponse().getContentAsString(), List.class);
        assertThat(responseDtoList.size()).isEqualTo(5);
    }

    @DisplayName("일정 수정 성공")
    @Test
    void updateSchedule() throws Exception {
        // given
        ScheduleRequestDto requestDto = scheduleRequestDto();
        Long id = 1L;

        doReturn(id)
                .when(scheduleService)
                .updateSchedule(any(Long.class), any(ScheduleRequestDto.class));

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.put("/api/schedules/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(requestDto))
        );

        // then
        MvcResult mvcResult = resultActions.andExpect(status().isOk()).andReturn();
        assertThat(mvcResult.getResponse().getContentAsString()).isEqualTo(id.toString());
    }

    @DisplayName("일정 삭제 성공")
    @Test
    void deleteSchedule() throws Exception {
        // given
        Long id = 1L;
        ScheduleRequestDto requestDto = scheduleRequestDto();

        doReturn(id)
                .when(scheduleService)
                .deleteSchedule(any(Long.class), any(ScheduleRequestDto.class));

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/schedules/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(requestDto))
        );

        // then
        MvcResult mvcResult = resultActions.andExpect(status().isOk()).andReturn();
        assertThat(mvcResult.getResponse().getContentAsString()).isEqualTo(id.toString());
    }

    private ScheduleRequestDto scheduleRequestDto() {
        return ScheduleRequestDto.builder()
                .title("testTitle")
                .content("testContent")
                .writer("test@writer")
                .password("1q2w3e!@#")
                .build();
    }

    private ScheduleResponseDto scheduleResponseDto() {
        return ScheduleResponseDto.builder()
                .id(1L)
                .title("testTitle")
                .content("testContent")
                .writer("test@writer")
                .build();
    }

    private List<ScheduleResponseDto> scheduleResponseList() {
        List<ScheduleResponseDto> scheduleResponseDtoList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            scheduleResponseDtoList.add(ScheduleResponseDto.builder().id((long) i)
                    .title("testTitle")
                    .content("testContent")
                    .writer("test@writer")
                    .build());
        }
        return scheduleResponseDtoList;
    }
}