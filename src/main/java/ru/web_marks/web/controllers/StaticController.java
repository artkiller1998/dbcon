package ru.web_marks.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class StaticController {
    @RequestMapping(value = "/static/css/{path}", method = RequestMethod.GET)
    public ModelAndView redirectStaticCss(@PathVariable("path") String path) {
        ModelAndView modelAndView = new ModelAndView("redirect:https://sms.gitwork.ru/static/dbconnector/css/"+path);
        return modelAndView;
    }

    @RequestMapping(value = "/static/js/{path}", method = RequestMethod.GET)
    public ModelAndView redirectStaticJs(@PathVariable("path") String path) {
        ModelAndView modelAndView = new ModelAndView("redirect:https://sms.gitwork.ru/static/dbconnector/js/"+path);
        return modelAndView;
    }

    @RequestMapping(value = "/static/fonts/{path}", method = RequestMethod.GET)
    public ModelAndView redirectStaticFonts(@PathVariable("path") String path) {
        ModelAndView modelAndView = new ModelAndView("redirect:https://sms.gitwork.ru/static/dbconnector/fonts/"+path);
        return modelAndView;
    }

    @RequestMapping(value = "/static/fonts/flaticon/{path}", method = RequestMethod.GET)
    public ModelAndView redirectStaticFontsFi(@PathVariable("path") String path) {
        ModelAndView modelAndView = new ModelAndView("redirect:https://sms.gitwork.ru/static/dbconnector/fonts/flaticon/"+path);
        return modelAndView;
    }

    @RequestMapping(value = "/static/img/{path}", method = RequestMethod.GET)
    public ModelAndView redirectStaticImg(@PathVariable("path") String path) {
        ModelAndView modelAndView = new ModelAndView("redirect:https://sms.gitwork.ru/static/dbconnector/img/"+path);
        return modelAndView;
    }

    @RequestMapping(value = "/static/img/loaders/{path}", method = RequestMethod.GET)
    public ModelAndView redirectStaticImgLd(@PathVariable("path") String path) {
        ModelAndView modelAndView = new ModelAndView("redirect:https://sms.gitwork.ru/static/dbconnector/img/loaders/"+path);
        return modelAndView;
    }

    @RequestMapping(value = "/static/img/logo/{path}", method = RequestMethod.GET)
    public ModelAndView redirectStaticImgLg(@PathVariable("path") String path) {
        ModelAndView modelAndView = new ModelAndView("redirect:https://sms.gitwork.ru/static/dbconnector/img/logo/"+path);
        return modelAndView;
    }

    @RequestMapping(value = "/static/img/manifest/{path}", method = RequestMethod.GET)
    public ModelAndView redirectStaticImgMnf(@PathVariable("path") String path) {
        ModelAndView modelAndView = new ModelAndView("redirect:https://sms.gitwork.ru/static/dbconnector/img/manifest/"+path);
        return modelAndView;
    }

    @RequestMapping(value = "/static/img/masks/{path}", method = RequestMethod.GET)
    public ModelAndView redirectStaticImgMas(@PathVariable("path") String path) {
        ModelAndView modelAndView = new ModelAndView("redirect:https://sms.gitwork.ru/static/dbconnector/img/masks/"+path);
        return modelAndView;
    }

    @RequestMapping(value = "/static/favicon.ico", method = RequestMethod.GET)
    public ModelAndView redirectStaticFvc() {
        ModelAndView modelAndView = new ModelAndView("redirect:https://sms.gitwork.ru/static/dbconnector/favicon.ico");
        return modelAndView;
    }
}
