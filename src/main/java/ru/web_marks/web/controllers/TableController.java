package ru.web_marks.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ru.web_marks.domain.User;

import java.security.Principal;

@Controller
public class TableController {

    @Autowired
    private ru.web_marks.service.CustomUserDetailsService userService;

    @RequestMapping(value = {"/","/home"}, method = RequestMethod.GET)
    public ModelAndView home(Principal principal) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            String princ_str = principal.getName();
            User user = userService.findUserByLogin(princ_str);

            modelAndView.addObject("currentUser", user);
            modelAndView.addObject("fullName", user.getFullname());
            modelAndView.addObject("avatarUrl", user.getAvatar_url());
        }
        catch (Exception ex) {

        }
        modelAndView.setViewName("studentsTable");
        return modelAndView;
    }

    @RequestMapping(value = {"/table"}, method = RequestMethod.GET)
    public ModelAndView table_view(Principal principal) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            String princ_str = principal.getName();
            User user = userService.findUserByLogin(princ_str);

            modelAndView.addObject("currentUser", user);
            modelAndView.addObject("fullName", user.getFullname());
            modelAndView.addObject("avatarUrl", user.getAvatar_url());
        }
        catch (Exception ex) {

        }


        modelAndView.setViewName("table");
        return modelAndView;
    }

}

