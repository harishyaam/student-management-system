package com.example.first.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class StudentRequest {

    @NotBlank(message = "Name Cannot Be Blank")
    @Size(min = 2,max = 30,message = "Name can only have from 2 to 30 Characters")
    private String name;

    @Min(value = 18,message ="Must be 18 years or Older")
    private int age;

    @NotBlank(message = "Cannot be Blank")
    private String course;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public String getCourse() {
        return course;
    }
    public void setCourse(String course) {
        this.course = course;
    }
}