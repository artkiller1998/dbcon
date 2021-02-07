package ru.web_marks.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import ru.web_marks.model.domain.Role;
import ru.web_marks.model.domain.User;
import ru.web_marks.repository.RoleRepository;

import java.security.Principal;

@Controller
public class TableController {

    @Autowired
    private ru.web_marks.service.CustomUserDetailsService userService;

    @Autowired
    private RoleRepository roleRepository;

    public ModelAndView fillModel(Principal principal){
        ModelAndView modelAndView = new ModelAndView();
        try {
            String login = principal.getName();
            User user = userService.findUserByLogin(login);;
            Role userRole = roleRepository.findByRole("USER");

            Role role = user.getRole();

            if (role.getId().equals(userRole.getId())) {
                modelAndView.addObject("role", "USER");
            }
            else {
                modelAndView.addObject("role", "TEACHER");
            }

            modelAndView.addObject("currentUser", user);
            modelAndView.addObject("fullName", user.getFullname());
            modelAndView.addObject("avatarUrl", user.getAvatar_url());
        }
        catch (Exception ex) {

        }
        return  modelAndView;
    }

    @RequestMapping(value = {"/table"}, method = RequestMethod.GET)
    public ModelAndView table_view(Principal principal) {
        ModelAndView modelAndView = fillModel(principal);

        modelAndView.setViewName("table");
        return modelAndView;
    }

    @RequestMapping(value = {"/","/home"}, method = RequestMethod.GET)
    public ModelAndView home(Principal principal) {
        ModelAndView modelAndView = fillModel(principal);

        modelAndView.setViewName("studentsTable");
        return modelAndView;
    }



}

