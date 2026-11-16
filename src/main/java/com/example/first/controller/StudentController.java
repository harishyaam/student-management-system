package com.example.first.controller;

import com.example.first.dto.PageResponse;
import com.example.first.service.StudentService;
import com.example.first.mapper.StudentMapper;
import com.example.first.dto.StudentResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.example.first.dto.StudentRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.first.model.Student;

import java.util.List;

@RestController
@RequestMapping("/students")
@Validated
public class StudentController {

    private final StudentService studentService;
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public PageResponse<StudentResponse> getStudents(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "5") @Min(1) int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String course,
            @RequestParam(required = false) @Min(18) Integer ageFrom,
            @RequestParam(required = false) @Min(18) Integer ageTo) {

        return studentService.getPaginatedStudents(
                page,
                size,
                sortBy,
                direction,
                name,
                course,
                ageFrom,
                ageTo);
    }

    @GetMapping("/search")
    public List<Student> searchStudentsByName(
            @RequestParam String name) {

        return studentService.searchByName(name);
    }

    @GetMapping("/search/course")
    public List<Student> searchStudentByCourse(
            @RequestParam String course){
        return studentService.searchByCourse(course);
    }

    @PostMapping
    public ResponseEntity<StudentResponse> addStudent(
            @RequestBody @Valid StudentRequest request) {

        Student student = StudentMapper.toStudent(request);

        Student addedStudent = studentService.addStudent(student);

        if (addedStudent == null) {
            return ResponseEntity.badRequest().build();
        }

        StudentResponse response =
                StudentMapper.toResponse(addedStudent);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentResponse> updateStudent(
            @PathVariable int id,
            @RequestBody @Valid StudentRequest request) {

        StudentResponse response = studentService.updateStudent(id, request);

        if (response != null) {
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(
            @PathVariable int id) {

        boolean deleted = studentService.deleteStudent(id);

        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentResponse> getStudentById(
            @PathVariable int id) {

        StudentResponse response = studentService.getStudentById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/older-than")
    public ResponseEntity<List<StudentResponse>> findStudentsOlderThan(
            @RequestParam int age) {

        List<Student> students = studentService.findStudentsOlderThan(age);
        List<StudentResponse> responses = students.stream().map(StudentMapper::toResponse).toList();

        return ResponseEntity.ok(responses);
        }
    }