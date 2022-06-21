package com.yunhalee.walkerholic;

import static org.assertj.core.api.Assertions.assertThat;

import com.yunhalee.walkerholic.util.DatabaseCleanup;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.io.File;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(locations = "/config/application-test.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AcceptanceTest {

    public final File imageFile = new File(getClass().getClassLoader().getResource("image1.jpeg").getPath());
    public static final String TEST_IMAGE_URL = "https://walkerholic-with-you.s3.ap-northeast-2.amazonaws.com/testImage/image.jpeg";

    @LocalServerPort
    int port;

    @Autowired
    private DatabaseCleanup databaseCleanup;

    @BeforeEach
    public void setUp() {
        if (RestAssured.port == RestAssured.UNDEFINED_PORT) {
            RestAssured.port = port;
            databaseCleanup.afterPropertiesSet();
        }
        databaseCleanup.execute();
    }


    public static ExtractableResponse<Response> create_request(Object request, String uri, String token) {
        return RestAssured
            .given().log().all()
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(request)
            .when().post("/api" + uri)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> update_Request(Object request, String uri, String token) {
        return RestAssured
            .given().log().all()
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(request)
            .when().put("/api" + uri)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> update_Request(String uri, String token) {
        return RestAssured
            .given().log().all()
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().put("/api" + uri)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> find_request(String uri, String token) {
        return RestAssured
            .given().log().all()
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/api" + uri)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> find_request(String uri) {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/api" + uri)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> delete_request(String uri, String token) {
        return RestAssured
            .given().log().all()
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().delete("/api" + uri)
            .then().log().all()
            .extract();
    }

    public static void check_create_response(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void check_ok_response(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void check_delete_response(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static void check_unauthorized_response(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}