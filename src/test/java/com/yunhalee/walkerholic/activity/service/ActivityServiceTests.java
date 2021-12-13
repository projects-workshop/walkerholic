package com.yunhalee.walkerholic.activity.service;

import com.yunhalee.walkerholic.activity.dto.ActivityCreateDTO;
import com.yunhalee.walkerholic.activity.dto.ActivityDTO;
import com.yunhalee.walkerholic.activity.domain.ActivityRepository;
import com.yunhalee.walkerholic.activity.service.ActivityService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ActivityServiceTests {

    @Autowired
    ActivityService activityService;

    @Autowired
    ActivityRepository activityRepository;

    @Test
    public void createActivity() {
        //given
        String name = "testActivity";
        Integer score = 1;
        String description = "This is test Activity.";

        ActivityCreateDTO activityCreateDTO = new ActivityCreateDTO(name, score, description);
        MultipartFile multipartFile = new MockMultipartFile("uploaded-file",
            "sampleFile.txt",
            "text/plain",
            "This is the file content".getBytes());
        //when
        ActivityCreateDTO activityCreateDTO1 = activityService
            .createActivity(activityCreateDTO, multipartFile);

        //then
        assertNotNull(activityCreateDTO1.getId());
        assertEquals(name, activityCreateDTO1.getName());
        assertEquals(score, activityCreateDTO1.getScore());
        assertEquals(description, activityCreateDTO1.getDescription());
        assertEquals("/activityUploads/" + activityCreateDTO1.getId() + "/" + "sampleFile.txt",
            activityCreateDTO1.getImageUrl());
    }

    @Test
    public void updateActivity() {
        //given
        Integer id = 1;
        String originalName = activityRepository.findById(id).get().getName();

        String name = "testUpdateActivity";
        Integer score = 1;
        String description = "This is test Activity.";
        ActivityCreateDTO activityCreateDTO = new ActivityCreateDTO(id, name, score, description);

        //when
        ActivityCreateDTO activityCreateDTO1 = activityService
            .updateActivity(activityCreateDTO, null);

        //then
        assertNotEquals(originalName, activityCreateDTO1.getName());
    }

    @Test
    public void getActivity() {
        //given
        Integer id = 1;

        //when
        ActivityDTO activityDTO = activityService.getActivity(id);

        //then
        assertEquals(id, activityDTO.getId());
    }

    @Test
    public void getActivities() {
        //given

        //when
        List<ActivityCreateDTO> activityCreateDTOS = activityService.getActivities();

        //then
        assertEquals(1, activityCreateDTOS.size());
    }

    @Test
    public void deleteActivity() {
        //given
        Integer id = 1;

        //when
        activityService.deleteActivity(id);

        //then
        assertNull(activityRepository.findById(id));
    }
}
