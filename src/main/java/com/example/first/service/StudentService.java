package com.example.first.service;

import com.example.first.dto.PageResponse;
import com.example.first.dto.StudentRequest;
import com.example.first.dto.StudentResponse;
import com.example.first.exception.StudentNotFoundException;
import com.example.first.mapper.StudentMapper;
import com.example.first.model.Student;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.first.repository.StudentRepository;
import com.example.first.specification.StudentSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import com.example.first.enums.SortDirection;
import org.springframework.stereotype.Service;
import com.example.first.enums.SortField;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


@Service
public class StudentService {

    private final StudentRepository studentRepository;


    public StudentService(StudentRepository studentRepository) {

        this.studentRepository = studentRepository;
    }

    private static final Logger logger =
            LoggerFactory.getLogger(StudentService.class);

    // ADD STUDENT

    public Student addStudent(Student student) {

        logger.info("Attempting to add a new student");
        logger.debug("Student details received for processing");

        if (student.getAge() < 18) {

            logger.warn("Student creation rejected: age is below 18");

            return null;
        }

        Student savedStudent = studentRepository.save(student);

        logger.info("Student created successfully with ID: {}", savedStudent.getId());

        return savedStudent;
    }

    //UPDATE STUDENT
    public StudentResponse updateStudent(int id, StudentRequest request) {

        Student student = studentRepository.findById(id).orElseThrow(()
                        -> new StudentNotFoundException(
                                "Student with id : " + id + " NOT FOUND !!!"));

        student.setName(request.getName());
        student.setAge(request.getAge());
        student.setCourse(request.getCourse());

        Student updatedStudent = studentRepository.save(student);

        return StudentMapper.toResponse(updatedStudent);
    }

    public StudentResponse getStudentById(int id) {

        Student student = studentRepository.findById(id).orElseThrow(()
                        -> new StudentNotFoundException("Student with id : " + id + " NOT FOUND !!!"));

        return StudentMapper.toResponse(student);
    }

    public List<Student> findStudentsOlderThan(int age) {

        return studentRepository.findStudentsOlderThan(age);
    }

    public List<Student> findByAgeGreaterThanAndCourse(
            int age,
            String course) {

        return studentRepository
                .findByAgeGreaterThanAndCourse(age, course);
    }

public boolean deleteStudent(int id) {

    if (!studentRepository.existsById(id)) {
        return false;
    }

    studentRepository.deleteById(id);
    return true;
}

//FILTERING + SORTING + PAGINATION

// SAFE SORTING (Sorting Helper Methods 1)
private SortField getValidSortField(String sortBy) {

    if (sortBy.equalsIgnoreCase("name")) {
        return SortField.NAME;
    }

    if (sortBy.equalsIgnoreCase("age")) {
        return SortField.AGE;
    }

    if (sortBy.equalsIgnoreCase("course")) {
        return SortField.COURSE;
    }

    return SortField.ID;
}

//(Sorting Helper Methods 3)
    private SortDirection getValidDirection(String direction) {

        if (direction.equalsIgnoreCase("desc")) {
            return SortDirection.DESC;
        }

        return SortDirection.ASC;
    }

    //(Sorting Helper Methods 2)
    private String getSortProperty(SortField sortField) {

        return switch (sortField) {
            case ID -> "id";
            case NAME -> "name";
            case AGE -> "age";
            case COURSE -> "course";
        };
    }

    public PageResponse<StudentResponse> getPaginatedStudents(
            int page,
            int size,
            String sortBy,
            String direction,
            String name,
            String course,
            Integer ageFrom,
            Integer ageTo) {

        Specification<Student> specification =
                StudentSpecification.withFilters(
                        name,
                        course,
                        ageFrom,
                        ageTo
                );

        if (ageFrom != null && ageTo != null && ageFrom > ageTo) {
            throw new IllegalArgumentException(
                    "ageFrom cannot be greater than ageTo"
            );
        }

//Sort Field
    SortField validSortField =
            getValidSortField(sortBy);

    String sortProperty =
            getSortProperty(validSortField);

    SortDirection validDirection =
            getValidDirection(direction);

    Sort sort;

    if (validDirection == SortDirection.DESC) {
        sort = Sort.by(sortProperty).descending();
    } else {
        sort = Sort.by(sortProperty).ascending();
    }

    Pageable pageable =
            PageRequest.of(page, size, sort);

    Page<Student> studentPage =
            studentRepository.findAll(
                    specification,
                    pageable
            );

    List<StudentResponse> responses =
            studentPage
                    .map(StudentMapper::toResponse)
                    .getContent();

    return new PageResponse<>(
            responses,
            studentPage.getNumber(),
            studentPage.getSize(),
            (int) studentPage.getTotalElements(),
            studentPage.getTotalPages()
    );
}

// SEARCH BY NAME
    public List<Student> searchByName(String name) {
        return studentRepository
                .findByNameContainingIgnoreCase(name);
    }

// SEARCH BY COURSE
    public List<Student> searchByCourse(String course) {
        return studentRepository
                .findByCourseContainingIgnoreCase(course);
    }
}