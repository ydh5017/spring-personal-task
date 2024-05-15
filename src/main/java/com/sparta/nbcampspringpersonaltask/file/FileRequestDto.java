package com.sparta.nbcampspringpersonaltask.file;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileRequestDto {

    private Long scheduleId;
    private String fileName;
    private String saveName;
    private Long size;

    @Builder
    public FileRequestDto(String fileName, String saveName, Long size) {
        this.fileName = fileName;
        this.saveName = saveName;
        this.size = size;
    }
}
