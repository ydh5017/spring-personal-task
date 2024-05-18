package com.sparta.nbcampspringpersonaltask.enumType;

import com.sparta.nbcampspringpersonaltask.exception.ScheduleException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum ImgFileType {

    JPEG("image/jpeg"),
    PJPEG("image/pjpeg"),
    PNG("image/png"),
    GIF("image/gif"),
    BMP("image/bmp"),
    X_WINDOWS_BMP("image/x-windows-bmp");

    private final String fileType;

    public static void getImgFileType(String fileType) {
        Arrays.stream(ImgFileType.values())
            .filter(imgFileType -> imgFileType.fileType.equals(fileType))
            .findAny()
            .orElseThrow(() -> new ScheduleException(ErrorCode.NOT_IMGFILE));
    }
}
