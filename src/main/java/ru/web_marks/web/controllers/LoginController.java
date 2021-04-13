/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.web_marks.web.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {

    @Autowired
    private ru.web_marks.service.CustomUserDetailsService userService;

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public ModelAndView login() {
        System.out.println("[INFO] LoginController login -- login\n");
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.addObject("role", "USER");
        modelAndView.setViewName("admin");
        return modelAndView;
    }

}
