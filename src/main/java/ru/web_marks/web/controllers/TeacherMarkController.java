package ru.web_marks.web.controllers;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.*;
import ru.web_marks.config.MongoConfig;
import ru.web_marks.model.Student;
import ru.web_marks.view.MongoDBPOperations;

@RestController
@RequestMapping(value = "teacher", method = RequestMethod.PUT)

public class TeacherMarkController {
    ApplicationContext ctx = new AnnotationConfigApplicationContext(MongoConfig.class);
    // интерфейс для использования mongoTemplate
    MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");
    // конкретная реализация интерфейса для объекта Student
    MongoDBPOperations ops = new MongoDBPOperations();

    @PutMapping(path="/{subject}/{year_group}/{id}")

    public String update(@PathVariable String subject , @PathVariable String year_group, @RequestBody String mark, @PathVariable String id) throws ChangeSetPersister.NotFoundException {

        Query searchInstance = Query.query(Criteria.where("tasks").elemMatch(Criteria.where("marks").elemMatch(Criteria.where("mrk_id").is(id))));
        Student temp = mongoOperation.findOne(searchInstance, Student.class);
        //assert temp != null;
        temp.setInstanceMark(id,mark);
        //mongoOperation.save(temp.getStudent());
        Update update = new Update();
        update.set("tasks", temp.getTasks());
        //mongoOperation.save(temp.getStudent());
        mongoOperation.updateFirst(searchInstance, update, Student.class);


        return mongoOperation.findOne(searchInstance, Student.class).toString();
    }
}
