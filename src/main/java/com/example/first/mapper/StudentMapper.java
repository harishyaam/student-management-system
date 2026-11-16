package com.example.first.mapper;

import com.example.first.dto.StudentRequest;
import com.example.first.dto.StudentResponse;
import com.example.first.model.Student;

public class StudentMapper {

    public static Student toStudent(StudentRequest request) {

        Student student = new Student();

        student.setName(request.getName());
        student.setAge(request.getAge());
        student.setCourse(request.getCourse());

        return student;
    }
    public static StudentResponse toResponse(Student student) {

        return new StudentResponse(
                student.getId(),
                student.getName(),
                student.getAge(),
                student.getCourse());

    }
}