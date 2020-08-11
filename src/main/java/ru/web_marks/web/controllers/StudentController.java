package ru.web_marks.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StudentController {
    @GetMapping("")
    public String student() {
        return "studentsTable";
    }
}