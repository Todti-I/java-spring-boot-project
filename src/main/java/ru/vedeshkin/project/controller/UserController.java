package ru.vedeshkin.project.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ru.vedeshkin.project.dto.UserDto;
import ru.vedeshkin.project.model.CustomUserDetails;
import ru.vedeshkin.project.repository.UserRepository;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping()
    public ModelAndView getAllUsers(@AuthenticationPrincipal CustomUserDetails user) {
        List<UserDto> users = userRepository.findAll().stream()
                .map(UserDto::mapTo)
                .toList();

        ModelAndView mav = new ModelAndView("list-users");
        mav.addObject("user", user);
        mav.addObject("users", users);

        return mav;
    }

}
