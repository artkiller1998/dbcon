package ru.web_marks.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.*;
import ru.web_marks.model.Mark;
import ru.web_marks.model.Task;
import ru.web_marks.model.domain.Backup;
import ru.web_marks.repository.BackupRepository;
import ru.web_marks.security.connection.MongoConfig;
import ru.web_marks.model.Student;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "teacher", method = RequestMethod.PUT)
public class TeacherMarkController {

    @Autowired
    BackupRepository backupRepository;

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
        assert temp != null;
        temp.setInstanceMark(id,mark);
        Update update = new Update();
        update.set("tasks", temp.getTasks());
        mongoOperation.updateFirst(searchInstance, update, Student.class);


        return mongoOperation.findOne(searchInstance, Student.class).toString();
    }

    @RequestMapping(value = "/backup/{subject}/{year_group}", method = RequestMethod.GET)
    public Map<String, String> backup_marks(@PathVariable String subject , @PathVariable String year_group ) {
        System.out.println("[INFO] TeacherMarkController backup_marks -- backup marks in map\n");
        Query searchInstance = new Query(Criteria.where("ancestors").all(subject,year_group));
        List<Student> studentList = mongoOperation.find(searchInstance, Student.class);
        Map<String, String> marks = new HashMap<>();
        if (!studentList.isEmpty()) {
            for (Student student : studentList) {
                for (Task task : student.getTasks()) {
                    for (Mark mark : task.getMarks()) {
                        marks.put(mark.getMrk_id(), mark.getMrk());
                    }
                }
            }
            Backup backup = new Backup();
            backup.setAncestors(studentList.get(0).getAncestors());
            backup.setDate(new Date());
            backup.setMarks(marks);

            backupRepository.save(backup);
            //restore_marks(subject, year_group, marks);
        }
        return marks;
    }

    @RequestMapping(value = "/restore/{subject}/{year_group}/{id}", method = RequestMethod.GET)
    public Map<String, String> restore_marks(@PathVariable String subject , @PathVariable String year_group, @PathVariable String id) {
        System.out.println("[INFO] TeacherMarkController restore_marks -- restore marks from map\n");

        Backup temp = backupRepository.findById(id)
                .orElseGet(() -> null);
        Map<String, String> marks = new HashMap<>();
        if (temp != null) {
            marks = temp.getMarks();


            Query searchInstance = new Query(Criteria.where("ancestors").all(subject,year_group));
            List<Student> studentList = mongoOperation.find(searchInstance, Student.class);

            for (Student student : studentList) {
                for (Task task : student.getTasks()) {
                    for (Mark mark : task.getMarks()) {
                        if (marks.containsKey(mark.getMrk_id())){
                            student.setInstanceMark(mark.getMrk_id(),marks.get(mark.getMrk_id()));
                        }

                        //marks.put(mark.getMrk_id(), mark.getMrk());
                    }
                }
                Update update = new Update();
                update.set("tasks", student.getTasks());
                mongoOperation.save(student);
            }
        }

        return marks;
    }
}
