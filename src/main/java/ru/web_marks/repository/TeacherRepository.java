package ru.web_marks.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.web_marks.model.domain.Teacher;

public interface TeacherRepository extends MongoRepository<Teacher, String> {
    Teacher findByLogin(String login);
}
