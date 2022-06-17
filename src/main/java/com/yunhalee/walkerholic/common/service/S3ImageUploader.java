package com.yunhalee.walkerholic.common.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import java.io.FileNotFoundException;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;

@Component
@Slf4j
public class S3ImageUploader {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region}")
    private String region;

    @Value("${cloud.aws.credentials.accessKey}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secretKey}")
    private String secretKey;

    @Value("${AWS_S3_BASE_IMAGE_URL}")
    private String defaultImageUrl;

    @Value("${AWS_S3_BUCKET_URL}")
    private String bucketUrl;

    private AmazonS3 s3;

    public S3ImageUploader(AmazonS3 s3) {
        this.s3 = s3;
    }

    public String saveImageByFolder(String uploadDir, MultipartFile multipartFile) {
        if (isEmpty(multipartFile)) {
            return defaultImageUrl;
        }
        removeFolder(uploadDir);
        return uploadImage(uploadDir, multipartFile);
    }

    public String uploadImage(String uploadDir, MultipartFile multipartFile) {
        try {
            File uploadFile = convert(multipartFile).orElseThrow(() -> new IllegalArgumentException("MultipartFile 형식을 File로 전환하는 데에 실패하였습니다."));
            return upload(uploadFile, uploadDir);
        }catch (IOException e) {
            throw new RuntimeException("Could not convert MultipartFile to file : " + e.getMessage());
        }
    }

    private Optional<File> convert(MultipartFile file) throws IOException {
        File convertFile = new File(System.currentTimeMillis() + StringUtils.cleanPath(file.getOriginalFilename()));
        if (convertFile.createNewFile()) {
            return writeFile(convertFile, file);
        }
        return Optional.empty();
    }

    private Optional<File> writeFile(File convertFile, MultipartFile file) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(convertFile)) {
            fos.write(file.getBytes());
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File does not found : " + e.getMessage());
        }
        return Optional.of(convertFile);
    }

    private String upload(File uploadFile, String folderName) {
        String fileName = folderName + "/" + uploadFile.getName();
        String uploadImageUrl = putS3(uploadFile, fileName);
        removeNewFile(uploadFile);
        return uploadImageUrl;
    }

    private String putS3(File uploadFile, String fileName) {
        s3.putObject(new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));
        return s3.getUrl(bucket, fileName).toString();
    }

    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("File deleted.");
        } else {
            log.info("File doesn't deleted.");
        }
    }

    public String getFileName(String imageUrl, String uploadDir) {
        return imageUrl.substring(bucketUrl.length() + uploadDir.length() + 2);
    }

    public void deleteByFilePath(String filePath) {
        String fileName = filePath.substring(bucketUrl.length() + 1);
        deleteFile(fileName);
    }

    public void deleteOriginalImage(String originalImageUrl, String changedImageUrl) {
        if (!originalImageUrl.equals(defaultImageUrl) ||
            !originalImageUrl.equals(changedImageUrl)) {
            deleteFile(originalImageUrl.replaceAll(bucketUrl, ""));
        }
    }

    public List<String> listFolder(String folder) {
        ListIterator<S3ObjectSummary> listIterator = getFileList(folder);
        return getFileNameList(listIterator);
    }

    private ListIterator<S3ObjectSummary> getFileList(String folder) {
        ListObjectsV2Request listObjectsV2Request = new ListObjectsV2Request().withBucketName(bucket).withPrefix(folder);
        ListObjectsV2Result listObjectsV2Result = s3.listObjectsV2(listObjectsV2Request);
        return listObjectsV2Result.getObjectSummaries().listIterator();
    }

    private List<String> getFileNameList(ListIterator<S3ObjectSummary> listIterator) {
        List<String> list = new ArrayList<>();
        while (listIterator.hasNext()) {
            S3ObjectSummary object = listIterator.next();
            list.add(object.getKey());
        }
        return list;
    }


    public void deleteFile(String fileName) {
        DeleteObjectRequest request = new DeleteObjectRequest(bucket, fileName);
        s3.deleteObject(request);
    }

    public boolean isEmpty(MultipartFile multipartFile) {
        return Objects.isNull(multipartFile) || multipartFile.isEmpty();
    }

    public void removeFolder(String folderName) {
        ListObjectsV2Request listObjectsV2Request = new ListObjectsV2Request().withBucketName(bucket).withPrefix(folderName + "/");
        ListObjectsV2Result listObjectsV2Result = s3.listObjectsV2(listObjectsV2Request);
        ListIterator<S3ObjectSummary> listIterator = listObjectsV2Result.getObjectSummaries().listIterator();
        emptyFolder(listIterator);
    }

    private void emptyFolder(ListIterator<S3ObjectSummary> listIterator) {
        while (listIterator.hasNext()) {
            S3ObjectSummary objectSummary = listIterator.next();
            DeleteObjectRequest request = new DeleteObjectRequest(bucket, objectSummary.getKey());
            s3.deleteObject(request);
            log.info("Deleted " + objectSummary.getKey());
        }
    }

}
