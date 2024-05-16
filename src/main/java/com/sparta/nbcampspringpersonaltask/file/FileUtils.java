package com.sparta.nbcampspringpersonaltask.file;

import com.sparta.nbcampspringpersonaltask.exception.ErrorCode;
import com.sparta.nbcampspringpersonaltask.exception.ScheduleException;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Component
@Slf4j
public class FileUtils {

    private final Tika tika = new Tika(); // 파일 변조 체크
    private final String uploadPath = Paths.get("/Users","yudonghyeon","Desktop","내일배움캠프","개인과제","upload-files").toString();

    /**
     * 다중 파일 업로드 메서드
     * @param multipartFiles 파일 목록
     * @return DB에 저장할 파일 정보 목록
     */
    public List<FileRequestDto> uploadFiles(List<MultipartFile> multipartFiles) {
        List<FileRequestDto> files = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            if (multipartFile.isEmpty()) {
                continue;
            }
            files.add(uploadFile(multipartFile));
        }
        return files;
    }

    /**
     * 단일 파일 업로드 메서드
     * @param multipartFile 파일
     * @return DB에 저장할 파일 정보
     */
    private FileRequestDto uploadFile(MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            return null;
        }

        if (!isValidImgFile(multipartFile)) {
            throw new ScheduleException(ErrorCode.NOT_IMGFILE);
        }

        String saveName = generateSaveFilename(multipartFile.getOriginalFilename());
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
        String uploadPath = getUploadPath(today) + File.separator + saveName;
        File uploadFile = new File(uploadPath);

        try {
            multipartFile.transferTo(uploadFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return FileRequestDto.builder()
                .fileName(multipartFile.getOriginalFilename())
                .saveName(saveName)
                .size(multipartFile.getSize())
                .build();
    }

    /**
     * 저장할 파일명 생성 메서드
     * @param filename 원본 파일명
     * @return 디스크에 저장할 파일명
     */
    private String generateSaveFilename(final String filename) {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        String extension = StringUtils.getFilenameExtension(filename);
        return uuid + "." + extension;
    }

    /**
     * 업로드 경로 반환 메서드
     * @param addPath 추가 경로
     * @return 업로드 경로
     */
    private String getUploadPath(String addPath) {
        return makeDirectories(uploadPath + File.separator + addPath);
    }

    /**
     * 업로드 폴더 생성 메서드
     * @param path 업로드 경로
     * @return 업로드 경로
     */
    private String makeDirectories(String path) {
        File dir = new File(path);
        if (dir.exists() == false) {
            dir.mkdirs();
        }
        return dir.getPath();
    }

    /**
     * 파일 변조 체크 메서드
     * @param file 파일
     * @return boolean
     */
    private boolean isValidImgFile(MultipartFile file) {
        boolean isValid = true;
        try {
            List<String> notValidTypeList = Arrays.asList("image/jpeg", "image/pjpeg", "image/png", "image/gif", "image/bmp", "image/x-windows-bmp");
            InputStream inputStream = file.getInputStream();
            log.info("getContentType : " + file.getContentType());
            String mimeType = tika.detect(inputStream);
            log.info("mimeType : " + mimeType);

            isValid = notValidTypeList.stream().anyMatch(notValidType -> notValidType.equalsIgnoreCase(mimeType));
            log.info("isValid : " + isValid);
        }catch (IOException e){
            log.error(e.getMessage());
            return isValid;
        }
        return isValid;
    }

    /**
     * 다운로드할 첨부파일 리소스 조회
     * @param file 첨부파일 상세정보
     * @return 첨부파일 리소스
     */
    public Resource readFileAsResource(FileResponseDto file) {
        String uploadDate = file.getCreatedAt().toLocalDate().format(DateTimeFormatter.ofPattern("yyMMdd"));
        String fileName = file.getSaveName();
        Path filePath = Paths.get(uploadPath,uploadDate, fileName);
        System.out.println(filePath);
        try {
            Resource resource = new UrlResource(filePath.toUri());
            if (!resource.exists() || !resource.isFile()) {
                throw new ScheduleException(ErrorCode.FILE_NOT_FOUND);
            }
            return resource;
        } catch (MalformedURLException e) {
            throw new ScheduleException(ErrorCode.FILE_NOT_FOUND);
        }
    }

    /**
     * 다중 파일 삭제 (from Disk)
     * @param files 삭제할 파일 정보 목록
     */
    public void deleteFiles(List<FileResponseDto> files) {
        if (CollectionUtils.isEmpty(files)) {
            return;
        }
        for (FileResponseDto file : files) {
            String uploadDate = file.getCreatedAt().toLocalDate().format(DateTimeFormatter.ofPattern("yyMMdd"));
            deleteFile(uploadDate, file.getSaveName());
        }
    }

    /**
     * 단일 파일 삭제 (from Disk)
     * @param uploadDate 추가 경로
     * @param fileName 저장한 파일명
     */
    private void deleteFile(String uploadDate, String fileName) {
        String filePath = Paths.get(uploadPath,uploadDate,fileName).toString();
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
    }
}
