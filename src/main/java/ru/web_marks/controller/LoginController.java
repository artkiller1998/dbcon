/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.web_marks.controller;


import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.web_marks.domain.User;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;
import ru.web_marks.repository.RoleRepository;
import ru.web_marks.web.controllers.ExampleController;

import java.security.Principal;

@Controller
public class LoginController {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ru.web_marks.service.CustomUserDetailsService userService;

    @Autowired
    private ExampleController exampleController;

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public ModelAndView login() {
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("admin");
        return modelAndView;
    }

//    @GetMapping("/table")
//    public ModelAndView table_view() {
//        ModelAndView modelAndView = new ModelAndView();
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        auth.getAuthorities();
//        auth.getDetails();
//        //OAuth2User user = exampleController.getCurrentUser();
////        String login = (String) user.getAttributes().get("name");
////        User _user = userService.findUserByLogin(login);
////        modelAndView.addObject("currentUser", _user);
////        modelAndView.addObject("fullName", "Welcome " + _user.getFullname());
//        modelAndView.setViewName("table");
//        return modelAndView;
//    }

    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public ModelAndView signup() {
        ModelAndView modelAndView = new ModelAndView();


        ///modelAndView.addObject("user", user);
        modelAndView.setViewName("signup");
        return modelAndView;
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public ModelAndView createNewUser(@Valid User user, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        User userExists = userService.findUserByLogin(user.getLogin());
        if (userExists != null) {
            bindingResult
                    .rejectValue("login", "error.user",
                            "There is already a user registered with the username provided");
        }
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("signup");
        } else {
//            Set<Role> authorities = new HashSet<>(user.getRoles());
//            Role teacherRole = roleRepository.findByRole("TEACHER");
//            authorities.add(teacherRole);

            userService.saveUser(user);
            modelAndView.addObject("successMessage", "User has been registered successfully");
            modelAndView.addObject("user", new User());
            modelAndView.setViewName("admin");

        }
        return modelAndView;
    }

    @RequestMapping(value = "/dashboard", method = RequestMethod.GET)
    public ModelAndView dashboard() {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByLogin(auth.getName());
        modelAndView.addObject("currentUser", user);
        modelAndView.addObject("fullName", user.getFullname());
        modelAndView.addObject("adminMessage", "Content Available Only for Users with Admin Role");
        modelAndView.setViewName("dashboard");
        return modelAndView;
    }
    



//    private static String authorizationRequestBaseUri
//            = "oauth2/authorization";
//    Map<String, String> oauth2AuthenticationUrls
//            = new HashMap<>();
//
//    @Autowired
//    private ClientRegistrationRepository clientRegistrationRepository;
//
    @RequestMapping(value = {"/login"}, method = RequestMethod.GET)
    public ModelAndView loginOauth(Model model) {
        ModelAndView modelAndView = new ModelAndView();

//        OAuth2User user = exampleController.getCurrentUser();
//        StringBuffer authorities = new StringBuffer();
//        user.getAuthorities().forEach((a) -> authorities.append(a.toString()).append(","));
////        modelAndView.addObject("user", user);
//        System.out.println(user);
//        model.addAttribute("currentUser", user);
//        model.addAttribute("fullName", user.getAttributes().get("username"));

        modelAndView.setViewName("studentsTable");
        return modelAndView;
    }

}
