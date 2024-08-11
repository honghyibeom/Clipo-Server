package myproject.cliposerver.controller;

import lombok.RequiredArgsConstructor;
import myproject.cliposerver.data.dto.ResponseDTO;
import myproject.cliposerver.exception.CustomException;
import myproject.cliposerver.exception.ErrorCode;
import myproject.cliposerver.service.Image.S3ImageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ImageController {
    private final S3ImageService s3ImageService;
    @PostMapping(value = "/s3/upload")
    public ResponseEntity<ResponseDTO> s3Upload(@RequestPart List<MultipartFile> files){
        if (files == null) {
            throw new CustomException(ErrorCode.EMPTY_FILE_EXCEPTION);
        }
        return ResponseEntity.ok(s3ImageService.uploadFileList(files));
    }
}
