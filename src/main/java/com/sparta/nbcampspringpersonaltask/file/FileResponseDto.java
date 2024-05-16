package com.sparta.nbcampspringpersonaltask.file;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class FileResponseDto {

    private Long id;
    private Long scheduleId;
    private String fileName;
    private String saveName;
    private Long size;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

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
