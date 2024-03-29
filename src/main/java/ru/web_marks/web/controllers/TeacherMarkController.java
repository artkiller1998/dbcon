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

    public ModelAndView fill_model_backup(Principal principal, String subject, String year_group) {
        ModelAndView modelAndView = tableController.fillModel(principal);
        //ModelAndView modelAndView = new ModelAndView("");

        MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");

        List<Backup> backupsList;

        Query searchInstance = new Query(Criteria.where("ancestors").all(subject, year_group));
        backupsList = mongoOperation.find(searchInstance, Backup.class);
        Collections.reverse(backupsList);
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
        modelAndView.addObject("backups_list", backupsList);
        modelAndView.addObject("date_format", dateFormat);
        modelAndView.addObject("backups_list_size", backupsList.size());
        modelAndView.setViewName("table::pills-backups-div");

        return modelAndView;
    }

    private boolean check_access(String subject, String year_group, Principal principal) {
        String login = principal.getName();

        Query all_group_q = new Query(Criteria.where("ancestors").all(subject,year_group));
        List<Student> all_group = mongoOperation.find(all_group_q, Student.class);

        Query first_object_q = Query.query(Criteria.where("id").is(all_group.get(0).getId()));
        Student first_object = mongoOperation.findOne(first_object_q, Student.class);
        assert first_object != null;
        List<String> ancestors = first_object.getAncestors();

        return !ancestors.contains(login);
    }

    @RequestMapping(value = "/backups/{subject}/{year_group}", method = RequestMethod.GET)
    public ModelAndView show_backups(@PathVariable String subject , @PathVariable String year_group, Principal principal) {

        System.out.println("[INFO] TeacherMarkController show_backups -- show backups\n");

        if (check_access(subject, year_group, principal)) {
            subject = "";
            year_group = "";
        }

        return fill_model_backup(principal, subject, year_group);
    }


    @PutMapping(path="/{subject}/{year_group}/{id}")
    public String update(@PathVariable String subject , @PathVariable String year_group,
                         @RequestBody String mark, @PathVariable String id, Principal principal) throws ChangeSetPersister.NotFoundException {
        System.out.println("[INFO] TeacherMarkController update -- update mark\n");

        if (check_access(subject, year_group, principal))
            return "access_denied";

        String login = principal.getName();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");

        Query searchInstance = Query.query(Criteria.where("tasks").elemMatch(Criteria.where("marks")
                .elemMatch(Criteria.where("mrk_id").is(id))));
        Student temp = mongoOperation.findOne(searchInstance, Student.class);
        assert temp != null;
        temp.setInstanceMark(id,mark);
        Update update = new Update();
        update.set("tasks", temp.getTasks());
        mongoOperation.updateFirst(searchInstance, update, Student.class);


        Query all_group_q = new Query(Criteria.where("ancestors").all(subject,year_group));
        List<Student> all_group = mongoOperation.find(all_group_q, Student.class);

        Query first_object_q = Query.query(Criteria.where("id").is(all_group.get(0).getId()));

        Student first_object = mongoOperation.findOne(first_object_q, Student.class);
        assert first_object != null;
        first_object.setEdited(login + " " + dateFormat.format(new Date()));

        Update update_edited = new Update();
        update_edited.set("edited", first_object.getEdited());

        System.out.println("Предмет " + subject + " группы " + year_group + "  изменен "  + first_object.getEdited() );

        mongoOperation.updateFirst(first_object_q, update_edited, Student.class);

        return "success";
    }

    @RequestMapping(value = "/backup/{subject}/{year_group}", method = RequestMethod.GET)
    public String backup_marks(@PathVariable String subject , @PathVariable String year_group, Principal principal ) {
        System.out.println("[INFO] TeacherMarkController backup_marks -- backup marks in map\n");

        if (check_access(subject, year_group, principal))
            return "access_denied";

        Query searchInstance = new Query(Criteria.where("ancestors").all(subject,year_group));
        List<Student> studentList = mongoOperation.find(searchInstance, Student.class);
        Map<String, Mark> marks = new HashMap<>();
        if (!studentList.isEmpty()) {
            for (Student student : studentList) {
                for (Task task : student.getTasks()) {
                    for (Mark mark : task.getMarks()) {
                        marks.put(mark.getMrk_id(), mark);
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



    @RequestMapping(value = "/restore/{id}", method = RequestMethod.GET)
    public ModelAndView restore_backup(@PathVariable String id, Principal principal) {
        System.out.println("[INFO] TeacherMarkController restore_marks -- restore marks from map\n");

        Backup temp = backupRepository.findById(id)
                .orElseGet(() -> null);
        Map<String, Mark> marks = new HashMap<>();
        ModelAndView modelAndView = new ModelAndView();
        if (temp != null) {
            marks = temp.getMarks();

            String subject = temp.getAncestors().get(2);
            String year_group = temp.getAncestors().get(1);

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

            modelAndView = fill_model_backup(principal, subject, year_group);
        }


        return modelAndView;
    }


    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public ModelAndView delete_backup(@PathVariable String id, Principal principal) {
        Backup temp = backupRepository.findById(id)
                .orElseGet(() -> null);
        System.out.println("[INFO] TeacherMarkController delete_backup -- delete_backup \n");
        backupRepository.deleteById(id);

        ModelAndView modelAndView = new ModelAndView();
        if (temp != null) {
            String subject = temp.getAncestors().get(2);
            String year_group = temp.getAncestors().get(1);

            modelAndView = fill_model_backup(principal, subject, year_group);
        }
        return modelAndView;
    }
}
