package com.sparta.nbcampspringpersonaltask.Entity;

import com.sparta.nbcampspringpersonaltask.dto.ScheduleRequestDto;
import com.sparta.nbcampspringpersonaltask.enumType.ErrorCode;
import com.sparta.nbcampspringpersonaltask.exception.ScheduleException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "schedule")
@NoArgsConstructor
@AllArgsConstructor
public class Schedule extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "content", nullable = false)
    private String content;
    @Column(name = "writer", nullable = false)
    private String writer;
    @Column(name = "password", nullable = false)
    private String password;

    public Schedule(ScheduleRequestDto scheduleRequestDto) {
        this.title = scheduleRequestDto.getTitle();
        this.content = scheduleRequestDto.getContent();
        this.writer = scheduleRequestDto.getWriter();
        this.password = scheduleRequestDto.getPassword();
    }

    /**
     * 일정 수정
     * @param scheduleRequestDto 일정 요청 DTO
     */
    public void update(ScheduleRequestDto scheduleRequestDto) {
        this.title = scheduleRequestDto.getTitle();
        this.content = scheduleRequestDto.getContent();
        this.writer = scheduleRequestDto.getWriter();
    }

    /**
     * 비밀번호 검증 메서드
     * @param requestDto 요청 DTO
     */
    public void validatePassword(ScheduleRequestDto requestDto) {
        if (!requestDto.getPassword().equals(this.password)) {
            throw new ScheduleException(ErrorCode.INVALID_PASSWORD);
        }
    }
}
