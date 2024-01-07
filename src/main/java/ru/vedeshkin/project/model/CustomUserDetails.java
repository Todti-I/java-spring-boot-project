package ru.vedeshkin.project.model;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import ru.vedeshkin.project.entity.User;

import java.util.stream.Collectors;

public class CustomUserDetails extends org.springframework.security.core.userdetails.User {

    private final boolean isAdmin;

    public CustomUserDetails(User user) {
        super(user.getEmail(),
                user.getPassword(),
                user.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.getName()))
                        .collect(Collectors.toList())
        );
        isAdmin = user.getRoles().stream().anyMatch(a -> a.getName().equals("ROLE_ADMIN"));
    }

    public boolean isAdmin() {
        return isAdmin;
    }
}
