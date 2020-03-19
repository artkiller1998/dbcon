/*
    Конкретная реализация записей типа Студент
*/
package ru.web_marks.model;

import java.util.ArrayList;

public class Student extends MongoModels {

    //String studentName;
    int groupNum;
    ArrayList<Mark> studentMarks = new ArrayList<Mark>();
    public Student(String firstName, String lastName, int groupNum) {
        // выполнение конструктора родителя для инициализации параметров
        super(firstName, lastName);
        this.groupNum = groupNum;
    }

    public int getInstanceGroup() {
        return groupNum;
    }

    public ArrayList<Mark> getInstanceMark() {
        return studentMarks;
    }

    public void setInstanceMark(String s_mark) {
        Mark mark = new Mark(s_mark);
        this.studentMarks.add(mark);
    }

    public void setInstanceAge(int studentAge) {
        this.groupNum = groupNum;
    }

    @Override
    public String toString() {
        return String.format(
                "{ \"class\"=Student\", \"id\"=\"%s\", \"fName\"=\"%s\", \"lName\"=\"%s\", \"marks\"=%s }",
                id, firstName, lastName,studentMarks);
    }

}