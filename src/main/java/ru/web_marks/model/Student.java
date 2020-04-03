/*
    Конкретная реализация записей типа Студент
*/
package ru.web_marks.model;

import java.util.ArrayList;

public class Student extends MongoModels{

    //String studentName;
    //int groupNum;
    ArrayList<String> ancestors = new ArrayList<String>();
    ArrayList<Mark> mark = new ArrayList<Mark>();
    String u_ident = "";
    String g_ident = "";
    String lesson = "";
    String semester = "";
    String subject = "";
    public Student(ArrayList<String> ancestors) {
        u_ident = ancestors.get(0);
        g_ident = ancestors.get(1);
        lesson = ancestors.get(2);
        semester = ancestors.get(3);
        subject = ancestors.get(4);
        // выполнение конструктора родителя для инициализации параметров
    }

//    public int getInstanceGroup() {
//        return groupNum;
//    }
//
    public ArrayList<Mark> getInstanceMark() {
        return mark;
    }
//
    public void setInstanceMark(String s_mark, String descr, String scale) {
        Mark mark = new Mark(s_mark,descr,scale);
        this.mark.add(mark);
    }
//
//    public void setInstanceAge(int studentAge) {
//        this.groupNum = groupNum;
//    }

    @Override
    public String toString() {
        return String.format(
                "{ \"class\":\"Student\", \"id\":\"%s\", \"u_ident\"" +
                        ":\"%s\", \"g_ident\":\"%s\", \"lesson\":\"%s\"," +
                        " \"semester\":\"%s\",  \"subject\":\"%s\", "+
                        " \"marks\":%s }",

                id, u_ident, g_ident,lesson,semester,subject,mark);
    }
}