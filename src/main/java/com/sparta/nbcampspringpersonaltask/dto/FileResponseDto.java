package com.sparta.nbcampspringpersonaltask.dto;

import com.sparta.nbcampspringpersonaltask.Entity.File;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class FileResponseDto {

    private Long id;                    // 파일 번호
    private Long scheduleId;            // 일정 번호
    private String fileName;            // 파일 이름
    private String saveName;            // 저장할 파일 이름
    private Long size;                  // 파일 크기
    private LocalDateTime createdAt;    // 생성일
    private LocalDateTime modifiedAt;   // 수정일

    public FileResponseDto(File file) {
        this.id = file.getId();
        this.scheduleId = file.getScheduleId();
        this.fileName = file.getFileName();
        this.saveName = file.getSaveName();
        this.size = file.getSize();
        this.createdAt = file.getCreatedAt();
        this.modifiedAt = file.getModifiedAt();
    }
}
