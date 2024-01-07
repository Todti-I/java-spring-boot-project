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
import ru.vedeshkin.project.entity.User;
import ru.vedeshkin.project.model.CustomUserDetails;
import ru.vedeshkin.project.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/users")
public class UsersController {

    private final UserRepository userRepository;

    @Autowired
    public UsersController(UserRepository userRepository) {
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

    @GetMapping("/delete")
    public String deleteUser(@RequestParam Long userId) {
        userRepository.deleteById(userId);
        return "redirect:/users";
    }

}
