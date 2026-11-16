package com.example.first.model;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.GenerationType;

@Entity
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "Course Should Not Be Blank")
    @Size(min = 2,max = 50,message = "Size Should Be in Between 2 and 50 characters")
    private String name;

    @Min(value = 18,message = "Age cannot be under 18")
    private int age;

    @NotBlank
    private String course;


    public Student (int id , String name, int age, String course){
        this.id= id;
        this.name = name;
        this.age =age;
        this.course = course;
    }

    public Student() {

    }

    public int getId(){
        return id;
    }
    public String getName(){
        return name;
    }
    public int getAge(){
        return age;
    }
    public String getCourse(){
        return course;
    }


    public void setName(String name) {
        this.name = name;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public void setCourse(String course) {
        this.course = course;
    }
}
