package com.sparta.nbcampspringpersonaltask.service;

import com.sparta.nbcampspringpersonaltask.Entity.File;
import com.sparta.nbcampspringpersonaltask.dto.FileRequestDto;
import com.sparta.nbcampspringpersonaltask.dto.FileResponseDto;
import com.sparta.nbcampspringpersonaltask.enumType.ErrorCode;
import com.sparta.nbcampspringpersonaltask.exception.ScheduleException;
import com.sparta.nbcampspringpersonaltask.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;

    /**
     * 파일 정보 저장 메서드
     * @param scheduleId 일정 ID
     * @param files 파일 목록
     */
    public void savaFiles(Long scheduleId, List<FileRequestDto> files) {
        // 파일 목록이 비었는지 확인
        if (CollectionUtils.isEmpty(files)) {
            return;
        }
        // 파일 객체에 일정 ID를 주입해준다.
        for (FileRequestDto file : files) {
            file.setScheduleId(scheduleId);
        }
        List<File> fileList = files.stream().map(File::new).toList();
        fileRepository.saveAll(fileList);
    }

    /**
     * 일정 첨부파일 목록 조회 메서드
     * @param scheduleId 일정 ID
     * @return 파일 목록
     */
    public List<FileResponseDto> findAllFilesByScheduleId(Long scheduleId) {
        return fileRepository.findAllByScheduleId(scheduleId).stream().map(FileResponseDto::new).toList();
    }

    /**
     * 파일 객체 반환 메서드
     * @param id 파일 ID
     * @return 파일
     */
    public FileResponseDto findFileById(Long id) {
        File file = fileRepository.findById(id).orElseThrow(()-> new ScheduleException(ErrorCode.FILE_NOT_FOUND));
        return new FileResponseDto(file);
    }

    /**
     * 파일 정보 삭제 메서드
     * @param id 파일 ID
     */
    public void deleteAllByScheduleId(Long id) {
        fileRepository.deleteAllByScheduleId(id);
    }
}
