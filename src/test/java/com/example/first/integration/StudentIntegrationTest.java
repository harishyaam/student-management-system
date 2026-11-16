package com.example.first.integration;

import com.example.first.dto.StudentRequest;
import com.example.first.dto.StudentResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest //Load the application's Spring context for this test
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class StudentIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

//    @Test
//    void getStudentByIdIntegrationTest() throws Exception {
//
//        mockMvc.perform(get("/students/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1));
//    }
    @Autowired
    private ObjectMapper objectMapper;
    @Test
    void createAndGetStudentIntegrationTest() throws Exception {

        StudentRequest request = new StudentRequest();
        request.setName("Test Student");
        request.setAge(21);
        request.setCourse("Java");

        String response = mockMvc.perform(
                        post("/students")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        StudentResponse createdStudent =
                objectMapper.readValue(response, StudentResponse.class);

        int id = createdStudent.getId();

        mockMvc.perform(get("/students/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Test Student"))
                .andExpect(jsonPath("$.age").value(21))
                .andExpect(jsonPath("$.course").value("Java"));
    }

}