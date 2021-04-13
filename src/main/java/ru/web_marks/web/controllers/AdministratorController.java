package ru.web_marks.web.controllers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import ru.web_marks.model.Student;
import ru.web_marks.security.connection.MongoConfig;
import ru.web_marks.model.DatabaseFillController;
import ru.web_marks.model.MongoModels;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(value = "administrator", method = RequestMethod.PUT)
public class AdministratorController {

    @Value("${spring.config.profile}:local")
    public String profile;

    @Autowired
    private TableController tableController;

    ApplicationContext ctx = new AnnotationConfigApplicationContext(MongoConfig.class);
    // интерфейс для использования mongoTemplate
    MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");

    @PutMapping(path="/load")
    public ResponseEntity<String> update(@RequestBody String data) throws ChangeSetPersister.NotFoundException, IOException {
        System.out.println("[INFO] AdministratorController update -- upload groups\n");
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

            Query searchInstance = new Query(Criteria.where("ancestors")
                    .all(config_name.substring(0, config_name.lastIndexOf('.'))
                    , group_name.substring(0, group_name.lastIndexOf('.'))));
            MongoModels resultInstance = mongoOperation.findOne(searchInstance, MongoModels.class);
            if (resultInstance != null) {
                return ResponseEntity.badRequest().body("Collection exists");
            }

            DatabaseFillController databaseFillController = new DatabaseFillController(config_content, group_content, config_name);
        }
        catch (Exception exc)
        {
            System.out.println("[ERROR] AdministratorController update -- upload error\n");
            return ResponseEntity.badRequest().body("Error");
        }

        String csvFile;

        if (profile.equals("local")) {
            csvFile = "src\\main\\resources\\static\\csv\\" + group_name;
        }
        else {
            csvFile = "../webapps/dbconnector/WEB-INF/classes/static/csv/" + group_name;
        }
        File f = new File(csvFile);

        final Path path = Paths.get(csvFile);

        if (!Files.isReadable(path) && !Files.isWritable(path) && !Files.isExecutable(path))
        {
            f.getParentFile().mkdirs();
            f.createNewFile();
            try (OutputStreamWriter writer =
                         new OutputStreamWriter(new FileOutputStream(path.toString()), StandardCharsets.UTF_8))
            {
                writer.write(group_content);
            }
        }

        return ResponseEntity.ok("true");
    }

    @DeleteMapping(path="/delete/{subject}/{year_group}")
    public RedirectView delete(@PathVariable String subject, @PathVariable String year_group)
            throws ChangeSetPersister.NotFoundException, IOException {

        System.out.println("[INFO] AdministratorController delete -- delete subject\n");
        try {
            Query searchInstance = new Query(Criteria.where("ancestors").all(subject, year_group));
            Student resultInstance = mongoOperation.findOne(searchInstance, Student.class);
            if (resultInstance == null) {
                //return ResponseEntity.badRequest().body("Error");
            }
            else {

                mongoOperation.remove(searchInstance, MongoModels.class).toString();
            }
        }
        catch (Exception exc)
        {
            System.out.println("[ERROR] AdministratorController delete -- delete subject error\n");
        }

        return new RedirectView("/dashboard/subjects", true);
    }

    @DeleteMapping(path="/delete/{year_group}")
    public RedirectView deleteGroup(@PathVariable String year_group)
            throws ChangeSetPersister.NotFoundException, IOException {
        System.out.println("[INFO] AdministratorController deleteGroup -- delete groups\n");
        String csvFile;

        if (profile.equals("local")) {
            csvFile = "src\\main\\resources\\static\\csv\\" + year_group + ".CSV";
        }
        else {
            csvFile = "../webapps/dbconnector/WEB-INF/classes/static/csv/" + year_group + ".CSV";
        }
        File f = new File(csvFile);

        Path path  = Paths.get(csvFile);

        try {
            if(Files.isRegularFile(path))
            {
                System.gc();
                Files.delete(path);
                Query searchInstance = new Query(Criteria.where("ancestors").all(year_group));
                Student resultInstance = mongoOperation.findOne(searchInstance, Student.class);
                resultInstance.delGroup(year_group);
            }
        }

        catch (Exception exc)
        {
            System.out.println("[ERROR] AdministratorController deleteGroup -- delete groups error\n");
            System.out.println(exc);
            // return ResponseEntity.badRequest().body("Error");
        }

        return new RedirectView("/dashboard/groups", true);
    }

    @RequestMapping(value = "/create_config", method = RequestMethod.GET)
    public ModelAndView login(Principal principal) {
        ModelAndView modelAndView = tableController.fillModel(principal);

        //modelAndView.addObject("role", "USER");
        modelAndView.setViewName("create_config");
        return modelAndView;
    }

}
