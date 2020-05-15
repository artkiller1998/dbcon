package ru.web_marks.model;

import java.util.ArrayList;

public class Task {

    ArrayList<Mark> marks = new ArrayList<Mark>();
    String lesson;

    @Override
    public String toString() {
        return String.format(
                "{ \"lesson\":\"%s\", \"marks\":%s }",
                lesson, marks);
    }
}
