package com.sparta.nbcampspringpersonaltask.file;

import com.sparta.nbcampspringpersonaltask.exception.ErrorCode;
import com.sparta.nbcampspringpersonaltask.exception.ScheduleException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;

    public void savaFiles(Long scheduleId, List<FileRequestDto> files) {
        if (CollectionUtils.isEmpty(files)) {
            return;
        }
        for (FileRequestDto file : files) {
            file.setScheduleId(scheduleId);
        }
        List<File> fileList = files.stream().map(File::new).toList();
        fileRepository.saveAll(fileList);
    }

    public List<FileResponseDto> findAllFilesByScheduleId(Long scheduleId) {
        return fileRepository.findAllByScheduleId(scheduleId).stream().map(FileResponseDto::new).toList();
    }

    public FileResponseDto findFileById(Long id) {
        File file = fileRepository.findById(id).orElseThrow(()-> new ScheduleException(ErrorCode.FILE_NOT_FOUND));
        return new FileResponseDto(file);
    }

    public void deleteAllByScheduleId(Long id) {
        fileRepository.deleteAllByScheduleId(id);
    }
}
