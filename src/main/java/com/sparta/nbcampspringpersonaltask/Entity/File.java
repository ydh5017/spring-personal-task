package com.sparta.nbcampspringpersonaltask.Entity;

import com.sparta.nbcampspringpersonaltask.dto.FileRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 파일 Entity
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class File extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "scheduleId", nullable = false)
    private Long scheduleId;
    @Column(name = "fileName", nullable = false)
    private String fileName;
    @Column(name = "saveName", nullable = false)
    private String saveName;
    @Column(name = "size", nullable = false)
    private long size;
    @Column(name = "isDeleted")
    private int isDeleted ;

    public File(FileRequestDto fileRequestDto) {
        this.scheduleId = fileRequestDto.getScheduleId();
        this.fileName = fileRequestDto.getFileName();
        this.saveName = fileRequestDto.getSaveName();
        this.size = fileRequestDto.getSize();
    }
}
