package ru.web_marks.repository;

import ru.web_marks.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface UserRepository extends MongoRepository<User, String> {
    User findByLogin(String login);
}
