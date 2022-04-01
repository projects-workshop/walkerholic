package com.yunhalee.walkerholic.util;

import com.yunhalee.walkerholic.common.service.S3ImageUploader;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
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

    private static final String FOLDER_NAME = "testUploads";
    private static final String ORIGINAL_FILE_NAME = "sampleFile.txt";
    private MultipartFile multipartFile;

    @BeforeEach
    public void setUp() {
        multipartFile = new MockMultipartFile("uploaded-file",
            ORIGINAL_FILE_NAME,
            "text/plain",
            "This is the file content".getBytes()) {
        };
    }

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

        //when
        String imageUrl = s3ImageUploader.uploadFile(FOLDER_NAME, multipartFile);

        //then
        System.out.println(imageUrl);

    }

    @Test
    public void testDeleteFile() throws IOException {
        //given
        s3ImageUploader.uploadFile(FOLDER_NAME, multipartFile);

        //when
        s3ImageUploader.deleteFile(FOLDER_NAME + "/" + ORIGINAL_FILE_NAME);
        List<String> list = s3ImageUploader.listFolder(FOLDER_NAME);

        //then
        for (String s : list) {
            System.out.println(s);
        }
    }

    @Test
    public void testDeleteFolder() throws IOException {
        //given
        s3ImageUploader.uploadFile(FOLDER_NAME, multipartFile);

        //when
        s3ImageUploader.removeFolder(FOLDER_NAME);
        List<String> list = s3ImageUploader.listFolder(FOLDER_NAME);

        //then
        assertEquals(list.size(), 0);
    }

}
