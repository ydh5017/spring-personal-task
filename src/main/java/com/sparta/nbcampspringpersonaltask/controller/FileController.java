package com.sparta.nbcampspringpersonaltask.controller;

import com.sparta.nbcampspringpersonaltask.dto.FileResponseDto;
import com.sparta.nbcampspringpersonaltask.service.FileService;
import com.sparta.nbcampspringpersonaltask.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;
    private final FileUtils fileUtils;

    /**
     * 파일 목록 조회 메서드
     * @param scheduleId 일정 ID
     * @return 파일 목록
     */
    @GetMapping
    public List<FileResponseDto> getAllFiles(Long scheduleId) {
        return fileService.findAllFilesByScheduleId(scheduleId);
    }

    /**
     * 파일 다운로드 메서드
     * @param id 파일 ID
     * @return 파일 다운로드
     */
    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id){
        FileResponseDto file = fileService.findFileById(id);
        Resource resource = fileUtils.readFileAsResource(file);
        try {
            String filename = URLEncoder.encode(file.getFileName(), "UTF-8");
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; fileName=\"" + filename + "\";")
                    .header(HttpHeaders.CONTENT_LENGTH, file.getSize()+"")
                    .body(resource);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("filename encoding failed : " + file.getFileName());
        }
    }
}
