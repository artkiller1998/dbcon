package ru.web_marks.web.controllers;

import java.io.File;
import java.io.FilenameFilter;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.web_marks.model.Student;
import ru.web_marks.model.domain.Role;
import ru.web_marks.model.domain.Teacher;
import ru.web_marks.model.domain.User;

import javax.validation.Valid;
import java.security.Principal;
import java.util.stream.Stream;


@Controller
@RequestMapping(value = "dashboard", method = RequestMethod.PUT)
public class DashboardController {

    @Autowired
    private ru.web_marks.service.CustomUserDetailsService userService;

    @Autowired
    private TableController tableController;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    ApplicationContext ctx;

    @Value("${spring.config.profile}:local")
    public String profile;

    @RequestMapping(value = {"/"}, method = RequestMethod.GET)
    public ModelAndView dashboard(Principal principal) {
        ModelAndView modelAndView = tableController.fillModel(principal);

        modelAndView.setViewName("/dashboard/dashboard");
        return modelAndView;
    }

    @RequestMapping(value = "/change_password", method = RequestMethod.GET)
    public ModelAndView changePassword(Principal principal) {
        ModelAndView modelAndView = tableController.fillModel(principal);
        modelAndView.setViewName("/dashboard/change_password");
        return modelAndView;
    }

    @RequestMapping(value = "/change_password", method = RequestMethod.POST)
    public ModelAndView changePassword(Principal principal, @RequestParam("passOld") String passOld, @RequestParam("passOne") String passOne) {
        System.out.println("[INFO] DashboardController changePassword -- check password and change\n");
        ModelAndView modelAndView = tableController.fillModel(principal);
        User adminObject = userService.findUserByLogin("admin");
        if (bCryptPasswordEncoder.matches(passOld, adminObject.getPassword())) {
            userService.updateAdminPassword(bCryptPasswordEncoder.encode(passOne));
            modelAndView.addObject("message", "Пароль был успешно изменен");
        }
        else {
            modelAndView.addObject("message", "Неверный пароль администратора");
            modelAndView.addObject("color", "red");
        }

        return modelAndView;
    }

    @RequestMapping(value = {"/teachers"}, method = RequestMethod.GET)
    public ModelAndView dashboard_teachers(Principal principal) {
        System.out.println("[INFO] DashboardController dashboard_teachers -- show teachers\n");
        ModelAndView modelAndView = tableController.fillModel(principal);

        MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");

        List<Teacher> teacherList;

        Query searchInstance = new Query(Criteria.where("login").exists(true));
        teacherList = mongoOperation.find(searchInstance, Teacher.class);
        modelAndView.addObject("teachers_list", teacherList);
        modelAndView.setViewName("/dashboard/teachers_list");
        return modelAndView;
    }

    @RequestMapping(value = {"/teachers/{subject}/{year_group}"}, method = RequestMethod.GET)
    public ModelAndView subject_dashboard_teachers(@PathVariable String subject , @PathVariable String year_group, Principal principal) {
        System.out.println("[INFO] DashboardController subject_dashboard_teachers -- show subject_dashboard_teachers\n");
        ModelAndView modelAndView = tableController.fillModel(principal);

        MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");

        Query all_group_q = new Query(Criteria.where("ancestors").all(subject,year_group));
        List<Student> all_group = mongoOperation.find(all_group_q, Student.class);

        Query first_object_q = Query.query(Criteria.where("id").is(all_group.get(0).getId()));

        Student first_object = mongoOperation.findOne(first_object_q, Student.class);
        List<String> ancestors = first_object.getAncestors();
        ancestors.remove(1);
        ancestors.remove(0);
        ancestors.remove(0);

        modelAndView.addObject("ancestors_list", ancestors);
        modelAndView.addObject("subject", subject);
        modelAndView.addObject("year_group", year_group);
        modelAndView.setViewName("/dashboard/ancestors_list");
        return modelAndView;
    }

    @RequestMapping(value = "/teachers/{subject}/{year_group}", method = RequestMethod.POST)
    public ModelAndView addSubjectTeacher(@PathVariable String subject , @PathVariable String year_group, @Valid Teacher teacher, BindingResult bindingResult) {
        System.out.println("[INFO] DashboardController addSubjectTeacher --  addSubjectTeacher\n");

        ModelAndView modelAndView = new ModelAndView("redirect:/dashboard/teachers/{subject}/{year_group}");

        MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");
        Query all_group_q = new Query(Criteria.where("ancestors").all(subject,year_group));
        List<Student> all_group = mongoOperation.find(all_group_q, Student.class);

        Query first_object_q = Query.query(Criteria.where("id").is(all_group.get(0).getId()));
        Student first_object = mongoOperation.findOne(first_object_q, Student.class);

        List<String> ancestors = first_object.getAncestors();
        List<String> copy = new ArrayList<>(ancestors);
        copy.remove(1);
        copy.remove(0);
        copy.remove(0);

        Teacher tecaherExists = userService.findTeacherByLogin(teacher.getLogin());
        if (tecaherExists == null) {
            userService.saveTeacher(teacher);
            modelAndView.addObject("teacher", new Teacher());
        }

        if (copy.contains(teacher.getLogin()))
            bindingResult
                .rejectValue("login", "error.user",
                        "Такой пользователь уже существует");
        else {
            Update update = new Update();
            ancestors.add(teacher.getLogin());
            first_object.setAncestors((ArrayList<String>) ancestors);
            update.set("ancestors", first_object.getAncestors());
            mongoOperation.updateFirst(first_object_q, update, Student.class);
        }


//        if (bindingResult.hasErrors()) {
//            //modelAndView.setViewName("/dashboard/teacher_list");
//        } else {
//
//        }
        //modelAndView.setViewName("/dashboard/teacher_list");
//
        return modelAndView;
    }

    @RequestMapping(value = "/teachers/{subject}/{year_group}/{teacher_name}", method = RequestMethod.DELETE)
    public ModelAndView deleteSubjectTeacher(@PathVariable String subject , @PathVariable String year_group, @PathVariable String teacher_name) {
        System.out.println("[INFO] DashboardController deleteTeacher -- delete deleteSubjectTeacher\n");
        ModelAndView modelAndView = new ModelAndView("redirect:/dashboard/teachers/{subject}/{year_group}");

        MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");
        Query all_group_q = new Query(Criteria.where("ancestors").all(subject,year_group));
        List<Student> all_group = mongoOperation.find(all_group_q, Student.class);

        Query first_object_q = Query.query(Criteria.where("id").is(all_group.get(0).getId()));
        Student first_object = mongoOperation.findOne(first_object_q, Student.class);

        List<String> ancestors = first_object.getAncestors();
        ancestors.remove(teacher_name);
        first_object.setAncestors((ArrayList<String>) ancestors);

        Update update = new Update();
        update.set("ancestors", first_object.getAncestors());
        mongoOperation.updateFirst(first_object_q, update, Student.class);
        return modelAndView;
    }


    @RequestMapping(value = {"/subjects"}, method = RequestMethod.GET)
    public ModelAndView dashboard_subjects(Principal principal) {
        System.out.println("[INFO] DashboardController dashboard_subjects -- show subjects\n");
        ModelAndView modelAndView = tableController.fillModel(principal);
        Set<AbstractMap.SimpleEntry<String,String>> subjects_set = new HashSet<>();

        MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");

        List<Student> subjectsList;

        Query searchInstance = new Query(Criteria.where("ancestors").exists(true));
        subjectsList = mongoOperation.find(searchInstance, Student.class);
        for (Student el : subjectsList) {
            subjects_set.add(new AbstractMap.SimpleEntry<>(el.getSubject(), el.getGroup()));
        }

        modelAndView.addObject("subjects_set", subjects_set);
        modelAndView.addObject("subjects_list_size", subjects_set.size());
        modelAndView.setViewName("/dashboard/subjects_list");
        return modelAndView;
    }

    @RequestMapping(value = {"/users"}, method = RequestMethod.GET)
    public ModelAndView dashboard_users(Principal principal) {
        System.out.println("[INFO] DashboardController dashboard_users -- show users\n");
        ModelAndView modelAndView = tableController.fillModel(principal);
        MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");
        List<User> usersList;

        Query searchInstance = new Query(Criteria.where("login").exists(true));
        usersList = mongoOperation.find(searchInstance, User.class);
        modelAndView.addObject("users_list", usersList);

        modelAndView.setViewName("/dashboard/users_list");
        return modelAndView;
    }

    @RequestMapping(value = {"/groups"}, method = RequestMethod.GET)
    public ModelAndView dashboard_groups(Principal principal) {
        System.out.println("[INFO] DashboardController dashboard_groups -- show groups\n");
        ModelAndView modelAndView = tableController.fillModel(principal);

        Set<String> groups_set = new HashSet<>();

        String[] pathnames;

        // Creates a new File instance by converting the given pathname string
        // into an abstract pathname

        String csvFile;

        if (profile.equals("local")) {
            csvFile = "src/main/resources/static/csv/";
        }
        else {
            csvFile = "../webapps/dbconnector/WEB-INF/classes/static/csv/";
        }

        File f = new File(csvFile);

        FilenameFilter filter = new FilenameFilter() {
            @Override
            public boolean accept(File f, String name) {
                return name.endsWith(".CSV");
            }
        };

        pathnames = f.list(filter);

        if (pathnames != null) {
            for(int i = 0; i < pathnames.length; i++) {
                pathnames[i] = pathnames[i].split("\\.")[0];
            }
            modelAndView.addObject("groups_count", pathnames.length);
        }
        else
            modelAndView.addObject("groups_count", 0);

        modelAndView.addObject("groups_set", pathnames);

        modelAndView.setViewName("/dashboard/groups_list");
        return modelAndView;
    }


    @RequestMapping(value = "/teachers", method = RequestMethod.POST)
    public ModelAndView addTeacher(@Valid Teacher teacher, BindingResult bindingResult) {
        System.out.println("[INFO] DashboardController addTeacher -- add teacher\n");

        ModelAndView modelAndView = new ModelAndView("redirect:teachers/");
        Teacher tecaherExists = userService.findTeacherByLogin(teacher.getLogin());
        if (tecaherExists != null) {
            bindingResult
                    .rejectValue("login", "error.user",
                            "There is already a user registered with the username provided");
        }
        if (bindingResult.hasErrors()) {
            //modelAndView.setViewName("/dashboard/teacher_list");
        } else {
            userService.saveTeacher(teacher);
            modelAndView.addObject("teacher", new Teacher());
        }
        //modelAndView.setViewName("/dashboard/teacher_list");
        return modelAndView;
    }

    @RequestMapping(value = "/teachers/{id}", method = RequestMethod.DELETE)
    public ModelAndView deleteTeacher(@PathVariable String id) {
        System.out.println("[INFO] DashboardController deleteTeacher -- delete teacher\n");
        ModelAndView modelAndView = new ModelAndView("redirect:/dashboard/teachers");
        userService.deleteTeacher(id);
        return modelAndView;
    }

    @RequestMapping(value = "/users/{id}", method = RequestMethod.DELETE)
    public ModelAndView deleteUser(@PathVariable String id) {
        System.out.println("[INFO] DashboardController deleteUser -- delete user\n");
        ModelAndView modelAndView = new ModelAndView("redirect:/dashboard/users");
        userService.deleteUser(id);
        return modelAndView;
    }

    @RequestMapping(value = {"/collections_clear/users"}, method = RequestMethod.GET)
    public void usersClear(Principal principal) {
        System.out.println("[INFO] DashboardController usersClear -- all users clear\n");
        userService.deleteUsers();
    }

    @RequestMapping(value = {"/collections_clear/teachers"}, method = RequestMethod.GET)
    public void collectionsClear(Principal principal) {
        System.out.println("[INFO] DashboardController collectionsClear -- all collections clear\n");
        userService.deleteTeachers();
    }


}
