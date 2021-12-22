package com.yunhalee.walkerholic.common.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import java.util.Objects;
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

    private AmazonS3 s3;

    public S3ImageUploader(AmazonS3 s3) {
        this.s3 = s3;
    }

    public String saveImageByFolder(String uploadDir, MultipartFile multipartFile)
        throws IOException {

        if (isEmpty(multipartFile)) {
            return defaultImageUrl;
        }

        try {
            removeFolder(uploadDir);
            return uploadFile(uploadDir, multipartFile);
        } catch (IOException ex) {
            new IOException("Could not save file : " + multipartFile.getOriginalFilename());
        }
        return "";
    }

    public List<String> listFolder(String folder) {

        ListObjectsV2Request listObjectsV2Request = new ListObjectsV2Request()
            .withBucketName(bucket).withPrefix(folder);

        ListObjectsV2Result listObjectsV2Result = s3.listObjectsV2(listObjectsV2Request);

        ListIterator<S3ObjectSummary> listIterator = listObjectsV2Result.getObjectSummaries()
            .listIterator();

        List<String> list = new ArrayList<>();

        while (listIterator.hasNext()) {
            S3ObjectSummary object = listIterator.next();
            list.add(object.getKey());
        }
        return list;
    }

    public String uploadFile(String folderName, MultipartFile multipartFile) throws IOException {
        File uploadFile = convert(multipartFile)
            .orElseThrow(
                () -> new IllegalArgumentException("MultipartFile 형식을 File로 전환하는 데에 실패하였습니다."));
        return upload(uploadFile, folderName);
    }

    public void deleteFile(String fileName) {
        DeleteObjectRequest request = new DeleteObjectRequest(bucket, fileName);
        s3.deleteObject(request);
    }

    public boolean isEmpty(MultipartFile multipartFile) {
        return Objects.isNull(multipartFile) || multipartFile.isEmpty();
    }

    public void removeFolder(String folderName) {
        ListObjectsV2Request listObjectsV2Request = new ListObjectsV2Request()
            .withBucketName(bucket).withPrefix(folderName + "/");
        ListObjectsV2Result listObjectsV2Result = s3.listObjectsV2(listObjectsV2Request);
        ListIterator<S3ObjectSummary> listIterator = listObjectsV2Result.getObjectSummaries()
            .listIterator();

        while (listIterator.hasNext()) {
            S3ObjectSummary objectSummary = listIterator.next();
            DeleteObjectRequest request = new DeleteObjectRequest(bucket, objectSummary.getKey());
            s3.deleteObject(request);
            System.out.println("Deleted " + objectSummary.getKey());
        }
    }

    private String upload(File uploadFile, String folderName) {
        String fileName = folderName + "/" + uploadFile.getName();
        String uploadImageUrl = putS3(uploadFile, fileName);

        removeNewFile(uploadFile);
        return uploadImageUrl;
    }

    private String putS3(File uploadFile, String fileName) {
        s3.putObject(new PutObjectRequest(bucket, fileName, uploadFile)
            .withCannedAcl(CannedAccessControlList.PublicRead));
        return s3.getUrl(bucket, fileName).toString();
    }

    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            System.out.println("File deleted.");
        } else {
            System.out.println("File doesn't deleted.");
        }
    }

    private Optional<File> convert(MultipartFile file) throws IOException {
        File convertFile = new File(
            System.currentTimeMillis() + StringUtils.cleanPath(file.getOriginalFilename()));

        if (convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }
        return Optional.empty();
    }


}
