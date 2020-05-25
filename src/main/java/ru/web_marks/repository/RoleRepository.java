package ru.web_marks.repository;

import ru.web_marks.domain.Role;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface RoleRepository extends MongoRepository<Role, String> {
    Role findByRole(String role);
}