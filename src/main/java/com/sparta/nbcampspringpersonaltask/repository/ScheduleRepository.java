package com.sparta.nbcampspringpersonaltask.repository;

import com.sparta.nbcampspringpersonaltask.Entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findAllByOrderByCreatedAtDesc();
    List<Schedule> findAllByTitleContainsOrderByCreatedAtDesc(String keyword);
}
