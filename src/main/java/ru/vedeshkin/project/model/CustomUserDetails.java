package ru.vedeshkin.project.model;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import ru.vedeshkin.project.entity.User;

import java.util.stream.Collectors;

public class CustomUserDetails extends org.springframework.security.core.userdetails.User {

    private final boolean canRead;
    private final boolean canEdit;
    private final boolean isAdmin;

    private final User user;

    public CustomUserDetails(User user) {
        super(user.getEmail(),
                user.getPassword(),
                user.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.getName()))
                        .collect(Collectors.toList())
        );
        isAdmin = user.getRoles().stream().anyMatch(a -> a.getName().equals("ROLE_ADMIN"));
        canRead = isAdmin || user.getRoles().stream().anyMatch(a -> a.getName().equals("ROLE_READ_ONLY"));
        canEdit = isAdmin || user.getRoles().stream().anyMatch(a -> a.getName().equals("ROLE_USER"));
        this.user = user;
    }

    public boolean canRead() {
        return canRead;
    }

    public boolean canEdit() {
        return canEdit;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public Long getId() {
        return user.getId();
    }

    public User getUserEntity() {
        return user;
    }

}
