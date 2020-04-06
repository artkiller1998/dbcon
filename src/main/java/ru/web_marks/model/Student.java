/*
    Конкретная реализация записей типа Студент
*/
package ru.web_marks.model;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Student extends MongoModels{
    public static Map current_group = new HashMap<String, String>();
    //String studentName;
    //int groupNum;
    ArrayList<String> ancestors = new ArrayList<String>();
    //ArrayList<Mark> mark = new ArrayList<Mark>();
    String parent = "";
    String fname = "";
    String g_ident = "";
    ArrayList<Task> tasks = new ArrayList<Task>();
    public Student() {
        // выполнение конструктора родителя для инициализации параметров
    }

//    public int getInstanceGroup() {
//        return groupNum;
//    }
//
//    public ArrayList<Mark> getInstanceMark() {
//        return marks;
//    }
//
//    public void setInstanceMark(String s_mark, String descr, String scale) {
//        Mark mark = new Mark(s_mark,descr,scale);
//        this.mark.add(mark);
//    }
//
//    public void setInstanceAge(int studentAge) {
//        this.groupNum = groupNum;
//    }

    @Override
    public String toString() {
        g_ident = ancestors.get(1);
        ObjectMapper objectMapper = new ObjectMapper();
        File file = new File("src/main/resources/static/json/"+ g_ident +".json");

        try {
            current_group = objectMapper.readValue(file, Map.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        fname = (String) current_group.get(parent);

        return String.format(
                "{ \"parent\":\"%s\", \"fname\":\"%s\", \"tasks\":%s }",
                parent, fname, tasks);
    }
}