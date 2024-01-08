package ru.vedeshkin.project.service;

import ru.vedeshkin.project.dto.UserDto;
import ru.vedeshkin.project.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);

    List<User> findAll();

    User create(UserDto userDto);

    User saveRoles(UserDto userDto);

    void deleteById(Long id);

}
