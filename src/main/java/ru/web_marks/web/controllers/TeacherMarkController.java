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
import org.springframework.web.servlet.ModelAndView;
import ru.web_marks.model.Mark;
import ru.web_marks.model.Task;
import ru.web_marks.model.domain.Backup;
import ru.web_marks.repository.BackupRepository;
import ru.web_marks.security.connection.MongoConfig;
import ru.web_marks.model.Student;

import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping(value = "teacher", method = RequestMethod.PUT)
public class TeacherMarkController {

    @Autowired
    BackupRepository backupRepository;

    @Autowired
    TableController tableController;

    ApplicationContext ctx = new AnnotationConfigApplicationContext(MongoConfig.class);
    // интерфейс для использования mongoTemplate
    MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");

    @RequestMapping(value = "/backups/{subject}/{year_group}", method = RequestMethod.GET)
    public ModelAndView show_backups(@PathVariable String subject , @PathVariable String year_group, Principal principal) {

        System.out.println("[INFO] TeacherMarkController show_backups -- show backups\n");
        //ModelAndView modelAndView = tableController.fillModel(principal);
        ModelAndView modelAndView = new ModelAndView("table::pills-backups-div");
//        Set<AbstractMap.SimpleEntry<String,String>> subjects_set = new HashSet<>();

        MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");

        List<Backup> backupsList;

        Query searchInstance = new Query(Criteria.where("ancestors").all(subject, year_group));
        backupsList = mongoOperation.find(searchInstance, Backup.class);


//        for (Backup el : backupsList) {
////            subjects_set.add(new AbstractMap.SimpleEntry<>(el.getSubject(), el.getGroup()));
//        }
        Collections.reverse(backupsList);
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
        modelAndView.addObject("backups_list", backupsList);
        modelAndView.addObject("date_format", dateFormat);
        modelAndView.addObject("backups_list_size", backupsList.size());
        //modelAndView.setViewName("/table ::pills-backups");
        return modelAndView;
    }


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
    public String backup_marks(@PathVariable String subject , @PathVariable String year_group ) {
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
        else {
            return "";
        }
        return "success";
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
