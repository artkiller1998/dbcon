package ru.web_marks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.web_marks.model.domain.Role;
import ru.web_marks.model.domain.Teacher;
import ru.web_marks.model.domain.User;
import ru.web_marks.repository.RoleRepository;
import ru.web_marks.repository.TeacherRepository;
import ru.web_marks.repository.UserRepository;
import ru.web_marks.service.CustomUserDetailsService;


@SpringBootApplication
@ComponentScan({"ru.web_marks.*"})
@EntityScan("ru.web_marks.*")
public class Application extends SpringBootServletInitializer {
   // public class Application  {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private UserRepository userRepository;

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(Application.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args); }


    @Bean
    CommandLineRunner init(RoleRepository roleRepository,
                           TeacherRepository teacherRepository,
                           UserRepository userRepository,
                            CustomUserDetailsService customUserDetailsService) {

        return args -> {
            Role adminRole = roleRepository.findByRole("ADMIN");
            Role newAdminRole;
            if (adminRole == null) {
                newAdminRole = new Role();
                newAdminRole.setRole("ADMIN");
                roleRepository.save(newAdminRole);
            }
            else {
                newAdminRole = adminRole;
            }

            Role teacherRole = roleRepository.findByRole("TEACHER");
            if (teacherRole == null) {
                Role newTeacherRole = new Role();
                newTeacherRole.setRole("TEACHER");
                roleRepository.save(newTeacherRole);
            }
            Role userRole = roleRepository.findByRole("USER");
            if (userRole == null) {
                Role newUserRole = new Role();
                newUserRole.setRole("USER");
                roleRepository.save(newUserRole);
            }

            Teacher teacher = teacherRepository.findByEmail("artkiller1998@1.ru");
            if (teacher == null) {
                Teacher newTeacher = new Teacher();
                newTeacher.setEmail("artkiller1998@1.ru");
                teacherRepository.save(newTeacher);
            }

            User user = userRepository.findByLogin("admin");
            if (user == null) {
                User newUser = new User();
                newUser.setLogin("admin");
                newUser.setEnabled(true);
                newUser.setFullname("admin");
                newUser.setEmail("admin@1.rus");
                newUser.setPassword(bCryptPasswordEncoder.encode("a"));
                newUser.setAvatar_url("/dbconnector/favicon.ico");
                newUser.setRole(newAdminRole);
                userRepository.save(newUser);
            }
        };

    }
}