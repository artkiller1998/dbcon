package ru.web_marks.web.controllers;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;
import ru.web_marks.security.connection.MongoConfig;
import ru.web_marks.model.Student;

@RestController
@RequestMapping("student")

public class StudentMarkController {

    ApplicationContext ctx = new AnnotationConfigApplicationContext(MongoConfig.class);
    // интерфейс для использования mongoTemplate
    MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");


    @GetMapping("/{subject}/{year_group}")
    public String list(@PathVariable String subject , @PathVariable String year_group )
            throws ChangeSetPersister.NotFoundException {
        System.out.println("[INFO] StudentMarkController list -- get marks in json\n");
        Query searchInstance = new Query(Criteria.where("ancestors").all(subject,year_group));
        return mongoOperation.find(searchInstance, Student.class).toString();
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
