package ru.vedeshkin.project.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.vedeshkin.project.dto.UserDto;
import ru.vedeshkin.project.entity.Role;
import ru.vedeshkin.project.entity.User;
import ru.vedeshkin.project.model.CustomUserDetails;
import ru.vedeshkin.project.repository.RoleRepository;
import ru.vedeshkin.project.repository.UserRepository;
import ru.vedeshkin.project.service.UserService;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/users")
public class UserController {

    private final RoleRepository roleRepository;
    private final UserService userService;

    @Autowired
    public UserController(RoleRepository roleRepository, UserService userService) {
        this.roleRepository = roleRepository;
        this.userService = userService;
    }

    @GetMapping()
    public ModelAndView getAllUsers(@AuthenticationPrincipal CustomUserDetails user) {
        ModelAndView mav = new ModelAndView("list-users");
        mav.addObject("user", user);
        mav.addObject("users", userService.findAll());

        return mav;
    }

    @GetMapping("/change-roles")
    public ModelAndView changeRolesForm(@AuthenticationPrincipal CustomUserDetails userDetails,
                                        @RequestParam Long userId) {
        ModelAndView mav = new ModelAndView("user-roles-form");
        mav.addObject("user", userDetails);
        userService.findById(userId).ifPresent(user -> mav.addObject("userDto", UserDto.of(user)));
        mav.addObject("allRoles", roleRepository.findAll());

        return mav;
    }

    @PostMapping("/roles")
    public String saveUserRoles(@ModelAttribute UserDto userDto) {
        userService.saveRoles(userDto);

        return "redirect:/users";
    }

    @GetMapping("/delete")
    public String deleteUser(@RequestParam Long userId) {
        userService.deleteById(userId);

        return "redirect:/users";
    }

}
