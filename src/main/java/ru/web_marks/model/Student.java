/*
    Конкретная реализация записей типа Студент
*/
package ru.web_marks.model;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


public class Student extends MongoModels{
    private static Map current_group = new HashMap<String, String>();
    private static Set loaded_gpoups = new HashSet<String>();
    ArrayList<String> ancestors = new ArrayList<String>();
    String parent = "";
    String edited = "";;
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

    public void setAncestors(ArrayList<String> ancestors) {
        this.ancestors = ancestors;
    }

    public String getEdited() {
        return edited;
    }

    public void setEdited(String edited) {
        this.edited = edited;
    }

    public String getId() {
        return  this.id;
    }

    public void setInstanceMark(String id, String value) {

        for ( Task task  : tasks) {
            for (Mark mark : task.marks) {
                if (mark.mrk_id.equals(id)) {
                    mark.mrk = value;
                    mark.date = new Date();
                }
            }
        }
    }

    private void upload_group(String g_ident) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        byte[] array = new byte[0];
        try {
            //InputStreamReader input_csv;

//            try {
//                array = Files.readAllBytes(Paths.get("src/main/resources/static/csv/" + g_ident + ".CSV"));
//            }
            //catch (FileNotFoundException e) {
                array = Files.readAllBytes(Paths.get("../webapps/dbconnector/WEB-INF/classes/static/csv/" + g_ident + ".CSV"));
           // }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String line = "";

        String key = "AsT16232Qsd84231";
        Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, aesKey);

        String decrypted = new String(cipher.doFinal(array));
        //System.out.println(decrypted);

        String lines[] = decrypted.split("\\r?\\n");

        for(String row : lines){
//        try {
//            if ((line = lines.readLine()) == null) break;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        String[] str = row.split(";");
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
            try {
                upload_group(g_ident);
            } catch (IOException | NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException e) {
                e.printStackTrace();
            }
        }

        String fname = (String) current_group.get(parent);

        return String.format(
                "{ \"parent\":\"%s\", \"fname\":\"%s\", \"edited\":\"%s\", \"tasks\":%s }",
                parent, fname, edited, tasks);
    }
}

