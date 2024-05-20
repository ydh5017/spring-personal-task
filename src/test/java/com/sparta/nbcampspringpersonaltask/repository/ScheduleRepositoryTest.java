package com.sparta.nbcampspringpersonaltask.repository;

import com.sparta.nbcampspringpersonaltask.Entity.Schedule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ScheduleRepositoryTest {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @DisplayName("일정 등록 성공")
    @Test
    void createSchedule() {
        Schedule schedule = schedule();

        Schedule savedSchedule = scheduleRepository.save(schedule);

        assertThat(savedSchedule.getId()).isEqualTo(schedule.getId());
        assertThat(savedSchedule.getTitle()).isEqualTo(schedule.getTitle());
        assertThat(savedSchedule.getContent()).isEqualTo(schedule.getContent());
        assertThat(savedSchedule.getWriter()).isEqualTo(schedule.getWriter());
    }

    @DisplayName("일정 목록 조회 성공")
    @Test
    void findAllByOrderByCreatedAtDesc() {
        scheduleRepository.saveAll(scheduleList());

        List<Schedule> scheduleList = scheduleRepository.findAllByOrderByCreatedAtDesc();

        assertThat(scheduleList.size()).isEqualTo(10);
    }

    @DisplayName("제목 키워드 일정 목록 조회 성공")
    @Test
    void findAllByTitleContainsOrderByCreatedAtDesc() {
        scheduleRepository.saveAll(scheduleList());
        scheduleRepository.save(new Schedule(11L, "title", "content", "test@writer", "1q2w3e!@#"));
        scheduleRepository.save(new Schedule(12L, "title", "content", "test@writer", "1q2w3e!@#"));

        List<Schedule> scheduleList = scheduleRepository.findAllByTitleContainsOrderByCreatedAtDesc("test");
        assertThat(scheduleList.size()).isEqualTo(10);
    }

    @DisplayName("일정 삭제 성공")
    @Test
    void deleteSchedule() {
        Schedule schedule = addSchedule();

        scheduleRepository.delete(schedule);

        assertThat(scheduleRepository.findAll().size()).isEqualTo(9);
    }

    private Schedule addSchedule() {
        List<Schedule> scheduleList = scheduleRepository.saveAll(scheduleList());
        return scheduleList.get(0);
    }

    private Schedule schedule() {
        return new Schedule(1L,
                "testTitle",
                "testContent",
                "test@writer",
                "1q2w3e!@#");
    }

    private List<Schedule> scheduleList() {
        List<Schedule> scheduleList = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            scheduleList.add(new Schedule((long) i,
                    "testTitle",
                    "testContent",
                    "test@writer",
                    "1q2w3e!@#"));
        }
        return scheduleList;
    }
}