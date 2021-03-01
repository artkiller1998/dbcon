//package ru.web_marks.web.controllers;
//
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//
//@RestController
//public class UserinfoController {
//
//    @GetMapping("/me")
//    public String index() {
//        OAuth2User user = getCurrentUser();
//
//        StringBuffer authorities = new StringBuffer();
//        user.getAuthorities().forEach((a) -> authorities.append(a.toString()).append(","));
//        return "Hello " + user.getAttributes().get("name") + ". Your email is " + user.getAttributes().get("email")
//                + ".Your URL is " + user.getAttributes().get("web_url") + ".Your username " + user.getAttributes().get("username") +
//                ".Your email is " + user.getAttributes().get("artkiller1998@1.ru") +
//                " and your profile picture is <img src='"+user.getAttributes().get("avatar_url")+"' /> <br />"
//                + "You have the following attributes: " + authorities.toString() + "<br />"
//                + "<a href='/logout'>logout</a>";
//    }
//
//    public OAuth2User getCurrentUser() {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        return ((OAuth2AuthenticationToken)auth).getPrincipal();
//    }
//
//}