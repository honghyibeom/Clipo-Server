package myproject.cliposerver.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import myproject.cliposerver.exception.CustomException;
import myproject.cliposerver.exception.ErrorCode;
import myproject.cliposerver.service.Image.S3ImageService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ImageController {
    private final S3ImageService s3ImageService;
    @Operation(summary = "이미지 업로드 테스트",description = "이미지 업로드 테스트 api")
    @PostMapping(value = "/s3/upload")
    public List<String> s3Upload(@RequestPart List<MultipartFile> files){
        if (files == null) {
            throw new CustomException(ErrorCode.EMPTY_FILE_EXCEPTION);
        }
        return s3ImageService.uploadFileList(files);
    }
}
