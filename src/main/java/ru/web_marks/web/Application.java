package ru.web_marks.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import ru.web_marks.config.MongoConfig;
import ru.web_marks.model.Student;
import ru.web_marks.view.MongoDBPOperations;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);


        ApplicationContext ctx = new AnnotationConfigApplicationContext(MongoConfig.class);
//        // интерфейс для использования mongoTemplate
        MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");
//        // конкретная реализация интерфейса для объекта Student
        MongoDBPOperations ops = new MongoDBPOperations();

        Student student = new Student("John", "Smith",22);

        student.setInstanceMark("3");
        student.setInstanceMark("4");
        student.setInstanceMark("5");
        student.setInstanceMark("+");
        student.setInstanceMark("ANY");
        student.setInstanceMark("99");
        student.setInstanceMark("9");


        ops.saveInstance(mongoOperation, student);
    }
}


// предоставляет информацию о конфигурации приложения
//        ApplicationContext ctx = new AnnotationConfigApplicationContext(MongoConfig.class);
//        // интерфейс для использования mongoTemplate
//        MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");
//        // конкретная реализация интерфейса для объекта Student
//        MongoDBPOperations ops = new MongoDBPOperations();
//
//        // создать объект, тип - студент ( ЛОКАЛЬНО )
//        Student student = new Student("John", "Smith",22);
//        Student student_r = new Student("Ivan", "Ivanov",19);
//        // поставить оценку
//        student.setInstanceMark(3);
//        student.setInstanceMark(4);
//        student.setInstanceMark(5);
//
//        student_r.setInstanceMark(5);
//        student_r.setInstanceMark(5);
//        student_r.setInstanceMark(5);
//
//        // сохранить объект (В БД)
//        ops.saveInstance(mongoOperation, student);
//        ops.saveInstance(mongoOperation, student_r);
//
//        // получить объект по критерию ( WHERE )
//        ops.searchInstance(mongoOperation, "lastName", "Smith");
//
//
//        // изменить объект по критерию ( WHERE )
//        ops.updateInstance(mongoOperation, "lastName", "Smith", "studentAge", "18");
//
//        //student_r.setInstanceMark(1);
//        ops.updateInstance(mongoOperation, "lastName", "Ivanov", "studentMarks", 1);
//
//        ops.searchInstance(mongoOperation, "lastName", "Smith");
//
//        //  получить все объекты
//        ops.getAllInstances(mongoOperation);

// удалить объект по критерию ( WHERE )
//ops.removeStudent(mongoOperation, "studentName", "John");
// get all the students
//ops.getAllStudent(mongoOperation);