package ru.vedeshkin.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import ru.vedeshkin.project.dto.UserDto;
import ru.vedeshkin.project.entity.User;
import ru.vedeshkin.project.model.CustomUserDetails;
import ru.vedeshkin.project.service.UserService;

import javax.validation.Valid;
import java.util.Optional;

@Controller
public class SecurityController {

    private final UserService userService;

    @Autowired
    public SecurityController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping({"/", "/index"})
    public ModelAndView index(@AuthenticationPrincipal CustomUserDetails user) {
        ModelAndView mav = new ModelAndView("index");
        mav.addObject("user", user);
        return mav;
    }

    @GetMapping("/login")
    public ModelAndView loginForm() {
        return new ModelAndView("login");
    }

    @GetMapping("/register")
    public ModelAndView registerForm() {
        ModelAndView mav = new ModelAndView("register");
        mav.addObject("user", new UserDto());
        return mav;
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") UserDto userDto,
                           BindingResult result,
                           Model model) {
        Optional<User> existingUser = userService.findByEmail(userDto.getEmail());

        if (existingUser.isPresent()) {
            result.rejectValue("email", null,
                    "На этот адрес электронной почты уже зарегистрирована учетная запись.");
        }

        if (result.hasErrors()) {
            model.addAttribute("user", userDto);
            return "/register";
        }

        userService.create(userDto);

        return "redirect:/register?success";
    }

}
