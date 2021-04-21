/*
    Конкретная реализация записей типа Студент
*/
package ru.web_marks.model;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


public class Student extends MongoModels{
    private static Map current_group = new HashMap<String, String>();
    private static Set loaded_gpoups = new HashSet<String>();
    ArrayList<String> ancestors = new ArrayList<String>();
    String parent = "";
    String edited = "";
    ArrayList<Task> tasks = new ArrayList<Task>();
    public Student() {
        // выполнение конструктора родителя для инициализации параметров
    }

    public String getSubject() {
        return ancestors.get(2);
    }

    public String getGroup() {
        return  ancestors.get(1);
    }

    public void delGroup(String group) {
        if (loaded_gpoups.contains(group)) {
            loaded_gpoups.remove(group);
            current_group.clear();
        }
    }

    public String getId() {
        return  this.id;
    }

    public void setInstanceMark(String id, String value, Principal principal) {
        String login = principal.getName();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
        for ( Task task  : tasks) {
            for (Mark mark : task.marks) {
                if (mark.mrk_id.equals(id)) {
                    mark.mrk = value;
                    mark.date = new Date();
                    edited = "Eddited by " + login + " at:" + dateFormat.format(mark.date);
                }
            }
        }
    }

    private void upload_group(String g_ident) {
        BufferedReader br = null;
        try {
            InputStreamReader input_csv;
            try {
                input_csv = new InputStreamReader(new
                        FileInputStream("src/main/resources/static/csv/" + g_ident + ".CSV"), StandardCharsets.UTF_8);
            }
            catch (FileNotFoundException e) {
                input_csv = new InputStreamReader(new
                        FileInputStream("../webapps/dbconnector/WEB-INF/classes/static/csv/" + g_ident + ".CSV"), StandardCharsets.UTF_8);
            }

            br = new BufferedReader(input_csv);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String line = "";

        while(true){
            try {
                if ((line = br.readLine()) == null) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            String[] str = line.split(";");
            for(int i=0;i<str.length;i++){
                current_group.put(str[0], str[1]);
            }
        }
        loaded_gpoups.add(g_ident);
    }

    public Student getStudent(){
        return this;
    }
    public ArrayList<Task> getTasks() {return this.tasks;}

    public ArrayList<String> getAncestors() {
        return ancestors;
    }

    @Override
    public String toString() {
        String g_ident = ancestors.get(1);
        //System.out.println("lg" + loaded_gpoups + "cg" + current_group);
        if (!loaded_gpoups.contains(g_ident)) {
            upload_group(g_ident);
        }

        String fname = (String) current_group.get(parent);

        return String.format(
                "{ \"parent\":\"%s\", \"fname\":\"%s\", \"tasks\":%s }",
                parent, fname, tasks);
    }
}

