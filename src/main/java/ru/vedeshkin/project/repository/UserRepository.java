package ru.vedeshkin.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.vedeshkin.project.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

}
