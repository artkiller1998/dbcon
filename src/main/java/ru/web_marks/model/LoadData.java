package ru.web_marks.model;

import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;
import ru.web_marks.security.connection.MongoConfig;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

class LoadData {

    public LoadData (String config_content, String group_content, String group_name) throws IOException {

        ApplicationContext context = new AnnotationConfigApplicationContext("ru.web_marks.security.connection");
        MongoConfig configure = (MongoConfig) context.getBean("mongo-config");

        System.out.println("LoadData started");

        String u_addr = configure.u_addr;
        int u_port = configure.u_port;
//        String u_dbname = "test";
        String u_dbname = configure.u_dbname;

        Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
        mongoLogger.setLevel(Level.SEVERE);
//        String MONGODB_HOST = u_addr;
//        int MONGODB_PORT = u_port;
        MongoCollection collection = null;

        try {
            /**
             *  Connect to MongoDB
             */
            MongoClient mongo = new MongoClient(u_addr,u_port);

            //MongoClient mongoClient = MongoParams.mongoClient;
            System.out.println("MongoParams.mongoClient " +
                            "mongoClient.toString() + " +
                    mongo.toString() +
                            "mongoClient.getAddress() + "+
                    mongo.getAddress() +
                            "mongoClient.getConnectPoint() + "+
                    mongo.getConnectPoint() +
                            "mongoClient.getDatabase(u_dbname) + "+
                    mongo.getDatabase(u_dbname) +
                    "mongoClient.getDatabase(u_dbname).getCollection(default);"+
                    mongo.getDatabase(u_dbname).getCollection("default")+
                    "u_dbname);"+
                    u_dbname);
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
            System.out.println(e);
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
