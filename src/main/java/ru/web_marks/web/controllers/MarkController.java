package ru.web_marks.web.controllers;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;
import ru.web_marks.config.MongoConfig;
import ru.web_marks.model.MongoModels;
import ru.web_marks.model.Student;
import ru.web_marks.view.MongoDBPOperations;
import java.util.List;

@RestController
@RequestMapping("student")

public class MarkController {

    ApplicationContext ctx = new AnnotationConfigApplicationContext(MongoConfig.class);
    // интерфейс для использования mongoTemplate
    MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");
    // конкретная реализация интерфейса для объекта Student
    MongoDBPOperations ops = new MongoDBPOperations();

    @GetMapping("/{subject}/{year_group}")
    public String list(@PathVariable String subject , @PathVariable String year_group ) throws ChangeSetPersister.NotFoundException {

        Query searchInstance = new Query(Criteria.where("ancestors").all(subject,year_group));
        return mongoOperation.find(searchInstance, Student.class).toString();
    }

    @PutMapping("/{subject}/{year_group}/{id}")
    public String update(@PathVariable String id, @RequestBody String mark, @PathVariable String subject ,
                         @PathVariable String year_group) throws ChangeSetPersister.NotFoundException {

        //Query searchInstance = new Query(Criteria.where("mark.id").is(id));
        //Query searchInstance2 = new Query();
        //Student temp = mongoOperation.find(searchInstance, Student.class);

        Query searchInstance = Query.query(Criteria.where("tasks").elemMatch(Criteria.where("marks").elemMatch(Criteria.where("id").is(id))));
        Student temp = mongoOperation.findOne(searchInstance, Student.class);
        assert temp != null;
        temp.setInstanceMark(id,mark);
        //String[] values = mark_id_val.split(",");
        //temp.setInstanceMark(values[0], values[1], values[2]);
        //ops.updateInstance(mongoOperation, "id", id, "mrk", values[0]);
        //ops.updateInstance(mongoOperation, "id", id, "mrk", mark_id_val);
        //Update update = new Update();
        //update.set(null, temp.getStudent());
        mongoOperation.save(temp.getStudent());
        //mongoOperation.updateFirst(searchInstance, update, Student.class);
        //mongoOperation.findAndModify(searchInstance,update,Student.class);
        return mongoOperation.findOne(searchInstance, Student.class).toString();
    }
}

    //ЧТОБЫ ПРОВЕРИТЬ В БРАУЗЕРЕ
//    fetch(
//  '/message/5e71d5f70deee42bfe4748b2',        !!!<<< сюда подставить ID студента
//    {
//        method: 'PUT',
//                headers: { 'Content-Type': 'text/html'},
//        body: '66'
//    }
//);

//    @DeleteMapping("{id}")
//    public void delete(@PathVariable String id) throws ChangeSetPersister.NotFoundException {
//        Map<String, String> message = getMessage(id);
//
//        messages.remove(message);
//    }
//}
