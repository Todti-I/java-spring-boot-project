package ru.vedeshkin.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.vedeshkin.project.dto.UserDto;
import ru.vedeshkin.project.entity.Role;
import ru.vedeshkin.project.entity.User;
import ru.vedeshkin.project.repository.RoleRepository;
import ru.vedeshkin.project.repository.UserRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User create(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getFirstName() + " " + userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        Optional<Role> optionalRole = roleRepository.findByName("ROLE_READ_ONLY");
        if (optionalRole.isEmpty()) {
            throw new NoSuchElementException("Role READ_ONLY not found");
        }
        user.setRoles(optionalRole.stream().toList());
        return userRepository.save(user);
    }

    @Override
    public User saveRoles(UserDto userDto) {
        List<Role> roles = roleRepository.findAllById(userDto.getRoleIds());
        User user = userRepository.getReferenceById(userDto.getId());
        user.setRoles(roles);
        return userRepository.save(user);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

}
