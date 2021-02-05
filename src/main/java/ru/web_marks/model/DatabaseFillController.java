package ru.web_marks.model;

import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.web_marks.security.connection.MongoConfig;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseFillController {

    class Config {
        public String _id;
        public List<MarkConfig> mark;
    }

    class MarkConfig {
        public String scale;
        public String descr;
    }

    static class Mark {
        public String mark;
        public String color;

        public Mark(String mrk, String clr) {
            this.mark = mrk;
            this.color = clr;
        }
    }

    static class MarkNote {
        public Config config;
        List<Mark> marks;

        public MarkNote(Config config) {
            this.config = config;
            this.marks = new ArrayList<>();
            for (int i = 0; i < config.mark.size(); i++) {
                Mark m = new Mark("", "");
                this.marks.add(m);
            }
        }
    }

    static class markExit {
        public String scale;
        public String mrk;
        public String descr;
        public String color;
        public Date date;
        public String mrk_id;

        markExit(String scale, String mark, String descr, String color) {
            this.scale = scale;
            this.mrk = mark;
            this.descr = descr;
            this.color = color;
            this.date = new Date(0);
            this.mrk_id = new ObjectId().toHexString();
        }
    }

    static class Task {
        List<markExit> marks;
        public String lesson;

        public Task(MarkNote marknote) {
            this.marks = new ArrayList<>();
            this.lesson = marknote.config._id;
            for (int i = 0; i < marknote.marks.size(); i++) {
                markExit me = new markExit(marknote.config.mark.get(i).scale, marknote.marks.get(i).mark, marknote.config.mark.get(i).descr, marknote.marks.get(i).color);
                this.marks.add(me);
            }
        }
    }

    static class Note {
        public List<String> ancestors;
        public String parent;
        List<Task> tasks;

        public Note(List<MarkNote> marknote, String name, String group, String mixed, String filename) {
            this.tasks = new ArrayList<>();

            for (int j = 0; j < marknote.size(); j++) {
                this.tasks.add(new Task(marknote.get(j)));
                this.ancestors = new ArrayList<>();
                this.ancestors.add(name.toUpperCase());
                this.ancestors.add(group.toUpperCase());
                this.ancestors.add(filename);
                this.ancestors.add(filename.substring(0, filename.length() - 2));
            }

            if (mixed.equals("MIXED"))
                this.ancestors.add("MIXED");
            this.parent = name;
        }
    }



    public DatabaseFillController(String config_content, String group_content, String group_name) throws IOException {
        new LoadData(config_content,  group_content, group_name);
    }
}

class LoadData {

    public LoadData (String config_content, String group_content, String group_name) throws IOException {

        ApplicationContext context = new AnnotationConfigApplicationContext("ru.web_marks.security.connection");
        MongoConfig configure = (MongoConfig) context.getBean("mongo-config");

        String u_addr = configure.u_addr;
        int u_port = configure.u_port;
        String u_dbname = configure.u_dbname;

        Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
        mongoLogger.setLevel(Level.SEVERE);
        String MONGODB_HOST = u_addr;
        int MONGODB_PORT = u_port;
        MongoCollection collection = null;

        try {
            /**
             *  Connect to MongoDB
             */
            MongoClient mongo = new MongoClient(MONGODB_HOST, MONGODB_PORT);
            /**
             * Get DB
             *
             * If it doesn't exist, MongoDB will create it
             */
            MongoDatabase db = mongo.getDatabase(u_dbname);   //НАЗВАНИЕ БД
            /**
             * Get Collection
             *
             * If it doesn't exist, MongoDB will create it
             */
            collection = db.getCollection("default");           //НАЗВАНИЕ КОЛЛЕКЦИИ
            System.out.println("Done");

        } catch (MongoException e) {
            e.printStackTrace();
        }

        Gson g = new Gson();

        DatabaseFillController.Config[] configs = g.fromJson(config_content, DatabaseFillController.Config[].class);

        List<String> names = new ArrayList<String>();

        //  простое чтение csv как текстового файла, для разделения стобцов используются простой разделитель строки ";"

        String line;
        Reader group_content_r = new StringReader(group_content);
        BufferedReader br = new BufferedReader(group_content_r);
        while ((line = br.readLine()) != null) {
            // use comma as separator
            String[] cols = line.split(";");
            names.add(cols[0]);
        }
        System.out.println("names: " + names);
        System.out.println("config size =  " + configs.length);

        List<DatabaseFillController.MarkNote> ListMN = new ArrayList<>();
        for (int j = 0; j < configs.length; j++) {
            DatabaseFillController.MarkNote m = new DatabaseFillController.MarkNote(configs[j]);
            ListMN.add(m);
        }

        String mxd = "";
        for (int i = 0; i < names.size(); i++) {
            if (names.get(i).equals("MIXED")) {
                mxd = names.get(0);
                continue;
            }

            String fname = group_name.substring(group_name.lastIndexOf('/') + 1, group_name.lastIndexOf('.')).toUpperCase();
            DatabaseFillController.Note note = new DatabaseFillController.Note(ListMN, names.get(i), names.get(i).substring(0, 4), mxd, fname); //MIXED
            Gson gson = new Gson();
            String json = gson.toJson(note);
            Document doc = Document.parse(json);
            collection.insertOne(doc);
        }
    }
}
