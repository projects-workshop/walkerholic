package com.yunhalee.walkerholic.useractivity.api;

import static com.yunhalee.walkerholic.activity.domain.ActivityTest.FIRST_ACTIVITY;
import static com.yunhalee.walkerholic.user.domain.UserTest.FIRST_USER;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.yunhalee.walkerholic.ApiTest;
import com.yunhalee.walkerholic.user.domain.Level;
import com.yunhalee.walkerholic.user.domain.UserTest;
import com.yunhalee.walkerholic.useractivity.domain.ActivityStatus;
import com.yunhalee.walkerholic.useractivity.domain.UserActivity;
import com.yunhalee.walkerholic.useractivity.dto.UserActivityRequest;
import com.yunhalee.walkerholic.useractivity.dto.UserActivityResponse;
import com.yunhalee.walkerholic.useractivity.dto.UserActivityResponses;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;

public class UserActivityApiTest extends ApiTest {

    private UserActivityRequest REQUEST = new UserActivityRequest(FIRST_USER.getId(), 10, FIRST_ACTIVITY.getId(), true);

    private static final UserActivity FIRST_USER_ACTIVITY = UserActivity.builder()
        .status(ActivityStatus.ONGOING)
        .user(UserTest.USER)
        .activity(FIRST_ACTIVITY).build();
    private static final UserActivity SECOND_USER_ACTIVITY = UserActivity.builder()
        .status(ActivityStatus.FINISHED)
        .user(UserTest.USER)
        .activity(FIRST_ACTIVITY).build();

    @Test
    void create_user_activity() throws Exception {
        when(userActivityService.create(any())).thenReturn(new UserActivityResponse(FIRST_USER_ACTIVITY));
        this.mockMvc.perform(post("/api/user-activities")
            .header(HttpHeaders.AUTHORIZATION, "Bearer token")
            .contentType(MediaTypes.HAL_JSON)
            .characterEncoding("utf-8")
            .content(request(REQUEST))
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("user-activity-create", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()), userActivityResponseFields()));
    }

    @Test
    void get_activities() throws Exception {
        when(userActivityService.userActivities(any(), anyInt())).thenReturn(new UserActivityResponses(
            Arrays.asList(new UserActivityResponse(FIRST_USER_ACTIVITY, Level.Starter.getName()), new UserActivityResponse(SECOND_USER_ACTIVITY, Level.Starter.getName())), 2L, 1, 20));
        this.mockMvc.perform(get("/api/users/1/user-activities")
            .header(HttpHeaders.AUTHORIZATION, "Bearer token")
            .param("page", "1")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("user-activity-get-all", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()), userActivityResponsesFields()));
    }


    @Test
    void update_user_activity() throws Exception {
        when(userActivityService.update(any(), anyInt())).thenReturn(new UserActivityResponse(FIRST_USER_ACTIVITY));
        this.mockMvc.perform(put("/api/user-activities/1")
            .header(HttpHeaders.AUTHORIZATION, "Bearer token")
            .contentType(MediaTypes.HAL_JSON)
            .characterEncoding("utf-8")
            .content(request(REQUEST))
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("user-activity-update", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()), userActivityResponseFields()));
    }

    @Test
    void delete_user_activity() throws Exception {
        this.mockMvc.perform(delete("/api/users/1/user-activities/1")
            .header(HttpHeaders.AUTHORIZATION, "Bearer token")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("user-activity-delete"));
    }


    public static ResponseFieldsSnippet userActivityResponseFields() {
        return responseFields(
            fieldWithPath("id").description("user's activity id"),
            fieldWithPath("activityId").description("activity id"),
            fieldWithPath("activityImageUrl").description("activity imageUrl"),
            fieldWithPath("activityName").description("activity name"),
            fieldWithPath("score").description("activity score"),
            fieldWithPath("finished").description("whether user's activity is finished or not"),
            fieldWithPath("createdAt").description("the time when user's activity has started"),
            fieldWithPath("updatedAt").description("the time when user's activity has recently updated"),
            fieldWithPath("level").description("user's current level"));
    }

    public static ResponseFieldsSnippet userActivityResponsesFields() {
        return responseFields(
            fieldWithPath("activities").description("user's activities"),
            fieldWithPath("activities.[].id").description("user's activity id"),
            fieldWithPath("activities.[].activityId").description("activity id"),
            fieldWithPath("activities.[].activityImageUrl").description("activity imageUrl"),
            fieldWithPath("activities.[].activityName").description("activity name"),
            fieldWithPath("activities.[].score").description("activity score"),
            fieldWithPath("activities.[].finished").description("whether user's activity is finished or not"),
            fieldWithPath("activities.[].createdAt").description("the time when user's activity has started"),
            fieldWithPath("activities.[].updatedAt").description("the time when user's activity has recently updated"),
            fieldWithPath("activities.[].level").description("user's current level"),
            fieldWithPath("totalPage").description("the number of total pages"),
            fieldWithPath("totalElement").description("the number of total userActivities"),
            fieldWithPath("score").description("user's current score")
            );
    }


}
