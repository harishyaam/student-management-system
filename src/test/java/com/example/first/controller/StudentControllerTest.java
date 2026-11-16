package com.example.first.controller;

import com.example.first.dto.StudentRequest;
import com.example.first.dto.StudentResponse;
import com.example.first.exception.StudentNotFoundException;
import com.example.first.model.Student;
import com.example.first.service.StudentService;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@WebMvcTest(StudentController.class)
public class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private StudentService studentService;

    @Test
    void getStudentByIdTest() throws Exception {

        StudentResponse response =
                new StudentResponse(1, "Rahul", 21, "CSE");

        when(studentService.getStudentById(1))
                .thenReturn(response);

        mockMvc.perform(get("/students/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Rahul"))
                .andExpect(jsonPath("$.age").value(21))
                .andExpect(jsonPath("$.course").value("CSE"));

        verify(studentService).getStudentById(1);
    }

    @Test
    void addStudentTest() throws Exception {

        StudentRequest request = new StudentRequest();
        request.setName("Anu");
        request.setAge(22);
        request.setCourse("Java");

        Student savedStudent = new Student();
        savedStudent.setName("Anu");
        savedStudent.setAge(22);
        savedStudent.setCourse("Java");

        when(studentService.addStudent(any(Student.class)))
                .thenReturn(savedStudent);

        mockMvc.perform(post("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Anu"))
                .andExpect(jsonPath("$.age").value(22))
                .andExpect(jsonPath("$.course").value("Java"));

        verify(studentService).addStudent(any(Student.class));
    }

    @Test
    void updateStudentTest() throws Exception {

        StudentRequest request = new StudentRequest();
        request.setName("Rahul Updated");
        request.setAge(22);
        request.setCourse("Java");

        StudentResponse response =
                new StudentResponse(1, "Rahul Updated", 22, "Java");

        when(studentService.updateStudent(anyInt(), any(StudentRequest.class)))
                .thenReturn(response);

        mockMvc.perform(put("/students/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Rahul Updated"))
                .andExpect(jsonPath("$.age").value(22))
                .andExpect(jsonPath("$.course").value("Java"));

        verify(studentService)
                .updateStudent(anyInt(), any(StudentRequest.class));
    }

    @Test
    void deleteStudentTest() throws Exception {

        when(studentService.deleteStudent(1))
                .thenReturn(true);

        mockMvc.perform(delete("/students/1"))
                .andExpect(status().isNoContent());

        verify(studentService).deleteStudent(1);
    }

    @Test
    void addStudentValidationTest() throws Exception {

        StudentRequest request = new StudentRequest();

        request.setName("");
        request.setAge(15);
        request.setCourse("");

        mockMvc.perform(post("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(studentService, never())
                .addStudent(any(Student.class));
    }

    @Test
    void getStudentByIdNotFoundTest() throws Exception {

        when(studentService.getStudentById(99))
                .thenThrow(
                        new StudentNotFoundException(
                                "Student with id : 99 NOT FOUND !!!"));

        mockMvc.perform(get("/students/99"))
                .andExpect(status().isNotFound());

        verify(studentService).getStudentById(99);
    }


}
