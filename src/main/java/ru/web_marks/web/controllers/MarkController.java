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
    List<MongoModels> listInstance;
//    private int counter = 4;
//    private List<Map<String, String>> messages = new ArrayList<Map<String, String>>() {{
//        add(new HashMap<String, String>() {{
//            put("id", "1");
//            put("text", "First message");
//        }});
//        add(new HashMap<String, String>() {{
//            put("id", "2");
//            put("text", "Second message");
//        }});
//        add(new HashMap<String, String>() {{
//            put("id", "3");
//            put("text", "Third message");
//        }});
//    }};

    @GetMapping("/{subject}/{year_group}")
    public String list(@PathVariable String subject , @PathVariable String year_group ) throws ChangeSetPersister.NotFoundException {

          Query searchInstance = new Query(Criteria.where("ancestors").all(subject,year_group));

//        String res = "Instance = ";
//        List listInstance = mongoOperation.findAll(MongoModels.class);
//        for(Object instance  :listInstance) {
//            res += instance;
//        }
        return mongoOperation.find(searchInstance, MongoModels.class).toString();
    }

    @GetMapping("{value}")
    public String getOne(@PathVariable String value) throws ChangeSetPersister.NotFoundException {
        Query searchInstance = new Query(Criteria.where("lastName").is(value));

        // find instance based on the query
        return mongoOperation.findOne(searchInstance, Student.class).toString();
    }


//    private Map<String, String> getMessage(@PathVariable String id) throws ChangeSetPersister.NotFoundException {
//        return messages.stream()
//                .filter(message -> message.get("id").equals(id))
//                .findFirst()
//                .orElseThrow(ChangeSetPersister.NotFoundException::new);
//    }
//
//    @PostMapping
//    public Map<String, String> create(@RequestBody Map<String, String> message) {
//        message.put("id", String.valueOf(counter++));
//
//        messages.add(message);
//
//        return message;
//    }

    @PutMapping("{id}")
    public String update(@PathVariable String id, @RequestBody String mark)
            throws ChangeSetPersister.NotFoundException {
        //Map<String, String> messageFromDb = getMessage(id);
        Query searchInstance = new Query(Criteria.where("id").is(id));
        Student temp = mongoOperation.findOne(searchInstance, Student.class);
        temp.setInstanceMark(mark);
        ops.updateInstance(mongoOperation, "id", id, "studentMarks", temp.getInstanceMark());

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
