package project.votebackend.service;

import jakarta.annotation.PostConstruct;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${cloud.aws.cloudfront.domain}")
    private String cloudFrontDomain;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;

    @Value("${cloud.aws.region.static}")
    private String region;

    private S3Client s3Client;

    @PostConstruct
    public void init() {
        s3Client = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)))
                .build();
    }

    //이미지 저장
    public String storeFile(MultipartFile file) {
        try {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            String key = "images/" + fileName;

            // 이미지 리사이즈: 최대 너비 1080px, JPEG 압축
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            Thumbnails.of(file.getInputStream())
                    .size(1080, 1080) // 비율 유지하면서 리사이즈
                    .outputFormat("jpg")
                    .outputQuality(0.5) // 50% 품질로 압축
                    .toOutputStream(os);

            byte[] resizedImage = os.toByteArray();

            PutObjectRequest putRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType("image/jpeg")
                    .build();

            s3Client.putObject(putRequest, RequestBody.fromBytes(resizedImage));

            return "https://" + cloudFrontDomain + "/" + key;

        } catch (IOException e) {
            throw new RuntimeException("S3 이미지 업로드 실패", e);
        }
    }

    //이미지 삭제(프로필, 글 이미지 수정 및 삭제 시)
    public void deleteFile(String fileUrl) {
        try {
            String key = fileUrl.substring(fileUrl.indexOf("images/"));

            DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            s3Client.deleteObject(deleteRequest);
        } catch (Exception e) {
            throw new RuntimeException("S3 이미지 삭제 실패", e);
        }
    }
}

