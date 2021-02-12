package ru.web_marks.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import ru.web_marks.model.domain.Role;
import ru.web_marks.model.domain.User;
import ru.web_marks.repository.RoleRepository;
import ru.web_marks.repository.TeacherRepository;
import ru.web_marks.repository.UserRepository;
import ru.web_marks.service.CustomUserDetailsService;

import java.util.*;


@Service
public class MyCustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(userRequest);
        Map<String, Object> attributes = user.getAttributes();
        Set<GrantedAuthority> authorities = new HashSet<>(user.getAuthorities());


        // At this point, you would load your data (e.g. from database) and modify the authorities as you wish
        // For the sake of testing, we'll just add the role 'ADMIN' to the user
        //authorities.add(new SimpleGrantedAuthority("USER"));


        Role teacherRole = roleRepository.findByRole("TEACHER");
        Role userRole = roleRepository.findByRole("USER");
        User user_temp = userRepository.findByLogin((String) user.getAttributes().get("username"));

        if(user_temp == null) {
            User _user = new User();

            _user.setEnabled(true);
            _user.setEmail((String) user.getAttributes().get("email"));
            _user.setFullname((String) user.getAttributes().get("name"));
            _user.setLogin((String) user.getAttributes().get("username"));
            _user.setAvatar_url((String) user.getAttributes().get("avatar_url"));

            if (teacherRepository.findByEmail(_user.getEmail()) != null) {

                _user.setRole(teacherRole);
                authorities.add(new SimpleGrantedAuthority("TEACHER"));
            } else {

                _user.setRole(userRole);
                authorities.add(new SimpleGrantedAuthority("USER"));
            }
            userRepository.save(_user);
        }
        else if (teacherRepository.findByEmail(user_temp.getEmail()) != null) {
            if (!user_temp.getRole().getId().equals(teacherRole.getId())) {
                user_temp.setRole(teacherRole);
            }
            authorities.add(new SimpleGrantedAuthority("TEACHER"));
            userRepository.save(user_temp);
        }
        else {
            authorities.add(new SimpleGrantedAuthority("USER"));
            user_temp.setRole(userRole);
            userRepository.save(user_temp);
        }

        return new DefaultOAuth2User(authorities, attributes, "username");
    }

}

