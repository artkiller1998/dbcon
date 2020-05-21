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
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.*;
import ru.web_marks.config.MongoConfig;
import ru.web_marks.model.DatabaseFillController;
import ru.web_marks.model.Student;
import ru.web_marks.view.MongoDBPOperations;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
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

    public boolean update(@RequestBody String data) throws ChangeSetPersister.NotFoundException, IOException {


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
            String config_name = posts.get(1).get(1);
            group_content = posts.get(0).get(0);
            group_name = posts.get(0).get(1);

            DatabaseFillController databaseFillController = new DatabaseFillController(config_content, group_content, config_name);
        }
        catch (Exception exc)
        {
            System.out.println("\nLoad error!\n");
            return false;
        }

        String csvFile = "src\\main\\resources\\static\\csv\\abc.csv";
        final Path path = Paths.get(csvFile);

        if (!Files.isReadable(path) && !Files.isWritable(path) && !Files.isExecutable(path))
        {
            try (OutputStreamWriter writer =
                         new OutputStreamWriter(new FileOutputStream(String.valueOf(path)), StandardCharsets.UTF_8))
            {
                writer.write(group_content);
            }
        }



        return  true;
    }
}
