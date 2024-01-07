package ru.vedeshkin.project.service;

import ru.vedeshkin.project.dto.UserDto;
import ru.vedeshkin.project.entity.User;

import java.util.List;

public interface UserService {

    void saveUser(UserDto userDto);

    User findUserByEmail(String email);

    List<UserDto> findAllUsers();

}
