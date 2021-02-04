package ru.web_marks.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.web_marks.domain.Teacher;

public interface TeacherRepository extends MongoRepository<Teacher, String> {
    Teacher findByEmail(String email);
}
