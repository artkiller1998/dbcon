package ru.web_marks.web.controllers;

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

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

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

        MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");

        List<Student> subjectsList = new ArrayList<>();

        Query searchInstance = new Query(Criteria.where("ancestors").exists(true));
        subjectsList = mongoOperation.find(searchInstance, Student.class);
        modelAndView.addObject("subjects_list", subjectsList);
        modelAndView.addObject("subjects_list_size", subjectsList.size());
        for (Student el : subjectsList) {

        }
        System.out.println(subjectsList);
        modelAndView.setViewName("/dashboard/subjects_list");
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
