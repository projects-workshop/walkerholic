package com.yunhalee.walkerholic.activity.api;

import static com.yunhalee.walkerholic.activity.domain.ActivityTest.FIRST_ACTIVITY;
import static com.yunhalee.walkerholic.activity.domain.ActivityTest.SECOND_ACTIVITY;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.yunhalee.walkerholic.ApiTest;
import com.yunhalee.walkerholic.activity.dto.ActivityDetailResponse;
import com.yunhalee.walkerholic.activity.dto.ActivityRequest;
import com.yunhalee.walkerholic.activity.dto.ActivityResponse;
import com.yunhalee.walkerholic.user.domain.UserTest;
import com.yunhalee.walkerholic.useractivity.domain.ActivityStatus;
import com.yunhalee.walkerholic.useractivity.domain.UserActivity;
import com.yunhalee.walkerholic.useractivity.dto.SimpleUserActivityResponse;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;


public class ActivityApiTest extends ApiTest {

    private static final ActivityRequest REQUEST = new ActivityRequest(FIRST_ACTIVITY.getName(),
        FIRST_ACTIVITY.getScore(),
        FIRST_ACTIVITY.getDescription(),
        FIRST_ACTIVITY.getImageUrl());

    @Test
    void save_image() throws Exception {
        when(activityService.uploadImage(any())).thenReturn(FIRST_ACTIVITY.getImageUrl());
        this.mockMvc.perform(multipart("/api/activities/images").file(MULTIPART_FILE)
            .header(HttpHeaders.AUTHORIZATION, "Bearer token")
            .accept(MediaType.MULTIPART_FORM_DATA))
            .andExpect(status().isOk())
            .andDo(document("activity-save-image", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())));
    }

    @Test
    void create_activity() throws Exception {
        when(activityService.create(any())).thenReturn(new ActivityResponse(FIRST_ACTIVITY));
        this.mockMvc.perform(post("/api/activities")
            .header(HttpHeaders.AUTHORIZATION, "Bearer token")
            .contentType(MediaTypes.HAL_JSON)
            .characterEncoding("utf-8")
            .content(request(REQUEST))
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("activity-create", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()), activityResponseFields()));
    }

    @Test
    void get_activity() throws Exception {
        UserActivity firstUserActivity = UserActivity.builder()
            .status(ActivityStatus.ONGOING)
            .user(UserTest.USER)
            .activity(FIRST_ACTIVITY).build();
        UserActivity secondUserActivity = UserActivity.builder()
            .status(ActivityStatus.FINISHED)
            .user(UserTest.USER)
            .activity(FIRST_ACTIVITY).build();
        when(activityService.activity(any())).thenReturn(new ActivityDetailResponse(FIRST_ACTIVITY, Arrays.asList(SimpleUserActivityResponse.of(firstUserActivity), SimpleUserActivityResponse.of(secondUserActivity))));
        this.mockMvc.perform(get("/api/activities/1")
            .header(HttpHeaders.AUTHORIZATION, "Bearer token")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("activity-get-by-id", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()), activityDetailResponseFields()));
    }

    @Test
    void get_activities() throws Exception {
        when(activityService.activities()).thenReturn(Arrays.asList(new ActivityResponse(FIRST_ACTIVITY), new ActivityResponse(SECOND_ACTIVITY)));
        this.mockMvc.perform(get("/api/activities")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("activity-get-all", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()), activityResponsesFields()));
    }

    @Test
    void update_activity() throws Exception {
        when(activityService.update(any(), any())).thenReturn(new ActivityResponse(FIRST_ACTIVITY));
        this.mockMvc.perform(put("/api/activities/1")
            .header(HttpHeaders.AUTHORIZATION, "Bearer token")
            .contentType(MediaTypes.HAL_JSON)
            .characterEncoding("utf-8")
            .content(request(REQUEST))
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("activity-update", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()), activityResponseFields()));
    }

    @Test
    void delete_activity() throws Exception {
        this.mockMvc.perform(delete("/api/activities/1")
            .header(HttpHeaders.AUTHORIZATION, "Bearer token")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent())
            .andDo(document("activity-delete"));
    }

    public static ResponseFieldsSnippet activityResponseFields() {
        return responseFields(
            fieldWithPath("id").description("activity id"),
            fieldWithPath("name").description("activity name"),
            fieldWithPath("score").description("activity score"),
            fieldWithPath("description").description("activity description"),
            fieldWithPath("imageUrl").description("activity image url"));
    }

    public static ResponseFieldsSnippet activityDetailResponseFields() {
        return responseFields(
            fieldWithPath("id").description("activity id"),
            fieldWithPath("name").description("activity name"),
            fieldWithPath("score").description("activity score"),
            fieldWithPath("description").description("activity description"),
            fieldWithPath("imageUrl").description("activity image url"),
            fieldWithPath("activityUsers").description("userActivity of someone who is doing or has finished this activity"),
            fieldWithPath("activityUsers.[].id").description("userActivity id"),
            fieldWithPath("activityUsers.[].status").description("userActivity's status"),
            fieldWithPath("activityUsers.[].userId").description("user's id"),
            fieldWithPath("activityUsers.[].userImageUrl").description("user's imageUrl"),
            fieldWithPath("activityUsers.[].userFullname").description("user's fullName"),
            fieldWithPath("activityUsers.[].updatedAt").description("last time when user did this activity"));
    }

    public static ResponseFieldsSnippet activityResponsesFields() {
        return responseFields(
            fieldWithPath("[].id").description("activity id"),
            fieldWithPath("[].name").description("activity name"),
            fieldWithPath("[].score").description("activity score"),
            fieldWithPath("[].description").description("activity description"),
            fieldWithPath("[].imageUrl").description("activity image url"));
    }









}
