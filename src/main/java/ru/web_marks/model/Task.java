package ru.web_marks.model;

import java.util.ArrayList;

public class Task {

    ArrayList<Mark> marks = new ArrayList<Mark>();
    String lesson;

//    public ArrayList<Mark> getInstanceMark() {
//        return marks;
//    }

//    public void setInstanceMark(String s_mark) {
//        Mark mark = new Mark(s_mark);
//        this.marks.add(mark);
//        this.lesson = lesson;
//    }


    @Override
    public String toString() {
        return String.format(
                "{ \"lesson\":\"%s\", \"marks\":%s }",
                lesson, marks);
    }
}
