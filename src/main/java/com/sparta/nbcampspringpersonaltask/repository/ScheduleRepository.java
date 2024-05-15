package com.sparta.nbcampspringpersonaltask.repository;

import com.sparta.nbcampspringpersonaltask.Entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findAllByOrderByCreatedAtDesc();
    List<Schedule> findAllByTitleContainsOrderByCreatedAtDesc(String keyword);
}
