package ru.vedeshkin.project.service;

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
    public void create(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getFirstName() + " " + userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        Role role = roleRepository.findByName("ROLE_READ_ONLY");
        if (role == null) {
            throw new NoSuchElementException("Role READ_ONLY not found");
        }
        user.setRoles(List.of(role));
        userRepository.save(user);
    }

    @Override
    public void saveRoles(UserDto userDto) {
        List<Role> roles = roleRepository.findAllById(userDto.getRoleIds());
        User user = userRepository.getReferenceById(userDto.getId());
        user.setRoles(roles);
        userRepository.save(user);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

}
