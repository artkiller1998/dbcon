package ru.web_marks.model;

import java.util.ArrayList;

public class Task extends Student{

    ArrayList<Mark> marks = new ArrayList<Mark>();
    String lesson  = "";

    public ArrayList<Mark> getInstanceMark() {
        return marks;
    }

    public void setInstanceMark(String s_mark, String descr, String scale) {
        Mark mark = new Mark(s_mark,descr,scale);
        this.marks.add(mark);
    }


    @Override
    public String toString() {
        return String.format(
                "{ \"lesson\":\"%s\", \"marks\":%s }",
                lesson, marks);
    }
}
