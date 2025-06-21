package com.omniversity.server.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.omniversity.server.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    User user;

    @BeforeEach
    public void setup() {
        user =
    }

    // Define a helper method to read JSON objects
    private String readResources(String resourcePath) throws IOException {
        ClassPathResource resource = new ClassPathResource(resourcePath);
        try (InputStreamReader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        }
    }

    @Test
    void registerExchangeUser_Success() throws Exception {
        // Define the success registration payload
        String jsonPayload = readResources("./testUserJSON/SuccessExchangeStudent.json");

        // Send a request to the registration endpoint and expect response code 201.
        mockMvc.perform(post("/users/register/exchange-user").contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload))
                .andExpect(status().isCreated()); // Expect response code 201
    }

    @Test
    void registerExchangeUser_DuplicatePrivateEmail() throws Exception {
        // Define the duplicate private email registration payload
        String jsonPayload = readResources("./testUserJSON/FailedExchangeStudentPrivateEmail.json");

        // Define the error message that should be returned
        String errMessage = "Error: You have already created an account with this email!";

        // Send a request to the registration endpoint and expect response code 400
        mockMvc.perform(post("/users/register/exchange-user").contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(errMessage)); // Expect response code 400
    }

    @Test
    void registerExchangeUser_DuplicateHomeEmail() throws Exception {
        // Define the duplicate home email registration payload
        String jsonPayload = readResources("./testUserJSON/FailedExchangeStudentHomeEmail.json");

        // Define the error message that should be returned
        String errMessage = "Error: This email has already been used by an another account for verification!";

        // Send a request to the registration endpoint and expect response code 400
        mockMvc.perform(post("/users/register/exchange-user").contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(errMessage)); // Expect response code 400
    }

    @Test
    void registerExchangeUser_DuplicateExchangeEmail() throws Exception {
        // Define the duplicate home email registration payload
        String jsonPayload = readResources("./testUserJSON/FailedExchangeStudentExchangeEmail.json");

        // Define the error message that should be returned
        String errMessage = "Error: This id has already been taken by an another user!";

        // Send a request to the registration endpoint and expect response code 400
        mockMvc.perform(post("/users/register/exchange-user").contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(errMessage)); // Expect response code 400
    }

}
