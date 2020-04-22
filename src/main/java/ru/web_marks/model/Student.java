/*
    Конкретная реализация записей типа Студент
*/
package ru.web_marks.model;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.*;


public class Student extends MongoModels{
    private static Map current_group = new HashMap<String, String>();
    private static Set loaded_gpoups = new HashSet<String>();
    //String studentName;
    //int groupNum;
    ArrayList<String> ancestors = new ArrayList<String>();
    //ArrayList<Mark> mark = new ArrayList<Mark>();
    String parent = "";
    //String fname = "";
    //String g_ident = "";
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
    public void setInstanceMark(String id,String value) {
        for ( Task task  :tasks) {
            for (Mark mark : task.marks) {
                if (mark.mrk_id.equals(id)) {
                    mark.mrk = value;
                    mark.date = new Date();
                }
            }
        }
    }

    private void upload_group(String g_ident) {
        System.out.println("1");
        BufferedReader br = null;
        try {
            InputStreamReader input_csv = new InputStreamReader(new
                    FileInputStream("src/main/resources/static/csv/"+ g_ident +".csv"), "windows-1251");
            br = new BufferedReader(input_csv);
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String line = "";
        //HashMap<String,String> map = new HashMap<String, String>();

        while(true){
            try {
                if ((line = br.readLine()) == null) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            String str[] = line.split(";");
            for(int i=0;i<str.length;i++){
                //String arr[] = str[i].split(":");
                current_group.put(str[0], str[1]);
            }
        }
        loaded_gpoups.add(g_ident);
    }

    public Student getStudent(){
        return this;
    }
    public ArrayList<Task> getTasks() {return this.tasks;}
//
//    public void setInstanceAge(int studentAge) {
//        this.groupNum = groupNum;
//    }

    @Override
    public String toString() {
        String g_ident = ancestors.get(1);
        if (!loaded_gpoups.contains(g_ident)) {
            upload_group(g_ident);
        }
//        String str[] = line.split(",");
//        for(int i=1;i<str.length;i++){
//            String arr[] = str[i].split(":");
//            map.put(arr[0], arr[1]);
//        }



        //System.out.println(map);



        //ObjectMapper objectMapper = new ObjectMapper();
        //File file = new File("src/main/resources/static/json/"+ g_ident +".json");

//        try {
//            current_group = objectMapper.readValue(file, Map.class);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        String fname = (String) current_group.get(parent);

        return String.format(
                "{ \"parent\":\"%s\", \"fname\":\"%s\", \"tasks\":%s }",
                parent, fname, tasks);
    }
}

