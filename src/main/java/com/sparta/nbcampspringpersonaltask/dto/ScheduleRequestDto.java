package com.sparta.nbcampspringpersonaltask.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ScheduleRequestDto {

    @NotEmpty(message = "제목을 작성해주세요.")
    @Size(max = 200, message = "제목은 최대 200자 이내로 작성해주세요.")
    private String title;

    @NotEmpty(message = "내용을 작성해주세요.")
    @Size(max = 400, message = "내용은 최대 400자 이내로 작성해주세요.")
    private String content;

    @NotEmpty(message = "담당자를 작성해주세요.")
    @Email(message = "담당자는 이메일 형식으로 작성해주세요.")
    private String writer;

    @NotEmpty(message = "비밀번호를 입력해주세요.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*?_]).{8,16}$",
            message = "비밀번호는 최소 8자에서 16자까지, 영문자, 숫자 및 특수 문자를 포함해야 합니다.")
    private String password;

    private List<MultipartFile> files = new ArrayList<>();

    @Builder
    public ScheduleRequestDto(String title, String content, String writer, String password, List<MultipartFile> files) {
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.password = password;
        this.files = files;
    }
}
