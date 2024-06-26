package com.sparta.nbcampspringpersonaltask.dto;

import com.sparta.nbcampspringpersonaltask.Entity.Schedule;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
public class ScheduleResponseDto {

    private Long id;                    // 번호
    private String title;               // 제목
    private String content;             // 내용
    private String writer;              // 담당자
    private LocalDateTime createdAt;    // 작성일
    private LocalDateTime modifiedAt;   // 수정일

    public ScheduleResponseDto(Schedule schedule) {
        this.id = schedule.getId();
        this.title = schedule.getTitle();
        this.content = schedule.getContent();
        this.writer = schedule.getWriter();
        this.createdAt = schedule.getCreatedAt();
        this.modifiedAt = schedule.getModifiedAt();
    }

    @Builder
    public ScheduleResponseDto(Long id, String title, String content, String writer, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}
