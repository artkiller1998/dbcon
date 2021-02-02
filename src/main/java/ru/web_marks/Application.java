package ru.web_marks;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import ru.web_marks.domain.Role;
import ru.web_marks.repository.RoleRepository;


@SpringBootApplication
@ComponentScan({"ru.web_marks.*"})
@EntityScan("ru.web_marks.*")
<<<<<<< HEAD


=======
//@PropertySource(name = "ConfigurationFromFile", value = "/classes/application.properties")
>>>>>>> parent of 7fc04e6... Works perfect + tomcat support
public class Application extends SpringBootServletInitializer {
   // public class Application  {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(Application.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args); }

<<<<<<< HEAD
=======

//    @Bean
//    public ViewResolver viewResolver() {
//        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
//        templateResolver.setTemplateMode("XHTML");
//        templateResolver.setPrefix("views/");
//        templateResolver.setSuffix(".html");
//
//        SpringTemplateEngine engine = new SpringTemplateEngine();
//        engine.setTemplateResolver(templateResolver);
//
//        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
//        viewResolver.setTemplateEngine(engine);
//        return viewResolver;
//    }

>>>>>>> parent of 7fc04e6... Works perfect + tomcat support
    @Bean
    CommandLineRunner init(RoleRepository roleRepository) {

        return args -> {

            Role adminRole = roleRepository.findByRole("ADMIN");
            if (adminRole == null) {
                Role newAdminRole = new Role();
                newAdminRole.setRole("ADMIN");
                roleRepository.save(newAdminRole);
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
        };

    }
}