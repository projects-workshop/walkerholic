package com.yunhalee.walkerholic.common.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.multipart.MultipartFile;

public class FakeS3ImageUploader extends S3ImageUploader {

    private List<String> files = new ArrayList<>();

    public FakeS3ImageUploader() {
        super(null);
    }

    @Override
    public String saveImageByFolder(String uploadDir, MultipartFile multipartFile) throws IOException {
        String fileName = uploadDir + "/" + multipartFile.getName();
        files.add(fileName);
        return fileName;
    }

    @Override
    public void deleteOriginalImage(String originalImageUrl, String changedImageUrl) {
    }

    @Override
    public List<String> listFolder(String folder) {
        return files.stream()
            .filter(file -> file.contains(folder))
            .collect(Collectors.toList());
    }

    @Override
    public String uploadFile(String uploadDir, MultipartFile multipartFile) throws IOException {
        String fileName = uploadDir + "/" + multipartFile.getOriginalFilename();
        files.add(fileName);
        return fileName;
    }

    @Override
    public void deleteFile(String fileName) {
        files = files.stream()
            .filter(file -> !file.equals(fileName))
            .collect(Collectors.toList());
    }

    @Override
    public boolean isEmpty(MultipartFile multipartFile) {
        return super.isEmpty(multipartFile);
    }

    @Override
    public void removeFolder(String folderName) {
        files = files.stream()
            .filter(file -> !file.contains(folderName + "/"))
            .collect(Collectors.toList());
    }

    public List<String> getFiles() {
        return Collections.unmodifiableList(files);
    }
}
