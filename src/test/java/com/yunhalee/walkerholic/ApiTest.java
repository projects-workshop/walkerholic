package com.yunhalee.walkerholic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yunhalee.walkerholic.security.jwt.service.JwtUserDetailsService;
import com.yunhalee.walkerholic.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc(addFilters = false) // -> webAppContextSetup(webApplicationContext)
@AutoConfigureRestDocs // -> apply(documentationConfiguration(restDocumentation))
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@TestPropertySource(locations = "/config/application-test.properties")
public abstract class ApiTest {

    @Autowired
    protected MockMvc mockMvc;

    protected static final MockMultipartFile MULTIPART_FILE = new MockMultipartFile("multipartFile", "image.png", "image/png", "image data".getBytes());

    private ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    protected PasswordEncoder passwordEncoder;

    @MockBean
    protected UserService userService;

    @MockBean
    protected JwtUserDetailsService jwtUserDetailsService;

    protected String request(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }


}