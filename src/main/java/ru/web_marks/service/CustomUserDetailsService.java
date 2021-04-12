package ru.web_marks.service;

import ru.web_marks.model.Student;
import ru.web_marks.model.domain.Role;
import ru.web_marks.model.domain.Teacher;
import ru.web_marks.model.domain.User;
import ru.web_marks.repository.RoleRepository;
import ru.web_marks.repository.TeacherRepository;
import ru.web_marks.repository.UserRepository;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    public User findUserByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    public Teacher findTeacherByLogin(String login) {
        return teacherRepository.findByLogin(login);
    }

    public void saveUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setEnabled(true);
        Role userRole = roleRepository.findByRole("USER");
        user.setRole(userRole);
        userRepository.save(user);
    }

    public void saveTeacher(Teacher teacher) {
        teacherRepository.save(teacher);
    }

    public void deleteTeacher(String id) {
        teacherRepository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = userRepository.findByLogin(login);
        if(user != null) {
            List<GrantedAuthority> authorities = getUserAuthority(user.getRole());
            return buildUserForAuthentication(user, authorities);
        } else {
            throw new UsernameNotFoundException("username not found");
        }
    }

    public List<GrantedAuthority> getUserAuthority(Role userRole) {
        Set<GrantedAuthority> roles = new HashSet<>();
        roles.add(new SimpleGrantedAuthority(userRole.getRole()));

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>(roles);
        return grantedAuthorities;
    }

    private UserDetails buildUserForAuthentication(User user, List<GrantedAuthority> authorities) {
        return new org.springframework.security.core.userdetails.User(user.getLogin(), user.getPassword(), authorities);
    }

    public boolean deleteTeachers() {
        teacherRepository.deleteAll();
        return true;
    }

    public boolean deleteUsers() {
        userRepository.deleteAll();
        return true;
    }

    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }
}
