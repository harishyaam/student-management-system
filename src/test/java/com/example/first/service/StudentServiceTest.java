package com.example.first.service;

import com.example.first.dto.StudentRequest;
import com.example.first.dto.StudentResponse;
import com.example.first.exception.StudentNotFoundException;
import com.example.first.model.Student;
import com.example.first.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)  // connects JUnit 5 with Mockito.
public class StudentServiceTest {

    @Mock   //creates a fake student repository for testing
    private StudentRepository studentRepository;

    @InjectMocks  //Create the StudentService that we want to test and inject our mocks into it.
    private StudentService studentService;

    @Test
    void getStudentByIdTest() {

        Student student = new Student();
//      student.setId(1);
        student.setName("Rahul");
        student.setAge(21);
        student.setCourse("CSE");

        when(studentRepository.findById(1))
                .thenReturn(Optional.of(student));

        StudentResponse result = studentService.getStudentById(1);  //enters your real student Service

        assertEquals("Rahul", result.getName());
        assertEquals(21, result.getAge());
        assertEquals("CSE", result.getCourse());

        verify(studentRepository).findById(1);
    }

    @Test
    void getStudentByIdNotFoundTest() {

        when(studentRepository.findById(99))
                .thenReturn(Optional.empty());

        assertThrows(
                StudentNotFoundException.class,
                () -> studentService.getStudentById(99));

    }

    @Test
    void addStudentTest() {

        Student student = new Student();

        student.setName("Anu");
        student.setAge(22);
        student.setCourse("Java");

        when(studentRepository.save(student))
                .thenReturn(student);

        Student result = studentService.addStudent(student);

        assertEquals("Anu", result.getName());
        assertEquals(22, result.getAge());
        assertEquals("Java", result.getCourse());

        verify(studentRepository).save(student);
    }

    @Test
    void addStudentUnderAgeTest() {

        Student student = new Student();

        student.setName("Arun");
        student.setAge(16);
        student.setCourse("CSE");

        Student result = studentService.addStudent(student);

        assertNull(result);

        verify(studentRepository, never()).save(student);

//        verify(repository).save(student)
//        → save() SHOULD be called
//
//        verify(repository, never()).save(student)
//        → save() SHOULD NOT be called
    }

    @Test
    void deleteStudentTest() {

        when(studentRepository.existsById(1))
                .thenReturn(true);

        boolean result = studentService.deleteStudent(1);

        assertTrue(result);

        verify(studentRepository).deleteById(1);
    }

    @Test
    void deleteStudentNotFoundTest() {

        when(studentRepository.existsById(99))
                .thenReturn(false);

        boolean result = studentService.deleteStudent(99);

        assertFalse(result);

        verify(studentRepository, never()).deleteById(99);
    }

    @Test
    void updateStudentTest() {

        Student student = new Student();

        student.setName("Rahul");
        student.setAge(21);
        student.setCourse("CSE");

        StudentRequest request = new StudentRequest();

        request.setName("Rahul Updated");
        request.setAge(22);
        request.setCourse("Java");

        when(studentRepository.findById(1))
                .thenReturn(Optional.of(student));
        when(studentRepository.save(student))
                .thenReturn(student);

        StudentResponse result = studentService.updateStudent(1, request);

        assertEquals("Rahul Updated", result.getName());
        assertEquals(22, result.getAge());
        assertEquals("Java", result.getCourse());

        verify(studentRepository).save(student);
    }

    @Test
    void updateStudentNotFoundTest() {

        when(studentRepository.findById(99))
                .thenReturn(Optional.empty());

        StudentRequest request = new StudentRequest();

        request.setName("Updated Name");
        request.setAge(22);
        request.setCourse("Java");

        assertThrows(
                StudentNotFoundException.class,
                () -> studentService.updateStudent(99, request));

        verify(studentRepository, never()).save(any(Student.class));

    }

}
