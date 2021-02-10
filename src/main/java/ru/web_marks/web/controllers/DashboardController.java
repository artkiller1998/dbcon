package ru.web_marks.web.controllers;

import java.util.AbstractMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import ru.web_marks.model.Student;
import ru.web_marks.model.domain.Teacher;
import ru.web_marks.model.domain.User;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Controller
@RequestMapping(value = "dashboard", method = RequestMethod.PUT)
public class DashboardController {

    @Autowired
    private ru.web_marks.service.CustomUserDetailsService userService;

    @Autowired
    private TableController tableController;

    @Autowired
    ApplicationContext ctx;

    @RequestMapping(value = {"/"}, method = RequestMethod.GET)
    public ModelAndView dashboard(Principal principal) {
        ModelAndView modelAndView = tableController.fillModel(principal);

        modelAndView.setViewName("/dashboard/dashboard");
        return modelAndView;
    }

    @RequestMapping(value = {"/teachers"}, method = RequestMethod.GET)
    public ModelAndView dashboard_teachers(Principal principal) {
        ModelAndView modelAndView = tableController.fillModel(principal);

        MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");

        List<Teacher> teacherList;

        Query searchInstance = new Query(Criteria.where("email").exists(true));
        teacherList = mongoOperation.find(searchInstance, Teacher.class);
        modelAndView.addObject("teachers_list", teacherList);
        System.out.println(teacherList);
        modelAndView.setViewName("/dashboard/teachers_list");
        return modelAndView;
    }


    @RequestMapping(value = {"/subjects"}, method = RequestMethod.GET)
    public ModelAndView dashboard_subjects(Principal principal) {
        ModelAndView modelAndView = tableController.fillModel(principal);
        Set<AbstractMap.SimpleEntry<String,String>> subjects_set = new HashSet<>();

        MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");

        List<Student> subjectsList;

        Query searchInstance = new Query(Criteria.where("ancestors").exists(true));
        subjectsList = mongoOperation.find(searchInstance, Student.class);
        for (Student el : subjectsList) {
            subjects_set.add(new AbstractMap.SimpleEntry<>(el.getSubject(), el.getGroup()));
        }
        for (AbstractMap.SimpleEntry<String,String> entry : subjects_set) {
            //System.out.println(entry.getKey(), entry.getValue());
        }
        System.out.println(subjectsList);
        System.out.println(subjects_set);

        modelAndView.addObject("subjects_set", subjects_set);
        modelAndView.addObject("subjects_list_size", subjects_set.size());
        modelAndView.setViewName("/dashboard/subjects_list");
        return modelAndView;
    }

    @RequestMapping(value = {"/users"}, method = RequestMethod.GET)
    public ModelAndView dashboard_users(Principal principal) {
        ModelAndView modelAndView = tableController.fillModel(principal);
        MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");
        List<User> usersList;

        Query searchInstance = new Query(Criteria.where("login").exists(true));
        usersList = mongoOperation.find(searchInstance, User.class);
        modelAndView.addObject("users_list", usersList);
        System.out.println(usersList);

        //modelAndView.addObject("subjects_set", subjects_set);
        //modelAndView.addObject("subjects_list_size", subjects_set.size());
        modelAndView.setViewName("/dashboard/users_list");
        return modelAndView;
    }

    @RequestMapping(value = {"/groups"}, method = RequestMethod.GET)
    public ModelAndView dashboard_groups(Principal principal) {
        ModelAndView modelAndView = tableController.fillModel(principal);

        Set<String> groups_set = new HashSet<>();

        MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");

        List<Student> subjectsList;

        Query searchInstance = new Query(Criteria.where("ancestors").exists(true));
        subjectsList = mongoOperation.find(searchInstance, Student.class);
        for (Student el : subjectsList) {
            groups_set.add(el.getGroup());
        }
        modelAndView.addObject("groups_set", groups_set);
        modelAndView.setViewName("/dashboard/groups_list");
        return modelAndView;
    }


    @RequestMapping(value = "/teachers", method = RequestMethod.POST)
    public ModelAndView addTeacher(@Valid Teacher teacher, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView("redirect:/dashboard/teachers");
        Teacher tecaherExists = userService.findTeacherByEmail(teacher.getEmail());
        if (tecaherExists != null) {
            bindingResult
                    .rejectValue("email", "error.user",
                            "There is already a user registered with the username provided");
        }
        if (bindingResult.hasErrors()) {
            //modelAndView.setViewName("/dashboard/teacher_list");
        } else {
            userService.saveTeacher(teacher);
            modelAndView.addObject("successMessage", "User has been registered successfully");
            modelAndView.addObject("teacher", new Teacher());
        }
        //modelAndView.setViewName("/dashboard/teacher_list");
        return modelAndView;
    }

    @RequestMapping(value = "/teachers/{id}", method = RequestMethod.DELETE)
    public ModelAndView deleteTeacher(@PathVariable String id) {
        ModelAndView modelAndView = new ModelAndView("redirect:/dashboard/teachers");
        userService.deleteTeacher(id);
        return modelAndView;
    }
}
