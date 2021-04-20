package ru.web_marks.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.web_marks.model.domain.Backup;
import ru.web_marks.model.domain.User;


public interface BackupRepository extends MongoRepository<Backup, String> {
    User findByAncestors(String login);
}
