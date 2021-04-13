package ru.web_marks.web.controllers;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.*;
import ru.web_marks.security.connection.MongoConfig;
import ru.web_marks.model.Student;

@RestController
@RequestMapping(value = "teacher", method = RequestMethod.PUT)

public class TeacherMarkController {
    ApplicationContext ctx = new AnnotationConfigApplicationContext(MongoConfig.class);
    // интерфейс для использования mongoTemplate
    MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");

    @PutMapping(path="/{subject}/{year_group}/{id}")

    public String update(@PathVariable String subject , @PathVariable String year_group,
                         @RequestBody String mark, @PathVariable String id) throws ChangeSetPersister.NotFoundException {
        System.out.println("[INFO] TeacherMarkController update -- update mark\n");

        Query searchInstance = Query.query(Criteria.where("tasks").elemMatch(Criteria.where("marks")
                .elemMatch(Criteria.where("mrk_id").is(id))));
        Student temp = mongoOperation.findOne(searchInstance, Student.class);
        temp.setInstanceMark(id,mark);
        Update update = new Update();
        update.set("tasks", temp.getTasks());
        mongoOperation.updateFirst(searchInstance, update, Student.class);


        return mongoOperation.findOne(searchInstance, Student.class).toString();
    }
}
