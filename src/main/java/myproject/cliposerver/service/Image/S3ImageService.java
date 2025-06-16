package myproject.cliposerver.service.Image;

import io.awspring.cloud.s3.ObjectMetadata;
import io.awspring.cloud.s3.S3Operations;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myproject.cliposerver.exception.CustomException;
import myproject.cliposerver.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3ImageService {

    private final S3Client s3Client;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    public List<String> uploadFileList(List<MultipartFile> multipartFileList) {
        List<String> fileNameList = new ArrayList<>();
        multipartFileList.forEach(file -> {
            String name = uploadFile(file);
            fileNameList.add(name);
        });
        return fileNameList;
    }

    public String uploadFile(MultipartFile multipartFile) {
        String fileName = createFileName(multipartFile.getOriginalFilename());

        try {
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(fileName)
                    .contentType(multipartFile.getContentType())
                    .acl(ObjectCannedACL.PUBLIC_READ)
                    .build();

            s3Client.putObject(request, RequestBody.fromInputStream(multipartFile.getInputStream(), multipartFile.getSize()));
        } catch (IOException e) {
            throw new CustomException(ErrorCode.PUT_OBJECT_EXCEPTION);
        }

        return "https://" + bucket + ".s3.ap-northeast-2.amazonaws.com/" + fileName;
    }

    public void deleteFile(String fileUrl) {
        String fileName = extractFileName(fileUrl);

        try {
            DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(fileName)
                    .build();

            s3Client.deleteObject(deleteRequest);
        } catch (Exception e) {
            log.error("S3 delete error", e);
            throw new CustomException(ErrorCode.S3_DELETE_EXCEPTION);
        }
    }

    public boolean doesFileExist(String fileUrl) {
        String fileName = extractFileName(fileUrl);
        try {
            HeadObjectRequest headRequest = HeadObjectRequest.builder()
                    .bucket(bucket)
                    .key(fileName)
                    .build();

            s3Client.headObject(headRequest);
            return true;
        } catch (NoSuchKeyException e) {
            return false;
        } catch (Exception e) {
            log.error("S3 check existence error", e);
            throw new CustomException(ErrorCode.S3_CHECK_FILE_EXISTENCE_EXCEPTION);
        }
    }

    private String extractFileName(String fullUrl) {
        String prefix = "https://" + bucket + ".s3.ap-northeast-2.amazonaws.com/";
        return fullUrl.replace(prefix, "");
    }

    private String createFileName(String originalFileName) {
        return UUID.randomUUID().toString().concat(getFileExtension(originalFileName));
    }

    private String getFileExtension(String fileName) {
        if (fileName == null || fileName.length() == 0 || !fileName.contains(".")) {
            throw new CustomException(ErrorCode.INVALID_FILE_EXTENTION);
        }

        String ext = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
        List<String> validExts = List.of(".jpg", ".jpeg", ".png");
        if (!validExts.contains(ext)) {
            throw new CustomException(ErrorCode.NO_FILE_EXTENTION);
        }

        return ext;
    }
}
