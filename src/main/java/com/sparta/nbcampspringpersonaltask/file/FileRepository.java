package com.sparta.nbcampspringpersonaltask.file;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<File, Long> {
    List<File> findAllByScheduleId(Long scheduleId);
    void deleteAllByScheduleId(Long scheduleId);
}
