package com.yunhalee.walkerholic.util;

import com.yunhalee.walkerholic.common.service.S3ImageUploader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class S3ImageUploaderTests {

    @Autowired
    S3ImageUploader s3ImageUploader;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region}")
    private String region;

    @Value("${cloud.aws.credentials.accessKey}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secretKey}")
    private String secretKey;


    @Test
    public void testListFolder() {
        //given
        String folderName = "postUploads";

        //when
        List<String> list = s3ImageUploader.listFolder(folderName);

        //then
        for (String s : list) {
            System.out.println(s);
        }
    }

    @Test
    public void testGetValue() {
        System.out.println(bucket);
    }

    @Test
    public void testUploadFile() throws IOException {
        //given
        String folderName = "testUploads";
        MultipartFile multipartFile = new MockMultipartFile("uploaded-file",
            "sampleFile.txt",
            "text/plain",
            "This is the file content".getBytes()) {
        };

        //when
        String imageUrl = s3ImageUploader.uploadFile(folderName, multipartFile);

        //then
        System.out.println(imageUrl);

    }

    @Test
    public void testDeleteFile() throws IOException {
        //given
        String folderName = "testUploads";
        MultipartFile multipartFile = new MockMultipartFile("uploaded-file",
            "sampleFile.txt",
            "text/plain",
            "This is the file content".getBytes()) {
        };
        s3ImageUploader.uploadFile(folderName, multipartFile);

        //when
        s3ImageUploader.deleteFile("testUploads/sampleFile.txt");
        List<String> list = s3ImageUploader.listFolder(folderName);

        //then
        for (String s : list) {
            System.out.println(s);
        }

    }

    @Test
    public void testDeleteFolder() throws IOException {
        //given
        String folderName = "testUploads";
        MultipartFile multipartFile = new MockMultipartFile("uploaded-file",
            "sampleFile.txt",
            "text/plain",
            "This is the file content".getBytes()) {
        };
        s3ImageUploader.uploadFile(folderName, multipartFile);

        //when
        s3ImageUploader.removeFolder(folderName);
        List<String> list = s3ImageUploader.listFolder(folderName);

        //then
        assertEquals(list.size(), 0);
    }

}
