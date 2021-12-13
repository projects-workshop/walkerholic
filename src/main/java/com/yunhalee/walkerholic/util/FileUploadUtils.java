package com.yunhalee.walkerholic.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.NoSuchElementException;

public class FileUploadUtils {

    //폴더에 사진을 저장
    public static void saveFile(String uploadDir, String fileName, MultipartFile multipartFile)
        throws IOException {
        Path uploadPath = Paths.get(uploadDir);

        //폴더가 존재하지 않는다면 생성해준다.
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            throw new IOException("Could not save file : " + fileName, ex);
        }
    }

    //폴더에 파일이 이미 존재한다면 폴더 비워주기
    public static void cleanDir(String dir) throws IOException {
        Path dirPath = Paths.get(dir);

        try {
            Files.list(dirPath).forEach(file -> {
                if (!Files.isDirectory(file)) {
                    try {
                        Files.delete(file);
                    } catch (IOException ex1) {
                        System.out.println("Could not delete this file : " + file);
                    }
                }
            });
        } catch (IOException ex2) {
            System.out.println("Could not list directory : " + dirPath);
        }
    }

    //폴더 삭제하기
    public static void deleteDir(String dir) {
        Path dirPath = Paths.get(dir);
        if (Files.exists(dirPath)) {
            try {
                cleanDir(dir);
                Files.delete(dirPath);
            } catch (IOException e) {
                System.out.println("Could not delete directory : " + dir);
            }
        }
    }

    //파일 삭제하기
    public static void deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        } else {
            System.out.println("File does not exist. : " + filePath);
        }
    }
}
