package ru.vedeshkin.project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.vedeshkin.project.entity.Role;
import ru.vedeshkin.project.entity.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;

    @NotEmpty(message = "Email should not be empty")
    @Email
    private String email;

    @NotEmpty(message = "Password should not be empty")
    private String password;

    private List<Long> roleIds = new ArrayList<>();

    public static UserDto of(User user) {
        UserDto dto = new UserDto();
        String[] names = user.getName().split(" ");
        dto.setId(user.getId());
        dto.setFirstName(names.length > 0 ? names[0] : "");
        dto.setLastName(names.length > 1 ? names[1] : "");
        dto.setEmail(user.getEmail());
        dto.setRoleIds(user.getRoles().stream().map(Role::getId).toList());
        return dto;
    }

}
