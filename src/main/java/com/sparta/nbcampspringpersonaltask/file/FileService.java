package com.sparta.nbcampspringpersonaltask.file;

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
}
