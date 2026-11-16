package com.example.first.specification;

import com.example.first.model.Student;
import org.springframework.data.jpa.domain.Specification;

public class StudentSpecification {

    public static Specification<Student> hasName(String name) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("name")),
                        "%" + name.toLowerCase() + "%"
                );
    }

    public static Specification<Student> hasCourse(String course) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("course")),
                        "%" + course.toLowerCase() + "%"
                );
    }

    public static Specification<Student> hasAgeGreaterThanOrEqual(int age) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(
                        root.get("age"),
                        age
                );
    }

    public static Specification<Student> withFilters(
            String name,
            String course,
            Integer ageFrom,
            Integer ageTo){

        Specification<Student> specification =
                (root, query, criteriaBuilder) -> null;

        if (name != null && !name.isBlank()) {
            specification =
                    specification.and(hasName(name));
        }

        if (course != null && !course.isBlank()) {
            specification =
                    specification.and(hasCourse(course));
        }

        if (ageFrom != null) {
        specification =
                specification.and(
                        hasAgeGreaterThanOrEqual(ageFrom));
        }

        if (ageTo != null) {
        specification =
                specification.and(
                        hasAgeLessThanOrEqual(ageTo));
    }
        return specification;
    }

    public static Specification<Student> hasAgeLessThanOrEqual(int age) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(
                        root.get("age"),
                        age
                );
    }
}