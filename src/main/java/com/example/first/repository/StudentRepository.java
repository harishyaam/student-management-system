package com.example.first.repository;

import com.example.first.model.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
public interface StudentRepository
        extends JpaRepository<Student, Integer>,
        JpaSpecificationExecutor<Student> {

    List<Student> findByNameContainingIgnoreCase(String name);

    List<Student> findByCourseContainingIgnoreCase(String course);

    @Query("SELECT s FROM Student s WHERE s.age > :age")
    List<Student> findStudentsOlderThan(
            @Param("age") int age);

    @Query("""
       SELECT s
       FROM Student s
       WHERE s.age > :age
       AND s.course = :course """)

    List<Student> findByAgeGreaterThanAndCourse(
            @Param("age") int age,
            @Param("course") String course);

    Page<Student> findByNameContainingIgnoreCaseAndCourseContainingIgnoreCase(
            String name,
            String course,
            Pageable pageable);
}