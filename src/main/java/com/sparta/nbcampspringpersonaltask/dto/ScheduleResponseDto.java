package com.sparta.nbcampspringpersonaltask.dto;

import com.sparta.nbcampspringpersonaltask.Entity.Schedule;
import com.sparta.nbcampspringpersonaltask.file.FileResponseDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
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
}
