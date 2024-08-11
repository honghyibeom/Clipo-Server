package myproject.cliposerver.service.Image;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import myproject.cliposerver.data.dto.ResponseDTO;
import myproject.cliposerver.exception.CustomException;
import myproject.cliposerver.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Slf4j
@RequiredArgsConstructor
@Component
public class S3ImageService {
    private final AmazonS3 s3Client;

    @Value("${cloud.aws.s3.bucketName}")
    private String bucket;
    public ResponseDTO uploadFileList(List<MultipartFile> multipartFile) {
        List<String> fileNameList = new ArrayList<>();

        multipartFile.forEach(file -> {
            String fileName = createFileName(file.getOriginalFilename());
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.getSize());
            objectMetadata.setContentType(file.getContentType());

            try(InputStream inputStream = file.getInputStream()) {
                s3Client.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead));
            } catch(IOException e) {
                throw new CustomException(ErrorCode.PUT_OBJECT_EXCEPTION);
            }

            fileNameList.add("https://clipo-bucket-123123.s3.ap-northeast-2.amazonaws.com/"+fileName);
        });

        return ResponseDTO.builder()
                .body(fileNameList)
                .message("이미지 저장 성공")
                .build();
    }

    public String uploadFile(MultipartFile multipartFile) {
            String fileName = createFileName(multipartFile.getOriginalFilename());
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(multipartFile.getSize());
            objectMetadata.setContentType(multipartFile.getContentType());

            try(InputStream inputStream = multipartFile.getInputStream()) {
                s3Client.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead));
            } catch(IOException e) {
                throw new CustomException(ErrorCode.PUT_OBJECT_EXCEPTION);
            }

        return "https://clipo-bucket-123123.s3.ap-northeast-2.amazonaws.com/"+fileName;
    }



    // 파일 삭제
    public void deleteFile(String fileName) {

        s3Client.deleteObject(new DeleteObjectRequest(bucket, fileName));
    }

    // 파일명 중복 방지 (UUID)
    private String createFileName(String fileName) {
        return UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }
    // 파일 유효성 검사
    private String getFileExtension(String fileName) {
        if (fileName.length() == 0) {
            throw new CustomException(ErrorCode.INVALID_FILE_EXTENTION);
        }
        ArrayList<String> fileValidate = new ArrayList<>();
        fileValidate.add(".jpg");
        fileValidate.add(".jpeg");
        fileValidate.add(".png");
        fileValidate.add(".JPG");
        fileValidate.add(".JPEG");
        fileValidate.add(".PNG");
        String idxFileName = fileName.substring(fileName.lastIndexOf("."));
        if (!fileValidate.contains(idxFileName)) {
            throw new CustomException(ErrorCode.NO_FILE_EXTENTION);
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }
}


