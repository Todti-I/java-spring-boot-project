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

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/users")
public class UserController {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @Autowired
    public UserController(RoleRepository roleRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @GetMapping()
    public ModelAndView getAllUsers(@AuthenticationPrincipal CustomUserDetails user) {
        List<UserDto> users = userRepository.findAll().stream()
                .map(UserDto::of)
                .toList();

        ModelAndView mav = new ModelAndView("list-users");
        mav.addObject("user", user);
        mav.addObject("users", users);

        return mav;
    }

    @GetMapping("/change-roles")
    public ModelAndView changeRolesForm(@AuthenticationPrincipal CustomUserDetails userDetails,
                                        @RequestParam Long userId) {
        ModelAndView mav = new ModelAndView("user-roles-form");
        mav.addObject("user", userDetails);
        userRepository.findById(userId).ifPresent(user -> {
            mav.addObject("userDto", UserDto.of(user));
        });
        mav.addObject("allRoles", roleRepository.findAll());

        return mav;
    }

    @PostMapping("/roles")
    public String saveUserRoles(@ModelAttribute UserDto userDto) {
        List<Role> roles = roleRepository.findAllById(userDto.getRoleIds());
        User user = userRepository.getReferenceById(userDto.getId());
        user.setRoles(roles);
        userRepository.save(user);

        return "redirect:/users";
    }

    @GetMapping("/delete")
    public String deleteUser(@RequestParam Long userId) {
        userRepository.deleteById(userId);
        return "redirect:/users";
    }

}
