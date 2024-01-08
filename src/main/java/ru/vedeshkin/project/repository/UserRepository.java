package ru.vedeshkin.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.vedeshkin.project.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

}
