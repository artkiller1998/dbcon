package ru.web_marks.web.controllers;

//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.web_marks.config.MongoConfig;
import ru.web_marks.model.DatabaseFillController;
import ru.web_marks.model.MongoModels;
import ru.web_marks.view.MongoDBPOperations;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping(value = "administrator", method = RequestMethod.PUT)
public class AdministrarorController {

    ApplicationContext ctx = new AnnotationConfigApplicationContext(MongoConfig.class);
    // интерфейс для использования mongoTemplate
    MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");
    // конкретная реализация интерфейса для объекта Student
    MongoDBPOperations ops = new MongoDBPOperations();

    @PutMapping(path="/load")
    public ResponseEntity<String> update(@RequestBody String data) throws ChangeSetPersister.NotFoundException, IOException {

        System.out.println("\nLoad detected!\n");
        System.out.println("\n"+ data +"\n");
        String group_name;
        String group_content;
        try {
            Gson gson = new Gson();
            String jsonOutput = data;
            Type listType = new TypeToken<List<List<String>>>(){}.getType();
            List<List<String>> posts = gson.fromJson(jsonOutput, listType);
            String config_content = posts.get(1).get(0);
            String config_name = posts.get(1).get(1).toUpperCase();
            group_content = posts.get(0).get(0);
            group_name = posts.get(0).get(1).toUpperCase();

            Query searchInstance = new Query(Criteria.where("ancestors").all(config_name.substring(0, config_name.lastIndexOf('.'))));
            MongoModels resultInstance = mongoOperation.findOne(searchInstance, MongoModels.class);
            if (resultInstance != null) {
                return ResponseEntity.badRequest().body("Collection exists");
            }

            DatabaseFillController databaseFillController = new DatabaseFillController(config_content, group_content, config_name);
        }
        catch (Exception exc)
        {
            System.out.println("\nLoad error!\n");
            return ResponseEntity.badRequest().body("Error");
        }

        String csvFile = "src\\main\\resources\\static\\csv\\" + group_name;
        final Path path = Paths.get(csvFile);

        if (!Files.isReadable(path) && !Files.isWritable(path) && !Files.isExecutable(path))
        {
            try (OutputStreamWriter writer =
                         new OutputStreamWriter(new FileOutputStream(String.valueOf(path)), StandardCharsets.UTF_8))
            {
                writer.write(group_content);
            }
        }

        return ResponseEntity.ok("true");
    }

    @PutMapping(path="/delete")
    public ResponseEntity<String> delete(@RequestBody List<String> data) throws ChangeSetPersister.NotFoundException, IOException {

        System.out.println("\nDelition detected!\n");
        System.out.println("\n"+ data +"\n");
        try {
            String delition_value = data.get(0);
            String delition_type = data.get(1);
            System.out.println(delition_value + "\n" + delition_type);
            if ( delition_type.equals("configFile") ) {
                Query searchInstance = new Query(Criteria.where("ancestors").all(delition_value));
                MongoModels resultInstance = mongoOperation.findOne(searchInstance, MongoModels.class);
                if (resultInstance == null) {
                    return ResponseEntity.badRequest().body("Error");
                }
                else {
                    mongoOperation.remove(searchInstance, MongoModels.class).toString();
                }
            }
            else  {
                String csvFile = "src\\main\\resources\\static\\csv\\" + delition_value + ".CSV";
                Path path  = Paths.get(csvFile);
                if(Files.isRegularFile(path))
                {
                   Files.delete(path);
                }
                else {
                    System.out.println("\nDelition error!\n");
                    return ResponseEntity.badRequest().body("Error");
                }
            }
        }
        catch (Exception exc)
        {
            System.out.println("\nDelition error!\n");
            return ResponseEntity.badRequest().body("Error");
        }

        return ResponseEntity.ok("true");
    }
}
